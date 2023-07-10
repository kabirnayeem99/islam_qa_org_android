package io.github.kabirnayeem99.islamqaorg.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Defining the color scheme for the light theme.
private val LightThemeColors = lightColorScheme(
    primary = PurpleLight,
    onPrimary = White,
    primaryContainer = PinkLace,
    onPrimaryContainer = PurpleBarossa,
    secondary = PinkCannon,
    onSecondary = White,
    secondaryContainer = PinkLace,
    onSecondaryContainer = PurpleBarossa,
    tertiary = Cinnamon,
    onTertiary = White,
    tertiaryContainer = YellowLight,
    onTertiaryContainer = Cola,
    error = RedThunderbird,
    onError = White,
    errorContainer = Cola,
    onErrorContainer = PeachSchnapps,
    background = Tutu,
    onBackground = BlackThunder,
    surface = Tutu,
    onSurface = BlackThunder,
)

// Defining the color scheme for the dark theme.
private val DarkThemeColors = darkColorScheme(
    primary = PurpleDark,
    onPrimary = PurpleTyrian,
    primaryContainer = PurpleTyrian,
    onPrimaryContainer = PinkLace,
    secondary = PurpleLavenderRose,
    onSecondary = Color(0xff561250),
    secondaryContainer = Color(0xff712B68),
    onSecondaryContainer = PinkLace,
    tertiary = YellowDark,
    onTertiary = Color(0xff3A3000),
    tertiaryContainer = Color(0xff544600),
    onTertiaryContainer = Color(0xffFFE16E),
    error = Color(0xffFFB4AB),
    onError = Color(0xff690005),
    errorContainer = Color(0xff93000A),
    onErrorContainer = Color(0xffFFDAD6),
    background = BlackThunder,
    onBackground = Color(0xffEAE0E3),
    surface = BlackThunder,
    onSurface = Color(0xffEAE0E3),
)

@Composable
fun IslamQaTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    isDynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {

    // Checking if the theme is dark or light and then setting the color scheme accordingly.
    val colorScheme = when {

        isDarkTheme -> {
            if (isDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                dynamicDarkColorScheme(LocalContext.current)
            else DarkThemeColors
        }

        !isDarkTheme -> {
            if (isDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                dynamicLightColorScheme(LocalContext.current)
            else LightThemeColors
        }

        else -> DarkThemeColors

    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}