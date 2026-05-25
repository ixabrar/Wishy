package com.wishvault.app.ui.dashboard

import androidx.lifecycle.ViewModel
import com.wishvault.app.R
import com.wishvault.app.ui.components.WishItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
import java.util.UUID
import com.wishvault.app.util.WishVaultLogger

class VaultViewModel : ViewModel() {

    private val _savedItems = MutableStateFlow<List<WishItem>>(getInitialItems())
    val savedItems: StateFlow<List<WishItem>> = _savedItems.asStateFlow()

    private val repository = com.wishvault.app.data.ExtractionRepository()
    private val _isExtracting = MutableStateFlow(false)
    val isExtracting: StateFlow<Boolean> = _isExtracting.asStateFlow()

    suspend fun extractProductData(url: String): Result<com.wishvault.app.data.ExtractedProduct> {
        return repository.extractProduct(url)
    }

    fun extractAndAddWish(url: String) {
        WishVaultLogger.i("Ingestion", "VaultViewModel.extractAndAddWish triggered for Share Intent")
        viewModelScope.launch {
            _isExtracting.value = true
            val result = repository.extractProduct(url)
            result.onSuccess { product ->
                WishVaultLogger.i("Ingestion", "Product successfully loaded into VaultViewModel state")
                val newItem = WishItem(
                    id = UUID.randomUUID().toString(),
                    title = product.title,
                    brand = product.brand,
                    imageRes = R.drawable.img_leica, // Fallback
                    imageUrl = product.image,
                    heightRatio = listOf(0.8f, 1.0f, 1.2f, 1.5f).random()
                )
                val currentList = _savedItems.value.toMutableList()
                currentList.add(0, newItem)
                _savedItems.value = currentList
            }.onFailure { e ->
                WishVaultLogger.e("Ingestion", "Extraction failed in ViewModel, triggering graceful fallback save.", e)
                // Graceful fallback: save raw URL safely without breaking UI
                val fallbackItem = WishItem(
                    id = UUID.randomUUID().toString(),
                    title = "Unprocessed Link",
                    brand = java.net.URI(url).host ?: "Unknown Source",
                    imageRes = R.drawable.img_leica,
                    imageUrl = null, // Will attempt re-fetch in future architecture
                    heightRatio = 1.0f
                )
                val currentList = _savedItems.value.toMutableList()
                currentList.add(0, fallbackItem)
                _savedItems.value = currentList
            }
            _isExtracting.value = false
        }
    }

    fun addWish(title: String, brand: String, imageUrl: String? = null) {
        val newItem = WishItem(
            id = UUID.randomUUID().toString(),
            title = title,
            brand = brand,
            imageRes = R.drawable.img_leica, // Fallback
            imageUrl = imageUrl,
            heightRatio = listOf(0.8f, 1.0f, 1.2f, 1.5f).random()
        )
        
        val currentList = _savedItems.value.toMutableList()
        currentList.add(0, newItem) // Prepend to show immediately at the top
        _savedItems.value = currentList
    }

    private fun getInitialItems(): List<WishItem> {
        return listOf(
            WishItem("1", "Hand-thrown Matcha Bowl", "Studio Arhoj — Copenhagen", R.drawable.img_matcha, heightRatio = 1.2f),
            WishItem("2", "Leica M11 Rangefinder", "A lifetime companion.", R.drawable.img_leica, heightRatio = 0.8f),
            WishItem("3", "Vintage Silk Scarf", "Found in Paris. 1970s.", R.drawable.img_wardrobe, heightRatio = 1.5f),
            WishItem("4", "Kyoto Travel Guide", "Notes for the autumn trip.", R.drawable.img_kyoto, heightRatio = 1.0f),
            WishItem("5", "Walnut Desk Organizers", "Warmth for the studio.", R.drawable.img_interior, heightRatio = 1.2f)
        )
    }
}
