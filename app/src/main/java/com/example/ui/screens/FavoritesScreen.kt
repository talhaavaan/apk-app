package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Wallpaper
import com.example.ui.components.GlowButton
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.PureBlack
import com.example.ui.theme.TextGray
import com.example.ui.viewmodel.WallpaperViewModel

@Composable
fun FavoritesScreen(
    viewModel: WallpaperViewModel,
    onPreviewWallpaper: (Wallpaper) -> Unit,
    modifier: Modifier = Modifier
) {
    val favoritesList by viewModel.favorites.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PureBlack)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "SAVED // OFFLINE",
            fontSize = 14.sp,
            color = NeonBlue,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp,
            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
        )
        Text(
            text = "MY FAVORITES",
            style = MaterialTheme.typography.displayMedium,
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(20.dp))

        if (favoritesList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "No Saved Items",
                        tint = NeonBlue.copy(0.3f),
                        modifier = Modifier.size(72.dp)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "DECK EMPTY // SECURE PORT",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Initialize favorites by double-tapping or visiting wallpaper cards in the primary main grids.",
                        color = TextGray,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(28.dp))
                    
                    GlowButton(
                        text = "EXPLORE WALLPAPERS",
                        onClick = { viewModel.setTab("Home") },
                        modifier = Modifier.width(220.dp)
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 100.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(favoritesList) { fave ->
                    val wallpaper = Wallpaper.fromFavorite(fave)
                    WallpaperItemCard(
                        wallpaper = wallpaper,
                        height = 240.dp,
                        onClick = { onPreviewWallpaper(wallpaper) },
                        onDoubleTapLike = { viewModel.likeWallpaper(wallpaper) },
                        isFavoriteFlow = viewModel.isFavorite(wallpaper.id),
                        onToggleFavorite = { viewModel.toggleFavorite(wallpaper) }
                    )
                }
            }
        }
    }
}
