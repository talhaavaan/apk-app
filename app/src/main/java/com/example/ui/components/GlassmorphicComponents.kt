package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkGray
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.PureBlack
import com.example.ui.theme.PurpleGlow

@Composable
fun ShimmerItem(
    modifier: Modifier = Modifier,
    height: Dp = 200.dp,
    cornerRadius: Dp = 16.dp
) {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translate"
    )

    val shimmerColors = listOf(
        Color(0xFF0F0F14),
        Color(0xFF23232C),
        Color(0xFF0F0F14)
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(cornerRadius))
            .background(brush)
    )
}

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    borderColor: Color = Color.White.copy(alpha = 0.08f),
    borderWidth: Dp = 1.dp,
    cornerRadius: Dp = 20.dp,
    intensity: Float = 0.2f, // transparency weight
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = intensity),
                        Color.White.copy(alpha = intensity / 2f)
                    )
                )
            )
            .border(
                width = borderWidth,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        borderColor,
                        borderColor.copy(alpha = 0.2f)
                    )
                ),
                shape = RoundedCornerShape(cornerRadius)
            )
    ) {
        content()
    }
}

@Composable
fun GlowButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    glowColor: Color = NeonBlue,
    colors: List<Color> = listOf(NeonBlue, PurpleGlow)
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .drawBehind {
                drawRect(
                    color = glowColor.copy(alpha = 0.15f),
                    size = size,
                    alpha = 1.0f
                )
            }
            .blur(8.dp)
            .clip(RoundedCornerShape(12.dp))
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Brush.horizontalGradient(colors)),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            contentPadding = PaddingValues(vertical = 14.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = text,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }
    }
}

@Composable
fun CategoryLabelChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(
                if (isSelected) {
                    Brush.horizontalGradient(listOf(NeonBlue, PurpleGlow))
                } else {
                    Brush.horizontalGradient(listOf(DarkGray, DarkGray))
                }
            )
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(
                    if (isSelected) listOf(Color.Transparent, Color.Transparent)
                    else listOf(Color.White.copy(0.1f), Color.White.copy(0.02f))
                ),
                shape = RoundedCornerShape(14.dp)
            )
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            )
            .padding(horizontal = 20.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) PureBlack else Color.White,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            fontSize = 13.sp,
            letterSpacing = 0.5.sp
        )
    }
}
