from playwright.async_api import async_playwright

class PlaywrightEngine:
    @staticmethod
    async def render_page(url: str) -> str:
        """
        Launches a headless browser, navigates to the URL, and waits for network idling
        to ensure JavaScript frameworks (React/Next.js) fully hydrate the DOM.
        """
        try:
            async with async_playwright() as p:
                browser = await p.chromium.launch(headless=True)
                page = await browser.new_page(
                    user_agent="Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
                    viewport={"width": 1280, "height": 800}
                )
                
                # Wait until network is mostly idle to ensure JS hydration finishes
                await page.goto(url, wait_until="networkidle", timeout=15000)
                html_content = await page.content()
                
                await browser.close()
                return html_content
        except Exception as e:
            print(f"Playwright Engine Failed: {e}")
            return ""
