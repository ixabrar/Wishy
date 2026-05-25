package com.wishvault.app.ui.onboarding

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.wishvault.app.ui.components.PrimaryButton
import kotlinx.coroutines.delay

@Composable
fun OnboardingScreen(onNavigateToDashboard: () -> Unit) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(500) // Deep breath before showing onboarding
        isVisible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(32.dp)
    ) {
        androidx.compose.animation.AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(animationSpec = tween(1500)) + 
                    slideInVertically(
                        animationSpec = tween(1500),
                        initialOffsetY = { 30 }
                    ),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "A beautiful vault\nof future desires.",
                    style = MaterialTheme.typography.displayMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Text(
                    text = "Collect, curate, and organize your most meaningful aspirations in a soft, elegant space.",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                
                Spacer(modifier = Modifier.height(72.dp))
                
                PrimaryButton(
                    text = "Begin Collecting",
                    onClick = onNavigateToDashboard,
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
            }
        }
    }
}
