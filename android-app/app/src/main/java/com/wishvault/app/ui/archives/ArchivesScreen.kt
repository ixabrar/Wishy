package com.wishvault.app.ui.archives

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wishvault.app.R
import com.wishvault.app.ui.components.IdentityAnchor
import com.wishvault.app.ui.components.PremiumCard

data class ArchiveCollection(val title: String, val subtitle: String, val imageRes: Int)

@Composable
fun ArchivesScreen(
    modifier: Modifier = Modifier,
    onIdentityClick: () -> Unit
) {
    val archives = listOf(
        ArchiveCollection("Kyoto Evenings", "Preserved thoughts from the east.", R.drawable.img_kyoto),
        ArchiveCollection("Warm Interiors", "Selected artifacts for the home.", R.drawable.img_interior),
        ArchiveCollection("Quiet Cafes", "Spaces for reflection.", R.drawable.img_interior),
        ArchiveCollection("Autumn Wardrobe", "Textures and layers.", R.drawable.img_wardrobe),
        ArchiveCollection("Studio Corners", "Creative foundations.", R.drawable.img_interior)
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "THE ARCHIVES",
                            style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 2.sp),
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                        )
                        IdentityAnchor(
                            onClick = onIdentityClick,
                            alpha = 0.6f // Softer presence in the Archives
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(56.dp))
                    
                    Text(
                        text = "Preserved\nthoughts.",
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    
                    Spacer(modifier = Modifier.height(72.dp))
                }
            }

            items(archives) { archive ->
                CinematicArchiveCover(archive)
                Spacer(modifier = Modifier.height(120.dp)) // Calmer, slower scrolling rhythm
            }
        }
    }
}

@Composable
fun CinematicArchiveCover(archive: ArchiveCollection) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp), // Near edge-to-edge
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PremiumCard(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.75f) // Portrait magazine feel
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = archive.imageRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Soft gradient for text readability
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                                startY = 300f
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(32.dp)
                ) {
                    Text(
                        text = archive.title,
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = archive.subtitle,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}
