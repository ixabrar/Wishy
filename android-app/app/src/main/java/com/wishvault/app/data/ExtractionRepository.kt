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

data class ExtractRequest(val url: String)
data class ExtractedProduct(
    val title: String,
    val price: String,
    val brand: String,
    val store: String,
    val image: String,
    val category: String
)

class ExtractionRepository {
    private val client = OkHttpClient()
    private val gson = Gson()
    // Environment-aware backend URL
    private val baseUrl = Config.currentEnvironment.baseUrl

    suspend fun extractProduct(url: String): Result<ExtractedProduct> = withContext(Dispatchers.IO) {
        WishVaultLogger.i("Ingestion", "Extraction started for URL: $url")
        try {
            val reqBody = ExtractRequest(url)
            val jsonBody = gson.toJson(reqBody)
            
            val request = Request.Builder()
                .url("$baseUrl/extract")
                .post(jsonBody.toRequestBody("application/json; charset=utf-8".toMediaType()))
                .build()

            WishVaultLogger.i("Ingestion", "Dispatching POST request to: ${request.url}")

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    WishVaultLogger.e("Ingestion", "HTTP Request Failed. Code: ${response.code}")
                    return@withContext Result.failure(Exception("HTTP ${response.code}"))
                }
                
                val responseBody = response.body?.string() ?: ""
                WishVaultLogger.i("Ingestion", "HTTP Success. Raw response received: ${responseBody.take(100)}...")
                
                val product = gson.fromJson(responseBody, ExtractedProduct::class.java)
                WishVaultLogger.i("Ingestion", "JSON Parsing successful. Extracted title: ${product.title}")
                
                Result.success(product)
            }
        } catch (e: Exception) {
            WishVaultLogger.e("Ingestion", "Extraction failed entirely", e)
            Result.failure(e)
        }
    }
}
