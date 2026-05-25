from fastapi import APIRouter, HTTPException
from app.models.schemas import ExtractRequest, ExtractResponse
from app.services.extractor import ExtractionService

router = APIRouter()

@router.post("/extract", response_model=ExtractResponse)
async def extract_product(request: ExtractRequest):
    try:
        url_str = str(request.url)
        # Delegate to the extraction service
        product_data = await ExtractionService.extract(url_str)
        return product_data
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
