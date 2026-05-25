package com.wishvault.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.PersonOutline
import androidx.compose.material.icons.rounded.ShowChart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.wishvault.app.ui.theme.AmbientShadow
import com.wishvault.app.ui.theme.FrostedIvory

class DockShape : Shape {
    override fun createOutline(size: androidx.compose.ui.geometry.Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val corner = with(density) { 24.dp.toPx() }
        val cx = size.width / 2f
        // The AddButton is centered at parentHeight - 64.dp.
        val cy = size.height - with(density) { 64.dp.toPx() }
        
        val buttonRadius = with(density) { 32.dp.toPx() }
        val halo = with(density) { 6.dp.toPx() } // reduced halo for a tighter fit
        val cutoutR = buttonRadius + halo
        
        val swoopWidth = with(density) { 28.dp.toPx() } // generous width for a very soft, Apple-style swoop
        val startX = cx - cutoutR - swoopWidth
        val endX = cx + cutoutR + swoopWidth
        val bottomY = cy + cutoutR // Lowest point of the pocket
        
        val path = Path().apply {
            moveTo(corner, 0f)
            lineTo(startX, 0f)
            
            // Left continuous soft swoop
            cubicTo(
                startX + swoopWidth * 0.75f, 0f, // CP1: horizontally flat to maintain straight edge smoothness
                cx - cutoutR, bottomY,           // CP2: pull down into the deep pocket
                cx, bottomY                      // End: exact bottom center
            )
            
            // Right continuous soft swoop
            cubicTo(
                cx + cutoutR, bottomY,           // CP1: pull up identically
                endX - swoopWidth * 0.75f, 0f,   // CP2: horizontally flat before top edge
                endX, 0f                         // End: top edge
            )
            
            lineTo(size.width - corner, 0f)
            arcTo(Rect(size.width - 2 * corner, 0f, size.width, 2 * corner), -90f, 90f, false)
            lineTo(size.width, size.height - corner)
            arcTo(Rect(size.width - 2 * corner, size.height - 2 * corner, size.width, size.height), 0f, 90f, false)
            lineTo(corner, size.height)
            arcTo(Rect(0f, size.height - 2 * corner, 2 * corner, size.height), 90f, 90f, false)
            lineTo(0f, corner)
            arcTo(Rect(0f, 0f, 2 * corner, 2 * corner), 180f, 90f, false)
            close()
        }
        
        return Outline.Generic(path)
    }
}

@Composable
fun FloatingNavigationBar(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 48.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        val dockShape = DockShape()
        
        // The Glassmorphic Dock
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 24.dp,
                    shape = dockShape, // Use the custom cutout shape for shadows!
                    spotColor = AmbientShadow.copy(alpha = 0.5f),
                    ambientColor = AmbientShadow.copy(alpha = 0.1f)
                )
                .clip(dockShape) // Clip background strictly to the cutout
                .background(FrostedIvory.copy(alpha = 0.95f))
                .padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavItem(
                label = "Vault", 
                icon = Icons.Rounded.Home, 
                isSelected = currentRoute == "vault", 
                onClick = { onNavigate("vault") },
                modifier = Modifier.weight(1f)
            )
            NavItem(
                label = "Archives", 
                icon = Icons.Rounded.Folder, 
                isSelected = currentRoute == "archives", 
                onClick = { onNavigate("archives") },
                modifier = Modifier.weight(1f)
            )
            
            // Center Placeholder for Add Button
            Spacer(modifier = Modifier.weight(1f))
            
            NavItem(
                label = "Curator", 
                icon = Icons.Rounded.AutoAwesome, 
                isSelected = currentRoute == "curator", 
                onClick = { onNavigate("curator") },
                modifier = Modifier.weight(1f)
            )
            // Tracker button replaces the spacer to fill the blank space symmetrically
            NavItem(
                label = "Tracker", 
                icon = Icons.Rounded.ShowChart, 
                isSelected = currentRoute == "tracker", 
                onClick = { onNavigate("tracker") },
                modifier = Modifier.weight(1f)
            )
        }
        
        // The Protruding Sculpted Add Button
        Box(
            modifier = Modifier.offset(y = (-32).dp), // Elevated strictly above the dock
            contentAlignment = Alignment.Center
        ) {
            // The solid Antiquewhite mask is removed!
            // The DockShape now natively punches a completely transparent hole through the glass.
            AddButton(onClick = onAddClick)
        }
    }
}

@Composable
fun IdentityAnchor(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    alpha: Float = 1f
) {
    val haptic = LocalHapticFeedback.current
    Box(
        modifier = modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f * alpha))
            .clickable { 
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onClick() 
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.PersonOutline,
            contentDescription = "Identity",
            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f * alpha),
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun NavItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    
    val color = if (isSelected) MaterialTheme.colorScheme.primary 
                else MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f)

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { 
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onClick() 
            }
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
            ),
            color = color
        )
    }
}

@Composable
fun AddButton(onClick: () -> Unit) {
    val haptic = LocalHapticFeedback.current
    Box(
        modifier = Modifier
            .size(64.dp)
            .shadow(
                elevation = 20.dp, 
                shape = CircleShape, 
                spotColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f), // Stronger Puce glow
                ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
            )
            .background(MaterialTheme.colorScheme.primary, shape = CircleShape)
            .clickable {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.Add,
            contentDescription = "Add",
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.size(32.dp)
        )
    }
}
