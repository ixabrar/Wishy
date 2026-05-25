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
            "Accept-Language": "en-US,en;q=0.9",
        }
        
        try:
            # Request hardening: use realistic headers and strict timeouts
            response = requests.get(url, headers=headers, timeout=settings.request_timeout)
            response.raise_for_status()
            
            soup = BeautifulSoup(response.content, "html.parser")
            
            # Find the first capable parser
            for ParserClass in cls.PARSERS:
                parser = ParserClass(soup, url)
                if parser.can_parse():
                    return parser.parse()
            
            # Absolute worst-case fallback if all parsers fail (shouldn't happen since Generic always returns True)
            return cls._build_graceful_fallback(url)
            
        except Exception as e:
            # Graceful Fallback Object instead of HTTP 500 when possible
            print(f"Extraction Service Failed: {e}")
            return cls._build_graceful_fallback(url)
            
    @staticmethod
    def _build_graceful_fallback(url: str) -> dict:
        domain = urlparse(url).netloc.replace("www.", "")
        return {
            "title": "Unprocessed Link",
            "image": None,
            "price": None,
            "brand": domain,
            "store": domain,
            "success": False
        }
