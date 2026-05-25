package com.wishvault.app.ui.tracker

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun TrackerScreen(
    modifier: Modifier = Modifier
) {
    var isLoaded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(150)
        isLoaded = true
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Soft gradient orb for subtle visual warmth
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = (-100).dp)
                .size(300.dp)
                .blur(120.dp)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.03f), shape = CircleShape)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 120.dp) // Dock clearance
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 24.dp)
                ) {
                    Text(
                        text = "THE TRACKER",
                        style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 2.sp),
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                    )
                    
                    Spacer(modifier = Modifier.height(56.dp))
                    
                    Text(
                        text = "Quiet\nawareness.",
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }

            item {
                AnimatedVisibility(
                    visible = isLoaded,
                    enter = fadeIn(tween(1000)) + slideInVertically(tween(1000)) { 30 }
                ) {
                    Column(modifier = Modifier.padding(horizontal = 32.dp)) {
                        Text(
                            text = "NOTICED TODAY",
                            style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 1.sp),
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(bottom = 24.dp)
                        )
                        
                        // Editorial text block instead of a chart
                        Text(
                            text = "The Kyoto stay you saved feels quieter this season.",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                lineHeight = 40.sp,
                                letterSpacing = (-0.5).sp
                            ),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        
                        Spacer(modifier = Modifier.height(32.dp))
                        
                        // A quiet status indicator, completely avoiding green/red financial colors
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.5f), CircleShape)
                            )
                            Text(
                                text = "A gentle shift in availability",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                            )
                        }

                        Spacer(modifier = Modifier.height(80.dp))
                        
                        Text(
                            text = "Your Leica M11 collection is holding steady. No sudden movements in the market.",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                lineHeight = 40.sp,
                                letterSpacing = (-0.5).sp
                            ),
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
    }
}
