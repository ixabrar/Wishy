import asyncio
import requests
from bs4 import BeautifulSoup
from urllib.parse import urlparse
from app.config.settings import settings
from app.parsers.universal import UniversalParser
from app.services.browser import PlaywrightEngine

class ExtractionService:
    # We prioritize parsers. If a store-specific parser returns True for can_parse(), use it.
    # Otherwise fallback to the universal JSON-LD / OpenGraph parser.
    PARSERS = [
        UniversalParser
    ]

    @classmethod
    async def extract(cls, url: str) -> dict:
        headers = {
            "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
            "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
            "Accept-Language": "en-US,en;q=0.5",
            "Connection": "keep-alive",
            "Upgrade-Insecure-Requests": "1"
        }
        
        try:
            # Step 1: Fast Static Path (Non-blocking)
            response = await asyncio.to_thread(
                requests.get, url, headers=headers, timeout=settings.request_timeout
            )
            response.raise_for_status()
            
            resolved_url = response.url
            soup = BeautifulSoup(response.content, "html.parser")
            
            # Step 2: Extract & Evaluate Confidence
            static_data = None
            for ParserClass in cls.PARSERS:
                parser = ParserClass(soup, resolved_url)
                if parser.can_parse():
                    static_data = parser.parse()
                    static_data["resolved_url"] = resolved_url
                    break
            
            if not static_data:
                static_data = cls._build_graceful_fallback(url, resolved_url)
                
            # Step 3: Escalation Logic
            confidence = static_data.get("confidence", 0)
            if confidence >= 60:
                print(f"Extraction Service: Static confidence {confidence}/100. Fast path succeeded.")
                return static_data
                
            print(f"Extraction Service: Static confidence {confidence}/100. Escalating to Playwright Rendering Layer...")
            
            # Step 4: Heavy Playwright Hydration
            html_content = await PlaywrightEngine.render_page(resolved_url)
            if html_content:
                rendered_soup = BeautifulSoup(html_content, "html.parser")
                for ParserClass in cls.PARSERS:
                    parser = ParserClass(rendered_soup, resolved_url)
                    if parser.can_parse():
                        rendered_data = parser.parse()
                        rendered_data["resolved_url"] = resolved_url
                        
                        # Return whichever approach yielded better results
                        if rendered_data.get("confidence", 0) > confidence:
                            print(f"Extraction Service: Playwright succeeded. New confidence {rendered_data.get('confidence')}/100.")
                            return rendered_data
                            
            print("Extraction Service: Playwright fallback provided no improvement.")
            return static_data
            
        except Exception as e:
            # Graceful Fallback Object instead of HTTP 500 when possible
            print(f"Extraction Service Failed: {e}")
            return cls._build_graceful_fallback(url)
            
    @staticmethod
    def _build_graceful_fallback(original_url: str, resolved_url: str = None) -> dict:
        final_url = resolved_url or original_url
        domain = urlparse(final_url).netloc.replace("www.", "")
        return {
            "title": "Unprocessed Link",
            "image": None,
            "price": None,
            "brand": domain,
            "store": domain,
            "resolved_url": final_url,
            "canonical_url": final_url,
            "confidence": 0,
            "success": False
        }
