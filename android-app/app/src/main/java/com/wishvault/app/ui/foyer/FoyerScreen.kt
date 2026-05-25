package com.wishvault.app.ui.foyer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun FoyerScreen(onEnterVault: () -> Unit) {
    var step by remember { mutableStateOf(0) }
    val haptic = LocalHapticFeedback.current

    LaunchedEffect(Unit) {
        delay(200) // Deep breath before showing welcome
        step = 1 
        delay(1200) // Let the typography breathe
        step = 2 
        delay(800) // Wait before showing the button
        step = 3
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. The Identity Mark
            AnimatedVisibility(
                visible = step >= 1,
                enter = fadeIn(tween(1500))
            ) {
                com.wishvault.app.ui.components.VaultEmblem(modifier = Modifier.padding(bottom = 24.dp))
            }

            AnimatedVisibility(
                visible = step >= 1,
                enter = fadeIn(tween(1500)) + slideInVertically(tween(1500)) { 20 }
            ) {
                Text(
                    text = "A quiet place for your future.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))

            AnimatedVisibility(
                visible = step >= 2,
                enter = fadeIn(tween(1500)) + slideInVertically(tween(1500)) { 20 }
            ) {
                Text(
                    text = "WishVault",
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            
            Spacer(modifier = Modifier.height(48.dp))

            // 3. The Anonymous Entry
            AnimatedVisibility(
                visible = step >= 3,
                enter = fadeIn(tween(1000))
            ) {
                Button(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onEnterVault()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Enter the Vault",
                        style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 2.sp)
                    )
                }
            }
        }
    }
}
