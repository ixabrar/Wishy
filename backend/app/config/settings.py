import os
from pydantic_settings import BaseSettings

class Settings(BaseSettings):
    environment: str = os.getenv("ENVIRONMENT", "DEV_LOCAL")
    allowed_origins: list[str] = [
        "http://localhost",
        "http://localhost:8000",
        "https://api.wishvault.app"
    ]
    request_timeout: int = 5 # seconds
    
    class Config:
        env_file = ".env"

settings = Settings()
