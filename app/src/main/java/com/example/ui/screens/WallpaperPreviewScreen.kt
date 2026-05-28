package com.example.ui.screens

import android.app.WallpaperManager
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.Wallpaper
import com.example.ui.components.GlassCard
import com.example.ui.theme.*
import com.example.ui.viewmodel.WallpaperViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun WallpaperPreviewScreen(
    viewModel: WallpaperViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val wallpaper by viewModel.activeWallpaper.collectAsState()
    val isPremiumUser by viewModel.isPremiumUser.collectAsState()
    val isSettingWallpaper by viewModel.isSettingWallpaper.collectAsState()
    val downloadProgress by viewModel.downloadProgress.collectAsState()

    val currentWallpaper = wallpaper ?: return

    val isFav by viewModel.isFavorite(currentWallpaper.id).collectAsState(initial = false)

    var showApplyDialog by remember { mutableStateOf(false) }
    var scaleAnim by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        scaleAnim = true
    }

    // Zooming effect on page arrival
    val scalingFactor by animateFloatAsState(
        targetValue = if (scaleAnim) 1.05f else 1.0f,
        animationSpec = tween(1200, easing = EaseOutSine),
        label = "zoom"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PureBlack)
    ) {
        // 1. FULLSCREEN IMMERSIVE WALLPAPER GRAPHIC
        AsyncImage(
            model = currentWallpaper.url,
            contentDescription = currentWallpaper.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .scale(scalingFactor)
        )

        // Vignette Dark gradient overlay shading
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(0.4f),
                            Color.Transparent,
                            Color.Black.copy(0.95f)
                        ),
                        startY = 50f
                    )
                )
        )

        // 2. BACK NAVIGATION & FAV OPTION FLOATER
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(PureBlack.copy(0.6f))
                    .border(0.5.dp, Color.White.copy(0.08f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Leave preview",
                    tint = NeonBlue
                )
            }

            IconButton(
                onClick = { viewModel.toggleFavorite(currentWallpaper) },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(PureBlack.copy(0.6f))
                    .border(0.5.dp, Color.White.copy(0.08f), CircleShape)
            ) {
                Icon(
                    imageVector = if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite image state selector",
                    tint = if (isFav) AlertRed else Color.White
                )
            }
        }

        // 3. OVERLAID DETAIL ACTIONS TIER
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Glassmorphic Detail Panel metadata block
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                intensity = 0.12f,
                borderColor = Color.White.copy(0.05f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    // Category + Resolution
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Brush.horizontalGradient(CyberGlowBrush))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = currentWallpaper.category.uppercase(),
                                color = PureBlack,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }

                        Text(
                            text = currentWallpaper.resolution,
                            color = TextGray,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Title + Likes Count
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = currentWallpaper.title,
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = "By ${currentWallpaper.author} //",
                                color = TextGray,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(30.dp))
                                .background(Color.White.copy(0.06f))
                                .clickable { viewModel.likeWallpaper(currentWallpaper) }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Filled.Favorite,
                                    contentDescription = "Likes count",
                                    tint = AlertRed,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "${currentWallpaper.likesCount}",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Simulated color swatches extracted from the wallpaper image!
                    Text(
                        "COLORS EXTRACTION ENCODING //",
                        fontSize = 9.sp,
                        color = Color.White.copy(0.5f),
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val activeColorHex = currentWallpaper.colorHex
                        val secondaryHex = when (activeColorHex) {
                            "#00F0FF" -> "#8B5CF6"
                            "#8B5CF6" -> "#FF3366"
                            "#FF3366" -> "#000000"
                            else -> "#222222"
                        }
                        listOf(activeColorHex, secondaryHex, "#111111", "#FFFFFF").forEach { hex ->
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(Color(android.graphics.Color.parseColor(hex)))
                                    .border(1.dp, Color.White.copy(alpha = 0.1f), CircleShape)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // ACTION GRID BUTTONS (Download, Install backdrop)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // DOWNLOAD BUTTON
                        Button(
                            onClick = { viewModel.downloadWallpaperToGallery(context) },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .testTag("download_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = PureBlack
                            ),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            if (downloadProgress != null) {
                                CircularProgressIndicator(
                                    progress = { downloadProgress!! },
                                    color = PureBlack,
                                    modifier = Modifier.size(20.dp),
                                    strokeWidth = 2.dp,
                                )
                            } else {
                                Text(
                                    "DOWNLOAD",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    letterSpacing = 1.sp
                                )
                            }
                        }

                        // SET WALLPAPER ACTION BUTTON
                        Button(
                            onClick = { showApplyDialog = true },
                            modifier = Modifier
                                .weight(1.2f)
                                .height(50.dp)
                                .border(1.dp, NeonBlue, RoundedCornerShape(14.dp))
                                .testTag("set_wallpaper_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PureBlack.copy(0.6f),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text(
                                "INSTALL WALL",
                                color = NeonBlue,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // VIP Subscription Callout
            if (currentWallpaper.isPremium && !isPremiumUser) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Brush.horizontalGradient(CyberGlowBrush))
                        .clickable { viewModel.purchasePremiumToggle() }
                        .padding(horizontal = 18.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = PureBlack)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text("LOCKED WITH T4LHA PREMIUM", color = PureBlack, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
                                Text("Upgrade to bypass standard watermarks & export full 4K resolutions.", color = PureBlack.copy(0.7f), fontSize = 9.sp, lineHeight = 11.sp)
                            }
                        }
                        Text("JOIN VIP", color = PureBlack, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.border(1.dp, PureBlack, RoundedCornerShape(4.dp)).padding(horizontal = 6.dp, vertical = 2.dp))
                    }
                }
            }
        }

        // Wallpaper progress bar indicators
        if (isSettingWallpaper) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(PureBlack.copy(0.8f)),
                contentAlignment = Alignment.Center
            ) {
                GlassCard(
                    modifier = Modifier.width(280.dp),
                    intensity = 0.2f,
                    borderColor = NeonBlue
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        CircularProgressIndicator(color = NeonBlue)
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            "INSTALLING BACKGROUNDS...",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                        )
                    }
                }
            }
        }

        // 4. INSTALL OPTIONS POPUP SHEET
        if (showApplyDialog) {
            AlertDialog(
                onDismissRequest = { showApplyDialog = false },
                title = {
                    Text(
                        text = "INSTALLATION PANEL",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                },
                text = {
                    Text(
                        "Configure standard layout placements for on-device screens. Tap to initialize direct system application.",
                        color = TextGray,
                        fontSize = 13.sp
                    )
                },
                confirmButton = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                showApplyDialog = false
                                viewModel.applyWallpaperOnDevice(context, WallpaperManager.FLAG_SYSTEM)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = DarkGray, contentColor = Color.White)
                        ) {
                            Text("HOME SCREEN ONLY", color = NeonBlue, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                showApplyDialog = false
                                viewModel.applyWallpaperOnDevice(context, WallpaperManager.FLAG_LOCK)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = DarkGray, contentColor = Color.White)
                        ) {
                            Text("LOCK SCREEN ONLY", color = PurpleGlow, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                showApplyDialog = false
                                viewModel.applyWallpaperOnDevice(context, WallpaperManager.FLAG_SYSTEM or WallpaperManager.FLAG_LOCK)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = NeonBlue, contentColor = PureBlack)
                        ) {
                            Text("INSTALL BOTH SCREENS", fontWeight = FontWeight.ExtraBold)
                        }
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showApplyDialog = false }) {
                        Text("CANCEL", color = Color.White.copy(0.6f))
                    }
                },
                containerColor = DarkGray,
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.border(0.5.dp, Color.White.copy(0.1f), RoundedCornerShape(24.dp))
            )
        }
    }
}
