package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassCard
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.WallpaperViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: WallpaperViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainContainer(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainContainer(viewModel: WallpaperViewModel) {
    val isSplashFinished by viewModel.isSplashFinished.collectAsState()
    val activeWallpaper by viewModel.activeWallpaper.collectAsState()

    AnimatedContent(
        targetState = isSplashFinished,
        transitionSpec = {
            fadeIn() togetherWith fadeOut()
        },
        label = "splash_to_main"
    ) { progressFinished ->
        if (!progressFinished) {
            SplashScreen()
        } else {
            // Main App Container
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(PureBlack)
            ) {
                val currentTab by viewModel.currentTab.collectAsState()

                // Layout display of selected tab
                Box(modifier = Modifier.fillMaxSize()) {
                    when (currentTab) {
                        "Home" -> HomeScreen(
                            viewModel = viewModel,
                            onNavigateToSearch = { viewModel.setTab("Search") },
                            onPreviewWallpaper = { viewModel.selectWallpaper(it) }
                        )
                        "Categories" -> CategoriesScreen(
                            viewModel = viewModel,
                            onPreviewWallpaper = { viewModel.selectWallpaper(it) }
                        )
                        "Favorites" -> FavoritesScreen(
                            viewModel = viewModel,
                            onPreviewWallpaper = { viewModel.selectWallpaper(it) }
                        )
                        "Search" -> SearchScreen(
                            viewModel = viewModel,
                            onPreviewWallpaper = { viewModel.selectWallpaper(it) }
                        )
                        "Profile" -> ProfileScreen(
                            viewModel = viewModel
                        )
                    }
                }

                // FLOATING GLASSMORPHIC BOTTOM NAVIGATION BAR
                // It sits centered at the bottom, floating over full content
                GlassCard(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 16.dp, vertical = 20.dp)
                        .navigationBarsPadding() // Shift above system navigation pills safely
                        .fillMaxWidth()
                        .height(68.dp),
                    intensity = 0.16f,
                    borderColor = Color.White.copy(0.08f),
                    borderWidth = 0.5.dp,
                    cornerRadius = 24.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 4.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val navItems = listOf(
                            NavigationTab("Home", Icons.Default.Home, "home_tab"),
                            NavigationTab("Categories", Icons.Default.List, "categories_tab"),
                            NavigationTab("Favorites", Icons.Default.Favorite, "favorites_tab"),
                            NavigationTab("Search", Icons.Default.Search, "search_tab"),
                            NavigationTab("Profile", Icons.Default.Settings, "profile_tab")
                        )

                        navItems.forEach { tab ->
                            val isSelected = currentTab == tab.name
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .testTag(tab.tag)
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable { viewModel.setTab(tab.name) }
                                    .padding(vertical = 4.dp, horizontal = 12.dp)
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .then(
                                            if (isSelected) {
                                                Modifier.background(Brush.radialGradient(listOf(NeonBlue.copy(0.24f), Color.Transparent)))
                                            } else {
                                                Modifier.background(Color.Transparent)
                                            }
                                        )
                                ) {
                                    Icon(
                                        imageVector = tab.icon,
                                        contentDescription = tab.name,
                                        tint = if (isSelected) NeonBlue else Color.White.copy(0.5f),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                
                                if (isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .width(8.dp)
                                            .height(2.dp)
                                            .clip(CircleShape)
                                            .background(NeonBlue)
                                    )
                                }
                            }
                        }
                    }
                }

                // 2. FULLSCREEN WALLPAPER PREVIEW OVERLAY VIEW
                // Automatically rendering on top when an active wallpaper object is loaded
                AnimatedVisibility(
                    visible = activeWallpaper != null,
                    enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                    exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
                    modifier = Modifier.fillMaxSize()
                ) {
                    WallpaperPreviewScreen(
                        viewModel = viewModel,
                        onBack = { viewModel.selectWallpaper(null) }
                    )
                }
            }
        }
    }
}

data class NavigationTab(
    val name: String,
    val icon: ImageVector,
    val tag: String
)
