package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.PureBlack
import com.example.ui.theme.PurpleGlow

@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    
    // Constant pulsing neon radius
    val glowPulse by infiniteTransition.animateFloat(
        initialValue = 40f,
        targetValue = 90f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    // Linear entry animations
    val scale = remember { Animatable(0.4f) }
    val opacity = remember { Animatable(0f) }

    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessVeryLow
            )
        )
    }

    LaunchedEffect(key1 = true) {
        opacity.animateTo(
            targetValue = 1f,
            animationSpec = tween(1500, easing = EaseOutQuad)
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PureBlack),
        contentAlignment = Alignment.Center
    ) {
        // Decorative glowing particles background
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerOffset = Offset(size.width / 2f, size.height / 2f)
            
            // Outer geometric energy rings
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(PurpleGlow.copy(0.18f), Color.Transparent),
                    center = centerOffset,
                    radius = 350f
                ),
                radius = 350f,
                center = centerOffset
            )
            
            drawCircle(
                color = NeonBlue.copy(0.3f),
                radius = 240f + (glowPulse * 0.2f),
                center = centerOffset,
                style = Stroke(width = 1.5f)
            )

            drawCircle(
                color = PurpleGlow.copy(0.15f),
                radius = 300f - (glowPulse * 0.15f),
                center = centerOffset,
                style = Stroke(width = 1f)
            )

            // Drawing futuristic radar coordinate points
            drawCircle(color = NeonBlue, radius = 4f, center = Offset(size.width / 2f - 240f, size.height / 2f))
            drawCircle(color = PurpleGlow, radius = 4f, center = Offset(size.width / 2f + 240f, size.height / 2f))
        }

        // Animated Logotype block
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .scale(scale.value)
                .alpha(opacity.value)
        ) {
            // Graphic element: Triangular minimal terminal logo
            Canvas(modifier = Modifier.size(64.dp)) {
                val path = androidx.compose.ui.graphics.Path().apply {
                    moveTo(size.width / 2f, 0f)
                    lineTo(size.width, size.height)
                    lineTo(0f, size.height)
                    close()
                }
                drawPath(
                    path = path,
                    brush = Brush.horizontalGradient(listOf(NeonBlue, PurpleGlow)),
                    style = Stroke(width = 3.dp.toPx())
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Main display trademark
            Text(
                text = "T4LHA",
                fontSize = 44.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                letterSpacing = 8.sp,
                fontFamily = FontFamily.SansSerif,
                textAlign = TextAlign.Center
            )

            Text(
                text = "WALLS",
                fontSize = 44.sp,
                fontWeight = FontWeight.Light,
                color = NeonBlue,
                letterSpacing = 12.sp,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.offset(x = 3.dp),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Subtitle tagline code
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(PurpleGlow)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "AESTHETIC PORTAL // VER 2.0.6",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(0.6f),
                    letterSpacing = 3.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}
