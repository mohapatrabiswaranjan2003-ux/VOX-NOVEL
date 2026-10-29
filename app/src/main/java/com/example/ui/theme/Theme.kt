package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = AmberPrimaryDark,
    onPrimary = ObsidianBackgroundDark,
    primaryContainer = SlateSurfaceVariantDark,
    onPrimaryContainer = AmberPrimaryDark,
    secondary = AmberSecondaryDark,
    onSecondary = ObsidianBackgroundDark,
    secondaryContainer = SlateSurfaceVariantDark,
    onSecondaryContainer = TextParchmentDark,
    tertiary = TerracottaTertiaryDark,
    onTertiary = ObsidianBackgroundDark,
    background = ObsidianBackgroundDark,
    onBackground = TextParchmentDark,
    surface = SlateSurfaceDark,
    onSurface = TextParchmentDark,
    surfaceVariant = SlateSurfaceVariantDark,
    onSurfaceVariant = TextMutedDark,
    outline = SlateSurfaceVariantDark
)

private val LightColorScheme = lightColorScheme(
    primary = AmberPrimaryLight,
    onPrimary = LinenBackgroundLight,
    primaryContainer = ParchmentSurfaceVariantLight,
    onPrimaryContainer = AmberSecondaryLight,
    secondary = AmberSecondaryLight,
    onSecondary = LinenBackgroundLight,
    secondaryContainer = ParchmentSurfaceVariantLight,
    onSecondaryContainer = CharcoalTextLight,
    tertiary = TerracottaTertiaryLight,
    onTertiary = LinenBackgroundLight,
    background = LinenBackgroundLight,
    onBackground = CharcoalTextLight,
    surface = ParchmentSurfaceLight,
    onSurface = CharcoalTextLight,
    surfaceVariant = ParchmentSurfaceVariantLight,
    onSurfaceVariant = CharcoalMutedLight,
    outline = ParchmentSurfaceVariantLight
)

@Composable
fun VoxNovelTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
