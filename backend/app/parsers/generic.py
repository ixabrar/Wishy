from app.parsers.base import BaseParser
from typing import Optional

class GenericParser(BaseParser):
    def can_parse(self) -> bool:
        # The generic parser is the fallback, it can always attempt to parse.
        return True

    def extract_title(self) -> str:
        # 1. Try OpenGraph
        og_title = self.soup.find("meta", property="og:title")
        if og_title and og_title.get("content"):
            return og_title["content"].strip()
            
        # 2. Try standard meta title
        meta_title = self.soup.find("meta", attrs={"name": "title"})
        if meta_title and meta_title.get("content"):
            return meta_title["content"].strip()
            
        # 3. Try native title
        if self.soup.title and self.soup.title.string:
            return self.soup.title.string.strip()
            
        return "Unknown Product"

    def extract_image(self) -> Optional[str]:
        # 1. Try OpenGraph
        og_image = self.soup.find("meta", property="og:image")
        if og_image and og_image.get("content"):
            return og_image["content"]
            
        return None

    def extract_price(self) -> Optional[str]:
        # Generic price extraction is difficult safely, return None for MVP
        # Future: look for schema.org Product price
        return None

    def extract_brand(self) -> Optional[str]:
        # Try OpenGraph site_name
        og_site = self.soup.find("meta", property="og:site_name")
        if og_site and og_site.get("content"):
            return og_site["content"].strip()
            
        # Try meta property site_name
        meta_site = self.soup.find("meta", attrs={"name": "site_name"})
        if meta_site and meta_site.get("content"):
            return meta_site["content"].strip()
        
        return None
