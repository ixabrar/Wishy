package com.wishvault.app.ui.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.ui.unit.sp
import com.wishvault.app.R
import com.wishvault.app.ui.components.PremiumCard
import com.wishvault.app.ui.components.WishItem
import com.wishvault.app.ui.components.WishlistGrid
import kotlinx.coroutines.delay

data class CollectionItem(val title: String, val imageRes: Int)

@Composable
fun DashboardScreen(
    viewModel: VaultViewModel,
    paddingValues: PaddingValues,
    onIdentityClick: () -> Unit,
    onItemClick: (WishItem) -> Unit = {}
) {
    var isLoaded by remember { mutableStateOf(false) }
    val savedItems by viewModel.savedItems.collectAsState()
    val isExtracting by viewModel.isExtracting.collectAsState()

    LaunchedEffect(Unit) {
        delay(100)
        isLoaded = true
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Atmospheric Layered Depth
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 100.dp, y = (-50).dp)
                .size(400.dp)
                .blur(120.dp)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.05f), shape = CircleShape)
        )
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(x = (-100).dp, y = 300.dp)
                .size(350.dp)
                .blur(140.dp)
                .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.04f), shape = CircleShape)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                // We use contentPadding to push the content under the system bars seamlessly
                .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            // 0. EDITORIAL HEADER SYSTEM
            item {
                AnimatedVisibility(
                    visible = isLoaded,
                    enter = fadeIn(tween(600))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp, vertical = 24.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "WISHVAULT — Archive 01",
                            style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 2.sp),
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                        )
                        
                        com.wishvault.app.ui.components.IdentityAnchor(
                            onClick = onIdentityClick,
                            alpha = 1.0f // Strongest presence
                        )
                    }
                }
            }

            // 1. SPACIOUS EDITORIAL MOMENT: The Hero
            item {
                AnimatedVisibility(
                    visible = isLoaded,
                    enter = fadeIn(tween(800, delayMillis = 100)) + slideInVertically(tween(800, delayMillis = 100)) { 50 }
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(48.dp))
                        Text(
                            text = "Your future desires,\ncurated.",
                            style = MaterialTheme.typography.displayMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(horizontal = 32.dp),
                            textAlign = TextAlign.Start
                        )
                        Spacer(modifier = Modifier.height(56.dp))
                    }
                }
            }

            // 2. DENSER CURATION: Collections
            item {
                AnimatedVisibility(
                    visible = isLoaded,
                    enter = fadeIn(tween(800, delayMillis = 300)) + slideInVertically(tween(800, delayMillis = 300)) { 50 }
                ) {
                    Column {
                        Text(
                            text = "COLLECTIONS",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp)
                        )
                        val collections = listOf(
                            CollectionItem("The Kyoto Winter Edit", R.drawable.img_kyoto),
                            CollectionItem("Quiet Morning Rituals", R.drawable.img_matcha),
                            CollectionItem("Tactile Studio Artifacts", R.drawable.img_interior),
                            CollectionItem("Autumn Wool & Cashmere", R.drawable.img_wardrobe)
                        )
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(24.dp),
                            contentPadding = PaddingValues(horizontal = 32.dp)
                        ) {
                            itemsIndexed(collections) { _, collection ->
                                CollectionCard(title = collection.title, imageRes = collection.imageRes)
                            }
                        }
                        Spacer(modifier = Modifier.height(72.dp))
                    }
                }
            }

            // 3. SPACIOUS EDITORIAL MOMENT: AI Curator
            item {
                AnimatedVisibility(
                    visible = isLoaded,
                    enter = fadeIn(tween(800, delayMillis = 500)) + slideInVertically(tween(800, delayMillis = 500)) { 50 }
                ) {
                    Column {
                        PremiumCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 32.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp, vertical = 32.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "✨ A Gentle Nudge",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(32.dp))
                                Text(
                                    text = "You've saved beautiful pieces for your studio. Would you like to group them into a dedicated space?",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        lineHeight = 32.sp,
                                        fontSize = 18.sp
                                    ),
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(72.dp))
                    }
                }
            }

            // 4. DENSER CURATION: Editorial Grid
            item {
                AnimatedVisibility(
                    visible = isLoaded,
                    enter = fadeIn(tween(800, delayMillis = 700)) + slideInVertically(tween(800, delayMillis = 700)) { 50 }
                ) {
                    Column {
                        Text(
                            text = "RECENTLY SAVED",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1200.dp) 
                        ) {
                            WishlistGrid(
                                items = savedItems,
                                onItemClick = onItemClick
                            )
                        }
                    }
                }
            }
        }

        // Loading Overlay
        if (isExtracting) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f))
                    .clickable(
                        interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                        indication = null
                    ) { },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    androidx.compose.material3.CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Analyzing item...",
                        color = Color.White,
                        style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 1.sp)
                    )
                }
            }
        }
    }
}

@Composable
fun CollectionCard(title: String, imageRes: Int) {
    PremiumCard(
        modifier = Modifier
            .width(300.dp)
            .height(380.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            // Advanced Multi-Layer Gradient
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.1f),
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.85f)
                            ),
                            startY = 0f
                        )
                    )
            )
            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(horizontal = 32.dp, vertical = 40.dp)
            )
        }
    }
}
