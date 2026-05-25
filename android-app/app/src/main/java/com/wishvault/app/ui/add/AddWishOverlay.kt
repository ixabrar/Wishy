package com.wishvault.app.ui.add

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.util.Patterns
import com.wishvault.app.data.ExtractedProduct
import com.wishvault.app.util.WishVaultLogger

@Composable
fun AddWishOverlay(
    isVisible: Boolean,
    onClose: () -> Unit,
    onSave: (title: String, brand: String, imageUrl: String?) -> Unit,
    onExtract: suspend (String) -> Result<ExtractedProduct>
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(tween(800)) + slideInVertically(tween(800), initialOffsetY = { it / 4 }),
        exit = fadeOut(tween(600)) + slideOutVertically(tween(600), targetOffsetY = { it / 4 })
    ) {
        val haptic = LocalHapticFeedback.current
        val coroutineScope = rememberCoroutineScope()
        
        var input by remember { mutableStateOf("") }
        var title by remember { mutableStateOf("") }
        var brand by remember { mutableStateOf("") }
        var imageUrl by remember { mutableStateOf<String?>(null) }
        
        var isFetching by remember { mutableStateOf(false) }
        var fetchError by remember { mutableStateOf<String?>(null) }
        var loadingText by remember { mutableStateOf("Fetching product...") }

        val focusRequester = remember { FocusRequester() }

        LaunchedEffect(isFetching) {
            if (isFetching) {
                loadingText = "Fetching product..."
                delay(4000)
                loadingText = "Waking up server..."
            }
        }

        val isUrl = Patterns.WEB_URL.matcher(input).matches()

        // When the overlay becomes visible, request focus after a short delay
        LaunchedEffect(isVisible) {
            if (isVisible) {
                delay(400)
                focusRequester.requestFocus()
            } else {
                input = ""
                title = ""
                brand = ""
                imageUrl = null
                fetchError = null
                isFetching = false
            }
        }

        // Frosted background container
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.85f))
        ) {
            // Close Action
            Text(
                text = "Close",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 48.dp, end = 32.dp)
                    .clickable { 
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onClose() 
                    }
                    .padding(8.dp) // touch target
            )

            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(horizontal = 32.dp),
                verticalArrangement = Arrangement.spacedBy(32.dp)
            ) {
                if (isFetching) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        androidx.compose.material3.CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = loadingText,
                            style = MaterialTheme.typography.bodyMedium.copy(letterSpacing = 1.sp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                } else {
                    Text(
                        text = "What caught\nyour eye?",
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    if (fetchError != null) {
                        Text(
                            text = fetchError!!,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    // Main Input (URL or Title)
                    BasicTextField(
                        value = if (title.isNotEmpty()) title else input,
                        onValueChange = { 
                            if (title.isNotEmpty()) title = it else input = it
                            fetchError = null
                        },
                        textStyle = MaterialTheme.typography.headlineLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        modifier = Modifier.focusRequester(focusRequester),
                        decorationBox = { innerTextField ->
                            if (input.isEmpty() && title.isEmpty()) {
                                Text(
                                    text = "Paste a URL or type...",
                                    style = MaterialTheme.typography.headlineLarge,
                                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f)
                                )
                            }
                            innerTextField()
                        }
                    )

                    // Secondary Input (Brand/Source)
                    if (title.isNotEmpty() || !isUrl) {
                        BasicTextField(
                            value = brand,
                            onValueChange = { brand = it },
                            textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                            decorationBox = { innerTextField ->
                                if (brand.isEmpty()) {
                                    Text(
                                        text = "Designer or Source",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f)
                                    )
                                }
                                innerTextField()
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Actions
                    if (isUrl && title.isEmpty()) {
                        Text(
                            text = "EXTRACT PRODUCT",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .clickable {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    WishVaultLogger.i("Ingestion", "Manual Extract tapped for URL: $input")
                                    isFetching = true
                                    fetchError = null
                                    coroutineScope.launch {
                                        val result = onExtract(input)
                                        result.onSuccess { product ->
                                            WishVaultLogger.i("Ingestion", "AddWishOverlay extraction successful")
                                            title = product.title
                                            brand = product.brand
                                            imageUrl = product.image
                                        }.onFailure { e ->
                                            WishVaultLogger.e("Ingestion", "AddWishOverlay extraction failed", e)
                                            fetchError = "Couldn’t fully read this product yet."
                                        }
                                        isFetching = false
                                    }
                                }
                                .padding(vertical = 8.dp)
                        )
                    } else if (input.isNotEmpty() || title.isNotEmpty()) {
                        Text(
                            text = "SAVE TO VAULT",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .clickable {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    // Graceful Fallback if URL failed or just standard saving
                                    if (isUrl && fetchError != null) {
                                        WishVaultLogger.i("Ingestion", "Saving fallback item for failed URL")
                                        val domain = try { java.net.URI(input).host } catch(e: Exception) { "Unknown" }
                                        onSave("Unprocessed Link", domain ?: "Unknown Source", null)
                                    } else {
                                        val finalTitle = if (title.isNotEmpty()) title else input
                                        WishVaultLogger.i("Ingestion", "Saving standard item: $finalTitle")
                                        onSave(finalTitle, if (brand.isEmpty()) "Unknown" else brand, imageUrl)
                                    }
                                    onClose()
                                }
                                .padding(vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}
