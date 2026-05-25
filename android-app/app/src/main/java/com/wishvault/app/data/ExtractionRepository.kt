package com.wishvault.app.data

import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import com.wishvault.app.util.Config
import com.wishvault.app.util.WishVaultLogger
import java.util.concurrent.TimeUnit

data class ExtractRequest(val url: String)
data class ExtractedProduct(
    val title: String,
    val price: String?,
    val brand: String,
    val store: String,
    val image: String?,
    val resolved_url: String?
)

class ExtractionRepository {
    private val gson = Gson()
    
    // Explicit independent timeouts for cold-starts vs extraction
    private val healthClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

    private val extractClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    // Environment-aware backend URL
    private val baseUrl = Config.currentEnvironment.baseUrl
    
    private var lastHealthCheckTime = 0L
    private val CACHE_DURATION_MS = 60_000L // 60 seconds

    suspend fun checkHealth(): Boolean = withContext(Dispatchers.IO) {
        if (System.currentTimeMillis() - lastHealthCheckTime < CACHE_DURATION_MS) {
            WishVaultLogger.i("Health", "Using cached healthy status")
            return@withContext true
        }

        WishVaultLogger.i("Health", "Pinging /health to ensure server is awake...")
        WishVaultLogger.updateBackendStatus("Waking...")
        try {
            val request = Request.Builder()
                .url("$baseUrl/health")
                .get()
                .build()

            healthClient.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    WishVaultLogger.i("Health", "Server is Online.")
                    WishVaultLogger.updateBackendStatus("Online")
                    lastHealthCheckTime = System.currentTimeMillis()
                    true
                } else {
                    WishVaultLogger.e("Health", "Server returned HTTP ${response.code}")
                    WishVaultLogger.updateBackendStatus("Unreachable")
                    false
                }
            }
        } catch (e: Exception) {
            WishVaultLogger.e("Health", "Failed to reach server. It may be offline or timing out.", e)
            WishVaultLogger.updateBackendStatus("Unreachable")
            false
        }
    }

    suspend fun extractProduct(url: String): Result<ExtractedProduct> = withContext(Dispatchers.IO) {
        // Pre-flight check
        if (!checkHealth()) {
            WishVaultLogger.i("Health", "Backend health check failed or timed out, but attempting extraction anyway...")
        }

        WishVaultLogger.i("Ingestion", "Extraction started for URL: $url")
        try {
            val reqBody = ExtractRequest(url)
            val jsonBody = gson.toJson(reqBody)
            
            val request = Request.Builder()
                .url("$baseUrl/extract")
                .post(jsonBody.toRequestBody("application/json; charset=utf-8".toMediaType()))
                .build()

            WishVaultLogger.i("Ingestion", "Dispatching POST request to: ${request.url}")

            extractClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    WishVaultLogger.e("Ingestion", "HTTP Request Failed. Code: ${response.code}")
                    return@withContext Result.failure(Exception("HTTP ${response.code}"))
                }
                
                val responseBody = response.body?.string() ?: ""
                WishVaultLogger.i("Ingestion", "HTTP Success. Raw response received: ${responseBody.take(100)}...")
                
                val product = gson.fromJson(responseBody, ExtractedProduct::class.java)
                WishVaultLogger.i("Ingestion", "JSON Parsing successful. Extracted title: ${product.title}")
                
                if (product.resolved_url != null && product.resolved_url != url) {
                    WishVaultLogger.i("Redirect", "URL Resolved: $url -> ${product.resolved_url}")
                }
                
                Result.success(product)
            }
        } catch (e: Exception) {
            WishVaultLogger.e("Ingestion", "Extraction failed entirely", e)
            Result.failure(e)
        }
    }
}
