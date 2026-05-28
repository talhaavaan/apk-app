package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import kotlinx.coroutines.flow.Flow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.Wallpaper
import com.example.ui.components.CategoryLabelChip
import com.example.ui.components.GlassCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.WallpaperViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    viewModel: WallpaperViewModel,
    onNavigateToSearch: () -> Unit,
    onPreviewWallpaper: (Wallpaper) -> Unit,
    modifier: Modifier = Modifier
) {
    val wallpapers by viewModel.wallpapersList.collectAsState()
    val activeTabCategory by viewModel.selectedCategory.collectAsState()

    // Filter list based on selected quick category. If null (All), show standard list or combined logic
    val filteredList = remember(wallpapers, activeTabCategory) {
        if (activeTabCategory == null) {
            wallpapers
        } else {
            wallpapers.filter { it.category == activeTabCategory }
        }
    }

    val trendingPicks = remember(wallpapers) {
        wallpapers.filter { it.likesCount >= 1500 }.take(4)
    }

    val categoriesList = listOf(
        "All", "Cars", "Supercars", "Anime", "Dark", "Gaming", "Nature", "Islamic", "Neon", "AI Art", "Space", "Bikes", "Abstract", "Minimal", "AMOLED", "4K", "Live Wallpapers"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(PureBlack),
        contentPadding = PaddingValues(bottom = 100.dp, top = 20.dp) // Generous safe padding for bottom bar
    ) {
        // 1. BRAND HEADER & SEARCH QUICK LINK
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "T4LHA //",
                            fontSize = 14.sp,
                            color = NeonBlue,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp,
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                        )
                        Text(
                            text = "WALLS",
                            style = MaterialTheme.typography.displayMedium,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                    }

                    // Floating Glowing Premium indicator
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Brush.horizontalGradient(CyberGlowBrush))
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                            .clickable { viewModel.purchasePremiumToggle() },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "VIP",
                                tint = PureBlack,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "VIP",
                                color = PureBlack,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Quick Search Bar field
                OutlinedTextField(
                    value = "",
                    onValueChange = { onNavigateToSearch() },
                    readOnly = true,
                    placeholder = {
                        Text(
                            "Search wallpaper tags (Nissan, Cyber, Nature...)",
                            color = Color.White.copy(0.4f),
                            fontSize = 13.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = NeonBlue
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("search_bar_clickable")
                        .clickable { onNavigateToSearch() },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = DarkGray,
                        unfocusedContainerColor = DarkGray,
                        focusedBorderColor = NeonBlue.copy(alpha = 0.5f),
                        unfocusedBorderColor = Color.White.copy(alpha = 0.08f)
                    )
                )
            }
        }

        // 2. HERO TRENDING PICKS PANEL (Only visible when active search category is null/All)
        if (activeTabCategory == null) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                ) {
                    PaddingRow {
                        Text(
                            text = "TRENDING NOW",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "FIRE PICKS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = PurpleGlow,
                            letterSpacing = 1.2.sp,
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp)
                    ) {
                        items(trendingPicks) { wall ->
                            HeroTrendingCard(wall = wall, onClick = { onPreviewWallpaper(wall) })
                        }
                    }
                }
            }
        }

        // 3. HORIZONTAL QUICK CATEGORIES SLIDER
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 14.dp)
            ) {
                PaddingRow {
                    Text(
                        text = "EXPLORE SPACES",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "CATEGORIES",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonBlue,
                        letterSpacing = 1.2.sp,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp)
                ) {
                    items(categoriesList) { cat ->
                        CategoryLabelChip(
                            text = cat,
                            isSelected = (cat == "All" && activeTabCategory == null) || (activeTabCategory == cat),
                            onClick = {
                                if (cat == "All") {
                                    viewModel.selectCategory(null)
                                } else {
                                    viewModel.selectCategory(cat)
                                }
                            }
                        )
                    }
                }
            }
        }

        // 4. CHRYSTAL MASONRY DOUBLE-COLUMN GRID
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Text(
                    text = if (activeTabCategory == null) "FEED // DISCOVER" else "CATALOG // $activeTabCategory",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Dual Column Masonry layout mapping
                if (filteredList.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No items found. Tap another spot.", color = TextGray)
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Left index items
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            val leftList = filteredList.filterIndexed { index, _ -> index % 2 == 0 }
                            leftList.forEach { wall ->
                                WallpaperItemCard(
                                    wallpaper = wall,
                                    height = if (wall.id.hashCode() % 3 == 0) 280.dp else 220.dp, // variable offsets
                                    onClick = { onPreviewWallpaper(wall) },
                                    onDoubleTapLike = { viewModel.likeWallpaper(wall) },
                                    isFavoriteFlow = viewModel.isFavorite(wall.id),
                                    onToggleFavorite = { viewModel.toggleFavorite(wall) }
                                )
                            }
                        }

                        // Right index items
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            val rightList = filteredList.filterIndexed { index, _ -> index % 2 != 0 }
                            rightList.forEach { wall ->
                                WallpaperItemCard(
                                    wallpaper = wall,
                                    height = if (wall.id.hashCode() % 2 == 0) 240.dp else 290.dp,
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
}

@Composable
fun HeroTrendingCard(
    wall: Wallpaper,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(260.dp)
            .height(160.dp)
            .clip(RoundedCornerShape(24.dp))
            .border(1.dp, color = Color.White.copy(alpha = 0.08f), shape = RoundedCornerShape(24.dp))
            .clickable(onClick = onClick)
    ) {
        AsyncImage(
            model = wall.url,
            contentDescription = wall.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Overlay scrim gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(0.85f)),
                        startY = 100f
                    )
                )
        )

        // VIP tag
        if (wall.isPremium) {
            Box(
                modifier = Modifier
                    .padding(14.dp)
                    .align(Alignment.TopEnd)
                    .clip(CircleShape)
                    .background(PureBlack.copy(0.7f))
                    .border(0.5.dp, NeonBlue, CircleShape)
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text("VIP", color = NeonBlue, fontSize = 9.sp, fontWeight = FontWeight.SemiBold)
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.White.copy(0.12f))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = wall.category.uppercase(),
                    color = NeonBlue,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = wall.title,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun WallpaperItemCard(
    wallpaper: Wallpaper,
    height: androidx.compose.ui.unit.Dp,
    onClick: () -> Unit,
    onDoubleTapLike: () -> Unit,
    isFavoriteFlow: Flow<Boolean>,
    onToggleFavorite: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var likeHeartVisible by remember { mutableStateOf(false) }
    val isFav by isFavoriteFlow.collectAsState(initial = false)

    // Setup popping heart scale animation
    val heartScale = remember { Animatable(0f) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(20.dp))
            .border(
                width = 0.5.dp,
                color = Color.White.copy(alpha = 0.05f),
                shape = RoundedCornerShape(20.dp)
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onClick() },
                    onDoubleTap = {
                        onDoubleTapLike()
                        coroutineScope.launch {
                            likeHeartVisible = true
                            heartScale.snapTo(0f)
                            heartScale.animateTo(
                                1.5f,
                                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                            )
                            delay(400)
                            heartScale.animateTo(0f, animationSpec = tween(150))
                            likeHeartVisible = false
                        }
                    }
                )
            }
    ) {
        AsyncImage(
            model = wallpaper.url,
            contentDescription = wallpaper.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Card Scrim overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(0.85f)),
                        startY = 200f
                    )
                )
        )

        // Favorite Toggle bubble tag
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(10.dp)
                .size(34.dp)
                .clip(CircleShape)
                .background(PureBlack.copy(0.6f))
                .clickable { onToggleFavorite() }
                .border(0.5.dp, Color.White.copy(0.08f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Save favorite",
                tint = if (isFav) AlertRed else Color.White,
                modifier = Modifier.size(16.dp)
            )
        }

        // Live visual indicator
        if (wallpaper.isLive) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(10.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(PurpleGlow.copy(0.8f))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    "LIVE",
                    color = Color.White,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }
        }

        // Title Metadata block
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(12.dp)
        ) {
            Text(
                text = wallpaper.title,
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "By ${wallpaper.author}",
                color = TextGray,
                fontSize = 10.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Big animated heart pop when double-tapped!
        if (likeHeartVisible) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Post double tapped like animation",
                    tint = AlertRed,
                    modifier = Modifier
                        .size(54.dp)
                        .scale(heartScale.value)
                )
            }
        }
    }
}

@Composable
fun PaddingRow(content: @Composable RowScope.() -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        content = content
    )
}
