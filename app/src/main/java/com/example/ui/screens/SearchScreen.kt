package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Wallpaper
import com.example.ui.theme.*
import com.example.ui.viewmodel.WallpaperViewModel

@Composable
fun SearchScreen(
    viewModel: WallpaperViewModel,
    onPreviewWallpaper: (Wallpaper) -> Unit,
    modifier: Modifier = Modifier
) {
    val query by viewModel.searchQuery.collectAsState()
    val activeColor by viewModel.searchColor.collectAsState()
    val activeResolution by viewModel.searchResolution.collectAsState()
    val wallpapersList by viewModel.wallpapersList.collectAsState()

    // Interactive compute search results
    val searchResults = remember(wallpapersList, query, activeColor, activeResolution) {
        var list = wallpapersList

        if (query.isNotEmpty()) {
            list = list.filter { wall ->
                wall.title.contains(query, ignoreCase = true) ||
                        wall.tags.any { tag -> tag.contains(query, ignoreCase = true) } ||
                        wall.category.contains(query, ignoreCase = true)
            }
        }

        if (activeColor != null) {
            list = list.filter { it.colorHex.equals(activeColor, ignoreCase = true) }
        }

        if (activeResolution != null) {
            list = list.filter { it.resolution.contains(activeResolution!!) }
        }

        list
    }

    val trendingTags = listOf("gtr", "porsche", "neon", "cyberpunk", "anime", "amoled", "space", "islamic", "minimal")

    val colorFilters = listOf(
        ColorFilter("#00F0FF", NeonBlue, "Blue"),
        ColorFilter("#8B5CF6", PurpleGlow, "Purple"),
        ColorFilter("#FF3366", AlertRed, "Red"),
        ColorFilter("#FFCC00", Color(0xFFFFCC00), "Gold"),
        ColorFilter("#FFFFFF", Color.White, "White"),
        ColorFilter("#000000", Color(0xFF222222), "AMOLED")
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PureBlack)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "SEARCH // FILTERS",
            fontSize = 11.sp,
            color = NeonBlue,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp,
            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
        )
        Text(
            text = "EXPLORE",
            style = MaterialTheme.typography.displayMedium,
            color = Color.White,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Large Premium Search Bar
        OutlinedTextField(
            value = query,
            onValueChange = { viewModel.searchQuery.value = it },
            placeholder = { Text("Look up cars, anime, grids, spaces...", color = Color.White.copy(0.4f), fontSize = 14.sp) },
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = NeonBlue) },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { viewModel.searchQuery.value = "" }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Clear", tint = Color.White)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("search_field_entry"),
            shape = RoundedCornerShape(20.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = DarkGray,
                unfocusedContainerColor = DarkGray,
                focusedBorderColor = NeonBlue,
                unfocusedBorderColor = Color.White.copy(alpha = 0.08f),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Horizontal Category Quick Tags Slider
        Text("HOT TRENDS", fontSize = 11.sp, color = TextGray, fontWeight = FontWeight.SemiBold, letterSpacing = 1.2.sp)
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(trendingTags) { tag ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(30.dp))
                        .background(if (query == tag) NeonBlue else DarkGray)
                        .clickable { viewModel.searchQuery.value = tag }
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "#$tag",
                        color = if (query == tag) PureBlack else Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hex Color Accent Filter
        Text("PALETTE CHROMATIC SHUNTS", fontSize = 11.sp, color = TextGray, fontWeight = FontWeight.SemiBold, letterSpacing = 1.2.sp)
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(colorFilters) { filter ->
                val isSelected = activeColor == filter.hex
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(filter.color)
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) NeonBlue else Color.White.copy(0.12f),
                            shape = CircleShape
                        )
                        .clickable {
                            if (isSelected) {
                                viewModel.searchColor.value = null
                            } else {
                                viewModel.searchColor.value = filter.hex
                            }
                        }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Multi resolution picker
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("RATIO / RESOLUTION", fontSize = 11.sp, color = TextGray, fontWeight = FontWeight.SemiBold, letterSpacing = 1.2.sp)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("3840", "4000").forEach { res ->
                    val isSelected = activeResolution == res
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) PurpleGlow else DarkGray)
                            .clickable {
                                if (isSelected) {
                                    viewModel.searchResolution.value = null
                                } else {
                                    viewModel.searchResolution.value = res
                                }
                            }
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (res == "3840") "4K UHD" else "MOBILE HD",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Result text match indicator
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "MATCHES FOUND //",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite,
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
            )
            Text(
                text = "${searchResults.size} IMAGES",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = NeonBlue
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Masonry Grid View for results
        if (searchResults.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text("No matching wallpapers. Adjust selection grid.", color = TextGray)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 100.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(searchResults) { wall ->
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

data class ColorFilter(
    val hex: String,
    val color: Color,
    val name: String
)
