package com.wishvault.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.wishvault.app.ui.add.AddWishOverlay
import com.wishvault.app.ui.archives.ArchivesScreen
import com.wishvault.app.ui.components.FloatingNavigationBar
import com.wishvault.app.ui.curator.CuratorScreen
import com.wishvault.app.ui.dashboard.DashboardScreen
import com.wishvault.app.ui.dashboard.VaultViewModel
import com.wishvault.app.ui.foyer.FoyerScreen
import com.wishvault.app.ui.identity.IdentityOverlay
import com.wishvault.app.ui.detail.WishDetailOverlay
import com.wishvault.app.ui.components.WishItem
import com.wishvault.app.ui.developer.DeveloperLogsOverlay
import com.wishvault.app.ui.splash.SplashScreen
import com.wishvault.app.ui.tracker.TrackerScreen
import com.wishvault.app.ui.theme.WishVaultTheme
import com.wishvault.app.util.WishVaultLogger

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Edge-to-Edge Immersion
        
        var sharedUrl: String? = null
        if (intent?.action == android.content.Intent.ACTION_SEND && intent.type == "text/plain") {
            sharedUrl = intent.getStringExtra(android.content.Intent.EXTRA_TEXT)
            WishVaultLogger.i("Ingestion", "ACTION_SEND intent received with URL: $sharedUrl")
        }

        setContent {
            WishVaultTheme {
                WishVaultApp(initialSharedUrl = sharedUrl)
            }
        }
    }
}

@Composable
fun WishVaultApp(initialSharedUrl: String? = null) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "splash"
    
    // Global State
    val vaultViewModel: VaultViewModel = viewModel()
    var showAddOverlay by remember { mutableStateOf(false) }
    var showIdentityOverlay by remember { mutableStateOf(false) }
    var showDeveloperLogs by remember { mutableStateOf(false) }
    var selectedWishItem by remember { mutableStateOf<WishItem?>(null) }
    
    // Handle Shared URL
    LaunchedEffect(initialSharedUrl) {
        if (!initialSharedUrl.isNullOrEmpty()) {
            vaultViewModel.extractAndAddWish(initialSharedUrl)
        }
    }

    // If we're not in splash or foyer, show BottomNav
    val showBottomNav = currentRoute != "splash" && currentRoute != "foyer"

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            bottomBar = {
                if (showBottomNav) {
                    FloatingNavigationBar(
                        currentRoute = currentRoute,
                        onNavigate = { route -> 
                            if (currentRoute != route) {
                                navController.navigate(route) {
                                    popUpTo("vault") { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        onAddClick = { showAddOverlay = true }
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.background,
            modifier = Modifier.blur(if (showAddOverlay) 16.dp else 0.dp),
            contentWindowInsets = WindowInsets(0, 0, 0, 0) // Remove default scaffold insets to allow true edge-to-edge
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = "splash",
                modifier = Modifier.fillMaxSize()
            ) {
                composable("splash") {
                    SplashScreen(onSplashFinished = {
                        navController.navigate("foyer") {
                            popUpTo("splash") { inclusive = true }
                        }
                    })
                }
                composable("foyer") {
                    FoyerScreen(onEnterVault = {
                        navController.navigate("vault") {
                            popUpTo("foyer") { inclusive = true }
                        }
                    })
                }
                composable("vault") {
                    DashboardScreen(
                        viewModel = vaultViewModel, 
                        paddingValues = paddingValues,
                        onIdentityClick = { showIdentityOverlay = true },
                        onItemClick = { item -> selectedWishItem = item }
                    )
                }
                composable("archives") {
                    ArchivesScreen(
                        modifier = Modifier.padding(paddingValues),
                        onIdentityClick = { showIdentityOverlay = true }
                    )
                }
                composable("curator") {
                    CuratorScreen(
                        modifier = Modifier.padding(paddingValues),
                        onIdentityClick = { showIdentityOverlay = true }
                    )
                }
                composable("tracker") {
                    TrackerScreen(modifier = Modifier.padding(paddingValues))
                }
            }
        }

        // Global Overlay covers the entire Scaffold (including bottom nav)
        AddWishOverlay(
            isVisible = showAddOverlay,
            onClose = { showAddOverlay = false },
            onSave = { title, brand, imageUrl -> vaultViewModel.addWish(title, brand, imageUrl) },
            onExtract = { url -> vaultViewModel.extractProductData(url) }
        )

        // Identity Overlay covers the ecosystem softly
        IdentityOverlay(
            isVisible = showIdentityOverlay,
            onClose = { showIdentityOverlay = false },
            onDeveloperModeClick = { showDeveloperLogs = true }
        )

        // Wish Detail Overlay for specific items
        WishDetailOverlay(
            item = selectedWishItem,
            isVisible = selectedWishItem != null,
            onClose = { selectedWishItem = null }
        )

        // Developer Logs Layer (Top-most)
        DeveloperLogsOverlay(
            isVisible = showDeveloperLogs,
            onClose = { showDeveloperLogs = false }
        )
    }
}
