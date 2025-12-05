package com.xdev.jetpack.ui.theme

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

 val RedDarkColorScheme = darkColorScheme(
     primary = RedSwich,
     onPrimary = Color(0x77FF0000),
     secondaryContainer = Color(0xFF592A2A),
     onSecondaryContainer = Color(0xFFE18D8D)
)
private val RedLightColorScheme = lightColorScheme(
    primary = Color(0xFFF54646),
    onPrimary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFCB6464),
    onSecondaryContainer = Color(0xFFF59595)
)
private val YellowDarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFD967),
    onPrimary = Color(0xB5FFBF00),
    secondaryContainer = Color(0xFF59492A),
    onSecondaryContainer = Color(0xFFE1C58D)
)
private val YellowLightColorScheme = lightColorScheme(
    primary = Color(0xFFF5CF6C),
    onPrimary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFDAC36D),
    onSecondaryContainer = Color(0xFFECD4AB)
)

@Composable
fun dynamicTheme(context: Context, dynamicColor: Boolean, isDark: Boolean): ColorScheme {
    if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        return if (isDark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    } else {
        LaunchedEffect(Unit) {
            Toast.makeText(context, "Android 12 need", Toast.LENGTH_SHORT).show()
        }
        return if (isDark) DarkColorScheme else LightColorScheme
    }
}

@Composable
fun JetpackTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    colorTheme: String,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val colorScheme = when (colorTheme) {
        "default" -> dynamicTheme(context, dynamicColor, darkTheme)
        "dynamic dark" -> dynamicTheme(context, dynamicColor, true)
        "dynamic light" -> dynamicTheme(context, dynamicColor, false)
        "dark" -> DarkColorScheme
        "light" -> LightColorScheme
        "red" -> if (darkTheme) RedDarkColorScheme else RedLightColorScheme
        "yellow" -> if (darkTheme) YellowDarkColorScheme else YellowLightColorScheme
        else -> if (darkTheme) DarkColorScheme else LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}