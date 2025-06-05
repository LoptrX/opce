package com.ohuo.application.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


// 自定义自然风格配色
val GreenThemeColors = lightColorScheme(
    primary = Color(0xFF2E7D32),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFA5D6A7),
    secondary = Color(0xFF558B2F),
    tertiary = Color(0xFF33691E),
    surfaceVariant = Color(0xFFE8F5E9),
    error = Color(0xFFB71C1C)
)

// 适合深色模式的霓虹风格
val NeonPinkDarkColors = darkColorScheme(
    primary = Color(0xFFFF0266),
    secondary = Color(0xFF00E5FF),
    tertiary = Color(0xFF76FF03),
    surface = Color(0xFF2A2A2A),
    background = Color(0xFF121212),
    onPrimary = Color.Black // 高对比度
)

@Composable
fun DynamicTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if(darkTheme) NeonPinkDarkColors else GreenThemeColors,
        typography = MaterialTheme.typography,
        content = content
    )
}