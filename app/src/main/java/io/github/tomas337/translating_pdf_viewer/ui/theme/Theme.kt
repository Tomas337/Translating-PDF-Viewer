package io.github.tomas337.translating_pdf_viewer.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

val DarkColorScheme = darkColorScheme(
    primary = Persimmon,
    onPrimary = Ivory,
    primaryContainer = DarkerSalmon,
    onPrimaryContainer = Charcoal,
    inversePrimary = DodgerBlue,
    secondary = Charcoal,
    onSecondary = White,
    secondaryContainer = DarkGray,
    onSecondaryContainer = Ivory,
    tertiary = Silver,
    onTertiary = Charcoal,
    tertiaryContainer = DarkGray,
    onTertiaryContainer = White,
    background = DarkSurface,
    onBackground = White,
    surface = DarkSurface,
    onSurface = White,
    surfaceVariant = MidToneGray,
    onSurfaceVariant = White,
    surfaceTint = DarkerSalmon,
    inverseSurface = Ivory,
    inverseOnSurface = Charcoal,
    error = ErrorRed,
    onError = White,
    errorContainer = DarkerRed,
    onErrorContainer = White,
    outline = Silver,
    outlineVariant = DarkGray,
    scrim = Black,
    surfaceBright = DarkSurface,
    surfaceContainer = MidToneGray,
    surfaceContainerHigh = DarkGray,
    surfaceContainerHighest = MidToneGray,
    surfaceContainerLow = DarkSurface,
    surfaceContainerLowest = DarkSurface,
    surfaceDim = MidToneGray
)

private val LightColorScheme = lightColorScheme(
    primary = Persimmon,
    onPrimary = Ivory,
    primaryContainer = Salmon,
    onPrimaryContainer = Charcoal,
    inversePrimary = DodgerBlue,
    secondary = DarkLiver,
    onSecondary = Ivory,
    secondaryContainer = Silver,
    onSecondaryContainer = Charcoal,
    tertiary = Charcoal,
    onTertiary = Ivory,
    tertiaryContainer = Gainsboro,
    onTertiaryContainer = Charcoal,
    background = Ivory,
    onBackground = Charcoal,
    surface = Ivory,
    onSurface = Charcoal,
    surfaceVariant = Bone,
    onSurfaceVariant = Charcoal,
    surfaceTint = Salmon,
    inverseSurface = Charcoal,
    inverseOnSurface = Ivory,
    error = FireOpal,
    onError = Ivory,
    errorContainer = Salmon,
    onErrorContainer = Charcoal,
    outline = DarkLiver,
    outlineVariant = Silver,
    scrim = Charcoal,
    surfaceBright = Ivory,
    surfaceContainer = Ivory,
    surfaceContainerHigh = Bone,
    surfaceContainerHighest = Bone,
    surfaceContainerLow = Ivory,
    surfaceContainerLowest = Ivory,
    surfaceDim = Bone
)

@Composable
fun TranslatingPDFViewerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}