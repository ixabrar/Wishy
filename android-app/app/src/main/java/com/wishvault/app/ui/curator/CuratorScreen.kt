package com.wishvault.app.ui.curator

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wishvault.app.R
import com.wishvault.app.ui.components.IdentityAnchor
import com.wishvault.app.ui.components.PremiumCard
import kotlinx.coroutines.delay

@Composable
fun CuratorScreen(
    modifier: Modifier = Modifier,
    onIdentityClick: () -> Unit
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
        // Deep intelligent glow
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .offset(x = (-100).dp, y = 100.dp)
                .size(350.dp)
                .blur(140.dp)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.05f), shape = CircleShape)
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
                        .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "THE CURATOR",
                            style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 2.sp),
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                        )
                        IdentityAnchor(
                            onClick = onIdentityClick,
                            alpha = 0.3f // Quietest presence
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(56.dp))
                    
                    Text(
                        text = "Understood.",
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    
                    Spacer(modifier = Modifier.height(72.dp))
                }
            }

            item {
                AnimatedVisibility(
                    visible = isLoaded,
                    enter = fadeIn(tween(1000)) + slideInVertically(tween(1000)) { 50 }
                ) {
                    Column(modifier = Modifier.padding(horizontal = 32.dp)) {
                        Text(
                            text = "A PATTERN EMERGES",
                            style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 1.sp),
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.padding(bottom = 24.dp)
                        )
                        
                        Text(
                            text = "You've been collecting warm interiors and quiet travel spaces lately.",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                lineHeight = 44.sp,
                                letterSpacing = (-0.5).sp
                            ),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        
                        Spacer(modifier = Modifier.height(48.dp))

                        PremiumCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                Image(
                                    painter = painterResource(id = R.drawable.img_interior),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Black.copy(alpha = 0.2f))
                                )
                                Column(
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .padding(32.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Aesthetic Alignment",
                                        style = MaterialTheme.typography.labelMedium.copy(letterSpacing = 2.sp),
                                        color = Color.White.copy(alpha = 0.9f)
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "Explore spaces that match your recent saves.",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = Color.White,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
