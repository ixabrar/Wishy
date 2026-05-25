from pydantic import BaseModel, HttpUrl
from typing import Optional

class ExtractRequest(BaseModel):
    url: HttpUrl

class ExtractResponse(BaseModel):
    title: str
    image: Optional[str] = None
    price: Optional[str] = None
    brand: Optional[str] = None
    store: Optional[str] = None
    success: bool = True
