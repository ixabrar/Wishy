package com.wishvault.app.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.wishvault.app.ui.theme.AmbientShadow
import com.wishvault.app.ui.theme.FrostedIvory

@Composable
fun PremiumCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier
            .shadow(
                elevation = 24.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = AmbientShadow,
                ambientColor = AmbientShadow
            ),
        shape = RoundedCornerShape(24.dp),
        color = FrostedIvory,
        content = content
    )
}
