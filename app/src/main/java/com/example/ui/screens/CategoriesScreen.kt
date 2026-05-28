package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.Wallpaper
import com.example.ui.theme.*
import com.example.ui.viewmodel.WallpaperViewModel

@Composable
fun CategoriesScreen(
    viewModel: WallpaperViewModel,
    onPreviewWallpaper: (Wallpaper) -> Unit,
    modifier: Modifier = Modifier
) {
    val wallpapers by viewModel.wallpapersList.collectAsState()
    val activeSubCategory by viewModel.selectedCategory.collectAsState()

    val categoriesData = listOf(
        CategoryItem("Cars", "Vroom space & stance", "https://images.unsplash.com/photo-1503376780353-7e6692767b70?auto=format&fit=crop&q=80&w=600"),
        CategoryItem("Supercars", "Raw horsepower & glowing neon", "https://images.unsplash.com/photo-1614162692292-7ac56d7f7f1e?auto=format&fit=crop&q=80&w=600"),
        CategoryItem("Anime", "Holographic custom illustrations", "https://images.unsplash.com/photo-1540959733332-eab4deceeaf7?auto=format&fit=crop&q=80&w=600"),
        CategoryItem("Dark", "Sleek AMOLED pitch-black", "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&q=80&w=600"),
        CategoryItem("Gaming", "RGB grids & setup specs", "https://images.unsplash.com/photo-1542751371-adc38448a05e?auto=format&fit=crop&q=80&w=600"),
        CategoryItem("Nature", "Moody mist forest & peaks", "https://images.unsplash.com/photo-1475924156734-496f6cac6ec1?auto=format&fit=crop&q=80&w=600"),
        CategoryItem("Islamic", "Elegant minarets & gold art", "https://images.unsplash.com/photo-1564507592333-c60657eea523?auto=format&fit=crop&q=80&w=600"),
        CategoryItem("Neon", "Vaporwave grids & glowing sign boards", "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?auto=format&fit=crop&q=80&w=600"),
        CategoryItem("AI Art", "Mech knights & futuristic render", "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?auto=format&fit=crop&q=80&w=600"),
        CategoryItem("Space", "Atmospheric stellar nebula folds", "https://images.unsplash.com/photo-1462331940025-496dfbfc7564?auto=format&fit=crop&q=80&w=600"),
        CategoryItem("Bikes", "Hypnotic high revving race machines", "https://images.unsplash.com/photo-1558981806-ec527fa84c39?auto=format&fit=crop&q=80&w=600"),
        CategoryItem("Abstract", "Liquid glass flow & custom smoke", "https://images.unsplash.com/photo-1550684848-fac1c5b4e853?auto=format&fit=crop&q=80&w=600"),
        CategoryItem("Minimal", "Symmetrical designs & vector spaces", "https://images.unsplash.com/photo-1518770660439-4636190af475?auto=format&fit=crop&q=80&w=600"),
        CategoryItem("AMOLED", "Fully black energy conserving pixel arrays", "https://images.unsplash.com/photo-1541701494587-cb58502866ab?auto=format&fit=crop&q=80&w=600")
    )

    AnimatedContent(
        targetState = activeSubCategory,
        transitionSpec = {
            fadeIn() togetherWith fadeOut()
        },
        label = "category_view"
    ) { activeCat ->
        if (activeCat == null) {
            // General Category directory cards list
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(PureBlack)
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "DISCOVER // SPACES",
                    fontSize = 14.sp,
                    color = NeonBlue,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
                Text(
                    text = "CATEGORIES",
                    style = MaterialTheme.typography.displayMedium,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(20.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 100.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(categoriesData) { item ->
                        CategoryCardItem(category = item, onClick = {
                            viewModel.selectCategory(item.name)
                        })
                    }
                }
            }
        } else {
            // Subcategory list screen
            val subcategoryWallpapers = remember(wallpapers, activeCat) {
                wallpapers.filter { it.category.equals(activeCat, ignoreCase = true) }
            }

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(PureBlack)
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { viewModel.selectCategory(null) },
                        modifier = Modifier
                            .background(DarkGray, RoundedCornerShape(12.dp))
                            .border(0.5.dp, Color.White.copy(0.08f), RoundedCornerShape(12.dp))
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go back",
                            tint = NeonBlue
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "PORTAL // EXPLORE",
                            fontSize = 11.sp,
                            color = PurpleGlow,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp,
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                        )
                        Text(
                            text = activeCat.uppercase(),
                            fontSize = 24.sp,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                if (subcategoryWallpapers.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No active wallpapers uploaded in $activeCat.", color = TextGray)
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        contentPadding = PaddingValues(bottom = 100.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(subcategoryWallpapers) { wall ->
                            WallpaperItemCard(
                                wallpaper = wall,
                                height = 240.dp,
                                onClick = { onPreviewWallpaper(wall) },
                                onDoubleTapLike = { viewModel.likeWallpaper(wall) },
                                isFavoriteFlow = viewModel.isFavorite(wall.id),
                                onToggleFavorite = { viewModel.toggleFavorite(wall) }
                            )
                        }
                    }
                }
            }
        }
    }
}

data class CategoryItem(
    val name: String,
    val headline: String,
    val imageUrl: String
)

@Composable
fun CategoryCardItem(
    category: CategoryItem,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .clip(RoundedCornerShape(20.dp))
            .border(0.5.dp, Color.White.copy(0.08f), RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
    ) {
        AsyncImage(
            model = category.imageUrl,
            contentDescription = category.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Backdrop gradients
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(0.9f)),
                        startY = 50f
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(14.dp)
        ) {
            Text(
                text = category.name,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.2.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = category.headline,
                color = TextGray,
                fontSize = 10.sp,
                lineHeight = 12.sp,
                maxLines = 2,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
