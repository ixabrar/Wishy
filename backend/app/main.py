from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.routes import extract, health
from app.config.settings import settings

app = FastAPI(
    title="WishVault Extraction API",
    description="Production ingestion pipeline for structured ecommerce metadata.",
    version="1.0.0"
)

# Hardened CORS Middleware for production safety
app.add_middleware(
    CORSMiddleware,
    allow_origins=settings.allowed_origins,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Register Routers
app.include_router(health.router)
app.include_router(extract.router)
