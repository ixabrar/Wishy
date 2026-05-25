import json
from app.parsers.base import BaseParser
from typing import Optional, Any

class UniversalParser(BaseParser):
    def __init__(self, soup, url):
        super().__init__(soup, url)
        self.ld_data = self._extract_json_ld()

    def can_parse(self) -> bool:
        return True

    def _extract_json_ld(self) -> Optional[dict]:
        """Scans all application/ld+json scripts for a Product schema."""
        scripts = self.soup.find_all("script", type="application/ld+json")
        for script in scripts:
            try:
                if not script.string: continue
                data = json.loads(script.string)
                product = self._find_product_in_ld(data)
                if product: return product
            except json.JSONDecodeError:
                continue
        return None

    def _find_product_in_ld(self, data: Any) -> Optional[dict]:
        """Recursively search for @type == Product inside JSON-LD data structures (@graph or arrays)."""
        if isinstance(data, dict):
            type_val = data.get("@type", "")
            if type_val == "Product" or (isinstance(type_val, list) and "Product" in type_val):
                return data
            # Check @graph array
            if "@graph" in data:
                return self._find_product_in_ld(data["@graph"])
            # Check values recursively
            for key, val in data.items():
                if isinstance(val, (dict, list)):
                    found = self._find_product_in_ld(val)
                    if found: return found
        elif isinstance(data, list):
            for item in data:
                found = self._find_product_in_ld(item)
                if found: return found
        return None

    def extract_title(self) -> str:
        # 1. JSON-LD
        if self.ld_data and self.ld_data.get("name"):
            return str(self.ld_data.get("name")).strip()
            
        # 2. OpenGraph
        og_title = self.soup.find("meta", property="og:title")
        if og_title and og_title.get("content"):
            return og_title["content"].strip()
            
        # 3. Meta Title
        meta_title = self.soup.find("meta", attrs={"name": "title"})
        if meta_title and meta_title.get("content"):
            return meta_title["content"].strip()
            
        # 4. Native Title
        if self.soup.title and self.soup.title.string:
            return self.soup.title.string.strip()
            
        return "Unknown Product"

    def extract_image(self) -> Optional[str]:
        # 1. JSON-LD
        if self.ld_data and self.ld_data.get("image"):
            img = self.ld_data.get("image")
            if isinstance(img, str): return img
            if isinstance(img, list) and len(img) > 0: return img[0]
            if isinstance(img, dict) and img.get("url"): return img.get("url")
            
        # 2. OpenGraph
        og_image = self.soup.find("meta", property="og:image")
        if og_image and og_image.get("content"):
            return og_image["content"]
            
        return None

    def extract_price(self) -> Optional[str]:
        # 1. JSON-LD Normalization Layer
        if self.ld_data:
            offers = self.ld_data.get("offers", {})
            if isinstance(offers, list) and len(offers) > 0:
                offers = offers[0]
                
            if isinstance(offers, dict):
                price = offers.get("price") or offers.get("lowPrice")
                currency = offers.get("priceCurrency", "")
                if price:
                    return f"{price} {currency}".strip()
                    
        return None

    def extract_brand(self) -> Optional[str]:
        # 1. JSON-LD
        if self.ld_data and self.ld_data.get("brand"):
            brand = self.ld_data.get("brand")
            if isinstance(brand, str): return brand
            if isinstance(brand, dict) and brand.get("name"): return brand.get("name")

        # 2. OpenGraph site_name
        og_site = self.soup.find("meta", property="og:site_name")
        if og_site and og_site.get("content"):
            return og_site["content"].strip()
            
        return None

    def extract_canonical_url(self) -> str:
        # 1. Canonical Link
        canonical = self.soup.find("link", rel="canonical")
        if canonical and canonical.get("href"):
            return canonical["href"]
            
        # 2. OpenGraph URL
        og_url = self.soup.find("meta", property="og:url")
        if og_url and og_url.get("content"):
            return og_url["content"]
            
        return self.url
