from abc import ABC, abstractmethod
from typing import Optional
from bs4 import BeautifulSoup
from urllib.parse import urlparse

class BaseParser(ABC):
    def __init__(self, soup: BeautifulSoup, url: str):
        self.soup = soup
        self.url = url
        self.domain = urlparse(url).netloc.replace("www.", "")

    @abstractmethod
    def can_parse(self) -> bool:
        """Determines if this parser is capable of handling the current URL or DOM."""
        pass

    @abstractmethod
    def extract_title(self) -> str:
        pass

    @abstractmethod
    def extract_image(self) -> Optional[str]:
        pass

    @abstractmethod
    def extract_price(self) -> Optional[str]:
        pass

    @abstractmethod
    def extract_brand(self) -> Optional[str]:
        pass

    def parse(self) -> dict:
        """Executes the full parsing strategy and returns the structured dictionary."""
        return {
            "title": self.extract_title(),
            "image": self.extract_image(),
            "price": self.extract_price(),
            "brand": self.extract_brand() or self.domain,
            "store": self.domain,
            "success": True
        }
