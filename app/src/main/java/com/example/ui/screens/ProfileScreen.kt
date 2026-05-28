package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.GlassCard
import com.example.ui.components.GlowButton
import com.example.ui.theme.*
import com.example.ui.viewmodel.WallpaperViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileScreen(
    viewModel: WallpaperViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val username by viewModel.username.collectAsState()
    val userEmail by viewModel.userEmail.collectAsState()
    val isPremium by viewModel.isPremiumUser.collectAsState()
    val totalDownloads by viewModel.totalDownloadsCount.collectAsState()

    // Cache clean state
    var isCleaningCache by remember { mutableStateOf(false) }

    // Admin Upload forms states
    var adminTitle by remember { mutableStateOf("") }
    var adminCategory by remember { mutableStateOf("Cars") }
    var adminUrl by remember { mutableStateOf("") }
    var adminColorHex by remember { mutableStateOf("#00F0FF") }
    var adminTags by remember { mutableStateOf("") }

    val categoriesDropdown = listOf(
        "Cars", "Supercars", "Anime", "Dark", "Gaming", "Nature", "Islamic", "Neon", "AI Art", "Space", "Bikes", "Abstract", "Minimal", "AMOLED"
    )
    var dropdownExpanded by remember { mutableStateOf(false) }

    // Settings States
    var dloadQualityHigh by remember { mutableStateOf(true) }
    var notificationOn by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PureBlack)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        
        // 1. TOP HEADER BRAND
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "SETTINGS // UTILITY",
                    fontSize = 11.sp,
                    color = NeonBlue,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
                Text(
                    text = "PROFILE",
                    fontSize = 28.sp,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
            }

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(DarkGray)
                    .border(1.dp, NeonBlue.copy(0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = username.take(2).uppercase(),
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 2. USER DECK SUMMARY INFO
        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            intensity = 0.1f,
            borderColor = Color.White.copy(0.05f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                        // Online glow pulsing indicator
                            .background(if (isPremium) PurpleGlow else NeonBlue)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (isPremium) "T4LHA VIP CREW" else "STANDARD OPERATOR",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = username,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = userEmail,
                    color = TextGray,
                    fontSize = 12.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Stats rows (downloads tracker, cache sizing, network parameters)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Downloads Stat
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .background(Color.White.copy(0.04f), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Text("TOTAL DOWNLOADS", color = TextGray, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        Text("$totalDownloads IMAGES", color = NeonBlue, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                    }

                    // Network speed rating Mock
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .background(Color.White.copy(0.04f), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Text("SERVER PATH", color = TextGray, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        Text("PEXELS VIA REST", color = PurpleGlow, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 3. SPECIAL VIP PREMIUM DECK PANEL
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(PurpleGlow, DarkGray, PureBlack),
                        start = androidx.compose.ui.geometry.Offset(0f, 0f),
                        end = androidx.compose.ui.geometry.Offset(1000f, 1000f)
                    )
                )
                .border(
                    width = 1.dp,
                    brush = Brush.horizontalGradient(listOf(NeonBlue.copy(0.6f), Color.Transparent)),
                    shape = RoundedCornerShape(24.dp)
                )
                .clickable { viewModel.purchasePremiumToggle() }
                .padding(24.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "VIP UNLOCK CONSOLE",
                        fontSize = 11.sp,
                        color = NeonBlue,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                    )
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = if (isPremium) PurpleGlow else Color.White
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = if (isPremium) "YOU'RE CO-PILOTING PREMIUM" else "ACTIVATE INTUITIVE ULTRA 4K",
                    fontSize = 18.sp,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Bypass advertisements, auto-sync Live animations, extract complete raw uncompressed metadata parameters, and access the restricted catalog folders.",
                    color = TextGray,
                    fontSize = 11.sp,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(18.dp))

                GlowButton(
                    text = if (isPremium) "TERMINATE SUBSCRIPTION" else "UNLOCK PREMIUM ACCESS",
                    onClick = { viewModel.purchasePremiumToggle() }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 4. CORE SETTINGS SECTION
        Text(
            text = "PREFERENCES // ENGINE",
            fontSize = 11.sp,
            color = TextGray,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.2.sp,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(10.dp))

        // High Quality toggle
        PreferenceRow(
            iconColor = NeonBlue,
            title = "Download HD Raw Ultra-Res",
            subtitle = "Enable maximal pixel bandwidth export settings",
            checked = dloadQualityHigh,
            onCheckedChange = { dloadQualityHigh = it }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Notification toggle
        PreferenceRow(
            iconColor = PurpleGlow,
            title = "Notification Pings",
            subtitle = "Daily recommended walls & categories notices",
            checked = notificationOn,
            onCheckedChange = { notificationOn = it }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Cache Cleaner Utility Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(DarkGray)
                .clickable {
                    if (!isCleaningCache) {
                        isCleaningCache = true
                        viewModel.clearCacheFiles(context) { msg ->
                            isCleaningCache = false
                            Toast
                                .makeText(context, msg, Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(PureBlack),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = AlertRed
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Purge Cache Clusters",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (isCleaningCache) "Sweeping system memory clusters..." else "Free up cached image payloads (Saves space)",
                    color = TextGray,
                    fontSize = 11.sp
                )
            }
            if (isCleaningCache) {
                CircularProgressIndicator(
                    color = AlertRed,
                    modifier = Modifier.size(18.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "SWEEP",
                    color = Color.White.copy(0.4f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // 5. ADMIN PORTAL (FIRESTORE LIVE SIMULATION)
        Text(
            text = "HOST COMMAND // ADMIN PANEL",
            fontSize = 11.sp,
            color = TextGray,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.2.sp,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(10.dp))

        GlassCard(
            modifier = Modifier.fillMaxWidth(),
            intensity = 0.08f,
            borderColor = NeonBlue.copy(0.2f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Build, contentDescription = null, tint = NeonBlue, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "LIVE WALLPAPER UPLOADER",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Inject a fully functional remote high-quality Unsplash or Custom URL wallpaper into the live catalog feed immediately.",
                    color = TextGray,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Title Input
                OutlinedTextField(
                    value = adminTitle,
                    onValueChange = { adminTitle = it },
                    label = { Text("Wallpaper Display Title", fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Category Selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Select Category tag:", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Box {
                        Button(
                            onClick = { dropdownExpanded = true },
                            colors = ButtonDefaults.buttonColors(containerColor = DarkGray)
                        ) {
                            Text(adminCategory.uppercase(), color = NeonBlue, fontSize = 11.sp, fontWeight = FontWeight.Black)
                            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null, tint = NeonBlue)
                        }

                        DropdownMenu(
                            expanded = dropdownExpanded,
                            onDismissRequest = { dropdownExpanded = false },
                            modifier = Modifier.background(DarkGray)
                        ) {
                            categoriesDropdown.forEach { cat ->
                                DropdownMenuItem(
                                    text = { Text(cat, color = Color.White) },
                                    onClick = {
                                        adminCategory = cat
                                        dropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // URL Input
                OutlinedTextField(
                    value = adminUrl,
                    onValueChange = { adminUrl = it },
                    label = { Text("Image Link (HTTPS Unsplash URL)", fontSize = 11.sp) },
                    placeholder = { Text("https://images.unsplash.com/...", fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Color Swatch Hex Input
                OutlinedTextField(
                    value = adminColorHex,
                    onValueChange = { adminColorHex = it },
                    label = { Text("Primary Color Palette Hex (#00F0FF, #8B5CF6)", fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Tags Input
                OutlinedTextField(
                    value = adminTags,
                    onValueChange = { adminTags = it },
                    label = { Text("Tags (Comma Separated: audi, stance, cyber)", fontSize = 11.sp) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                GlowButton(
                    text = "INJECT WALLPAPER DESIGN",
                    onClick = {
                        viewModel.uploadNewWallpaperAdmin(
                            title = adminTitle,
                            category = adminCategory,
                            url = adminUrl,
                            colorHex = adminColorHex,
                            tagsInput = adminTags
                        )
                        // Toast alert and reset
                        Toast.makeText(context, "Wallpaper successfully injected! Check Category / Feed.", Toast.LENGTH_LONG).show()
                        adminTitle = ""
                        adminUrl = ""
                        adminTags = ""
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // ABOUT DEVELOPER INFO CARD
        Text(
            text = "INFO // COMPILER",
            fontSize = 11.sp,
            color = TextGray,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.2.sp,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(10.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(DarkGray)
                .padding(20.dp)
        ) {
            Text(
                text = "T4LHA WALLS",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                letterSpacing = 2.sp
            )
            Text(
                text = "AESTHETIC PORTAL // VER 2.0.6",
                fontSize = 10.sp,
                color = NeonBlue,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Engineered with modern Jetpack Compose, Material Design 3, Room Local SQL, and High Speed Coils. Proudly designed for aesthetic users, car lovers, and cyber visualizers.",
                color = TextGray,
                fontSize = 11.sp,
                lineHeight = 16.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(120.dp)) // Safe scrolling margins at very bottom
    }
}

@Composable
fun PreferenceRow(
    iconColor: Color,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(DarkGray)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(PureBlack),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (checked) Icons.Default.Check else Icons.Default.Close,
                contentDescription = null,
                tint = iconColor
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                color = TextGray,
                fontSize = 11.sp
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = PureBlack,
                checkedTrackColor = iconColor,
                uncheckedThumbColor = TextGray,
                uncheckedTrackColor = PureBlack
            )
        )
    }
}
