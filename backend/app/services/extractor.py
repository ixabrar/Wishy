import requests
from bs4 import BeautifulSoup
from urllib.parse import urlparse
from app.config.settings import settings
from app.parsers.generic import GenericParser
# Future imports: from app.parsers.amazon import AmazonParser

class ExtractionService:
    # We will prioritize parsers. If a store-specific parser returns True for can_parse(), use it.
    # Otherwise fallback to the generic OpenGraph parser.
    PARSERS = [
        # AmazonParser,
        GenericParser
    ]

    @classmethod
    def extract(cls, url: str) -> dict:
        headers = {
            "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
            "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8",
            "Accept-Language": "en-US,en;q=0.5",
            "Connection": "keep-alive",
            "Upgrade-Insecure-Requests": "1"
        }
        
        try:
            # Request hardening: use realistic headers and strict timeouts
            response = requests.get(url, headers=headers, timeout=settings.request_timeout)
            response.raise_for_status()
            
            # request.url is the starting URL, response.url is the FINAL resolved destination (after redirects)
            resolved_url = response.url
            soup = BeautifulSoup(response.content, "html.parser")
            
            # Find the first capable parser using the resolved URL
            for ParserClass in cls.PARSERS:
                parser = ParserClass(soup, resolved_url)
                if parser.can_parse():
                    parsed_data = parser.parse()
                    parsed_data["resolved_url"] = resolved_url
                    return parsed_data
            
            # Absolute worst-case fallback
            return cls._build_graceful_fallback(url, resolved_url)
            
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
            "success": False
        }
