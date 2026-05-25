package com.wishvault.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.wishvault.app.R

@Composable
fun VaultEmblem(modifier: Modifier = Modifier, alpha: Float = 1f) {
    Image(
        painter = painterResource(id = R.drawable.vault_logo),
        contentDescription = "Vault Emblem",
        modifier = modifier
            .size(120.dp)
            .alpha(alpha)
    )
}
