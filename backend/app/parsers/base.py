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

    @abstractmethod
    def extract_canonical_url(self) -> str:
        pass

    def calculate_confidence(self, data: dict) -> int:
        """Returns a 0-100 score based on data completeness."""
        score = 0
        if data.get("title") and data["title"] != "Unknown Product": score += 40
        if data.get("image"): score += 30
        if data.get("price"): score += 20
        if data.get("brand") and data["brand"] != self.domain: score += 10
        return score

    def parse(self) -> dict:
        """Executes the full parsing strategy and returns the structured dictionary."""
        data = {
            "title": self.extract_title(),
            "image": self.extract_image(),
            "price": self.extract_price(),
            "brand": self.extract_brand() or self.domain,
            "store": self.domain,
            "canonical_url": self.extract_canonical_url(),
            "success": True
        }
        data["confidence"] = self.calculate_confidence(data)
        return data
