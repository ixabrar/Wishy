package com.wishvault.app.ui.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onSplashFinished: () -> Unit) {
    var startAnimation by remember { mutableStateOf(false) }
    var showTagline by remember { mutableStateOf(false) }
    var fadeOut by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100) // Quick pause
        startAnimation = true
        delay(400) // Logo breathes
        showTagline = true
        delay(1800) // Shorter hold before moving to foyer
        onSplashFinished()
    }

    // Logo Typography Animation
    val logoAlpha by animateFloatAsState(
        targetValue = if (fadeOut) 0f else if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000, easing = LinearOutSlowInEasing),
        label = "LogoAlpha"
    )
    val logoOffsetY by animateFloatAsState(
        targetValue = if (startAnimation) 0f else 24f,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "LogoOffset"
    )

    // Tagline Animation
    val taglineAlpha by animateFloatAsState(
        targetValue = if (showTagline) 1f else 0f,
        animationSpec = tween(durationMillis = 800, easing = LinearOutSlowInEasing),
        label = "TaglineAlpha"
    )
    val taglineOffsetY by animateFloatAsState(
        targetValue = if (showTagline) 0f else 16f,
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "TaglineOffset"
    )

    // Warm Ambient Glow
    val glowAlpha by animateFloatAsState(
        targetValue = if (fadeOut) 0f else if (startAnimation) 0.15f else 0f,
        animationSpec = tween(durationMillis = 3000, easing = LinearOutSlowInEasing),
        label = "GlowAlpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        // Soft Puce ambient glow
        Box(
            modifier = Modifier
                .size(350.dp)
                .alpha(glowAlpha)
                .blur(80.dp)
                .background(MaterialTheme.colorScheme.primary, shape = CircleShape)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Minimal Vault Emblem
            com.wishvault.app.ui.components.VaultEmblem(
                modifier = Modifier
                    .alpha(logoAlpha)
                    .offset(y = logoOffsetY.dp),
                alpha = logoAlpha
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            // Typography-First Logo
            Text(
                text = "WishVault",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .alpha(logoAlpha)
                    .offset(y = logoOffsetY.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Editorial Tagline
            Text(
                text = "A home for future desires.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .alpha(taglineAlpha)
                    .offset(y = taglineOffsetY.dp)
            )
        }
    }
}
