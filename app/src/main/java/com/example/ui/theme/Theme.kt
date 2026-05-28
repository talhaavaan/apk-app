package com.example.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView

private val DarkColorScheme = darkColorScheme(
    primary = NeonBlue,
    secondary = PurpleGlow,
    tertiary = NeonBlue,
    background = PureBlack,
    surface = DarkGray,
    onPrimary = PureBlack,
    onSecondary = TextWhite,
    onBackground = TextWhite,
    onSurface = TextWhite,
    surfaceVariant = SurfaceGray,
    onSurfaceVariant = TextGray,
    error = AlertRed
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Force AMOLED rich dark theme by default
    dynamicColor: Boolean = false, // Disable dynamic colors to protect the strict cyber brand
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            var context = view.context
            var activity: Activity? = null
            while (context is android.content.ContextWrapper) {
                if (context is Activity) {
                    activity = context
                    break
                }
                context = context.baseContext
            }
            if (context is Activity) {
                activity = context
            }
            
            activity?.window?.let { window ->
                window.statusBarColor = PureBlack.toArgb()
                window.navigationBarColor = PureBlack.toArgb()
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
