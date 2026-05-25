package com.wishvault.app.ui.identity

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.activity.compose.BackHandler
import kotlinx.coroutines.delay

@Composable
fun IdentityOverlay(
    isVisible: Boolean,
    onClose: () -> Unit,
    onDeveloperModeClick: () -> Unit
) {
    BackHandler(enabled = isVisible) {
        onClose()
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(tween(600)),
        exit = fadeOut(tween(400))
    ) {
        var isLoaded by remember { mutableStateOf(false) }

        LaunchedEffect(isVisible) {
            if (isVisible) {
                delay(200)
                isLoaded = true
            } else {
                isLoaded = false
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.98f))
                .clickable(interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }, indication = null) { } // Consume clicks
        ) {
        // Aesthetic depth lighting
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 50.dp, y = (-50).dp)
                .size(350.dp)
                .blur(140.dp)
                .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.04f), shape = CircleShape)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Close Button
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                        IconButton(onClick = onClose) {
                            Icon(
                                imageVector = Icons.Rounded.Close,
                                contentDescription = "Close",
                                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Soft Monogram Avatar
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "A.",
                            style = MaterialTheme.typography.displayLarge.copy(fontSize = 48.sp),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    Spacer(modifier = Modifier.height(48.dp))

                    Text(
                        text = "THE IDENTITY",
                        style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 2.sp),
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                    )
                    
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }

            item {
                AnimatedVisibility(
                    visible = isLoaded,
                    enter = fadeIn(tween(1200)) + slideInVertically(tween(1200)) { 20 }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Your aesthetic profile and saved items.",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                lineHeight = 44.sp,
                                letterSpacing = (-0.5).sp
                            ),
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center
                        )
                        
                        Spacer(modifier = Modifier.height(64.dp))
                        
                        // Premium Utility Statistics
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            EmotionalSignal(
                                title = "32 Saved Items",
                                subtitle = "Across 5 Collections"
                            )
                            
                            EmotionalSignal(
                                title = "Dominant Category",
                                subtitle = "Interiors & Objects"
                            )
                            
                            EmotionalSignal(
                                title = "Recently Tracked",
                                subtitle = "Prices holding steady"
                            )

                            Spacer(modifier = Modifier.height(48.dp))

                            Text(
                                text = "Developer Mode",
                                style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 1.sp),
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),
                                modifier = Modifier
                                    .clickable { onDeveloperModeClick() }
                                    .padding(8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
    }
}

@Composable
fun EmotionalSignal(title: String, subtitle: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(letterSpacing = 0.5.sp),
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center
        )
    }
}
