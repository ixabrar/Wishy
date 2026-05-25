package com.wishvault.app.ui.detail

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wishvault.app.ui.components.WishItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WishDetailOverlay(
    item: WishItem?,
    isVisible: Boolean,
    onClose: () -> Unit
) {
    BackHandler(enabled = isVisible) {
        onClose()
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(tween(500)) { it } + fadeIn(tween(500)),
        exit = slideOutVertically(tween(400)) { it } + fadeOut(tween(400))
    ) {
        if (item == null) return@AnimatedVisibility

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .clickable(interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }, indication = null) { }
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 120.dp)
            ) {
                // Top Area: Immersive Image Carousel
                item {
                    Box(modifier = Modifier.fillMaxWidth().height(450.dp)) {
                        val pagerState = rememberPagerState(pageCount = { 3 })
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier.fillMaxSize()
                        ) { _ ->
                            if (item.imageUrl != null && item.imageUrl.isNotEmpty()) {
                                coil.compose.AsyncImage(
                                    model = item.imageUrl,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Image(
                                    painter = painterResource(id = item.imageRes),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                        
                        // Close Button overlaid on image
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 16.dp)
                                .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.TopEnd
                        ) {
                            IconButton(
                                onClick = onClose,
                                modifier = Modifier
                                    .background(Color.Black.copy(alpha = 0.3f), CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Close,
                                    contentDescription = "Close",
                                    tint = Color.White
                                )
                            }
                        }

                        // Pager Dots
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 16.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            repeat(3) { iteration ->
                                val color = if (pagerState.currentPage == iteration) Color.White else Color.White.copy(alpha = 0.5f)
                                Box(
                                    modifier = Modifier
                                        .padding(2.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                        .size(6.dp)
                                )
                            }
                        }
                    }
                }

                // Middle Area: Strong Information Hierarchy
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .padding(top = 32.dp)
                    ) {
                        Text(
                            text = item.brand.uppercase(),
                            style = MaterialTheme.typography.labelMedium.copy(letterSpacing = 2.sp),
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.displayMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "$3,495", // Mock price
                                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Tracked Price",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                    }
                }

                // Lower Action Area
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .padding(top = 48.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = { /* Open Store */ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.onBackground,
                                contentColor = MaterialTheme.colorScheme.background
                            ),
                            shape = CircleShape
                        ) {
                            Text("Open Store", style = MaterialTheme.typography.labelLarge)
                        }

                        Button(
                            onClick = { /* Add to Collection */ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.onBackground
                            ),
                            shape = CircleShape
                        ) {
                            Text("Add To Collection", style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }

                // Extras: Similar Finds Placeholder
                item {
                    Spacer(modifier = Modifier.height(64.dp))
                    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                        Text(
                            text = "SIMILAR ITEMS",
                            style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 1.sp),
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        // Placeholder for similar items horizontal row
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Similar products appear here", color = MaterialTheme.colorScheme.secondary)
                        }
                    }
                }
            }
        }
    }
}
