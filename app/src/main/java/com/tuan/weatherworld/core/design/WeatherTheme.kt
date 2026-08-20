package com.tuan.weatherworld.core.design

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

private val LocalWeatherConditionColors = staticCompositionLocalOf {
    LightWeatherConditionColors
}

private val LocalWeatherSpacing = staticCompositionLocalOf {
    WeatherSpacing
}

/**
 * Root design theme for Weather World.
 *
 * Material 3 receives the general color, typography and shape tokens. Custom
 * composition locals expose weather-condition palettes and spacing, which do
 * not have equivalent roles in MaterialTheme.
 */
@Composable
fun WeatherTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) {
        DarkWeatherColorScheme
    } else {
        LightWeatherColorScheme
    }

    val conditionColors = if (darkTheme) {
        DarkWeatherConditionColors
    } else {
        LightWeatherConditionColors
    }

    CompositionLocalProvider(
        LocalWeatherConditionColors provides conditionColors,
        LocalWeatherSpacing provides WeatherSpacing,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = WeatherTypography,
            shapes = WeatherMaterialShapes,
            content = content,
        )
    }
}

/**
 * Accessor for Weather World tokens that are not part of MaterialTheme.
 *
 * Example usage: WeatherTheme.conditionColors.sunny or
 * WeatherTheme.spacing.cardPadding.
 */
object WeatherTheme {
    val conditionColors: WeatherConditionColors
        @Composable
        @ReadOnlyComposable
        get() = LocalWeatherConditionColors.current

    val spacing: WeatherSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalWeatherSpacing.current

    val textStyles: WeatherTextStyles
        get() = WeatherTextStyles

    val shapes: WeatherShapes
        get() = WeatherShapes
}
