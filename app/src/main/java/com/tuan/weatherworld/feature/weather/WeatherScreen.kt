package com.tuan.weatherworld.feature.weather

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.tuan.weatherworld.R
import com.tuan.weatherworld.core.design.WeatherTheme

@Composable
fun WeatherScreen(
    cityName: String,
    temperature: Int,
    weatherCurrent: String,
    highTemperature: Int,
    lowTemperature: Int,
    onOpenLocations: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val rainyColors = WeatherTheme.conditionColors.rainy

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        rainyColors.backgroundStart,
                        rainyColors.backgroundEnd,
                    ),
                ),
            )
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(
                horizontal = WeatherTheme.spacing.screenHorizontal,
                vertical = WeatherTheme.spacing.screenVertical,
            ),
        contentAlignment = Alignment.Center,
    ) {
        TextButton(
            onClick = onOpenLocations,
            modifier = Modifier.align(Alignment.TopEnd),
        ) {
            Text(
                text = stringResource(R.string.weather_open_locations),
                color = rainyColors.content,
            )
        }

        WeatherDetails(
            cityName = cityName,
            temperature = temperature,
            weatherCurrent = weatherCurrent,
            highTemperature = highTemperature,
            lowTemperature = lowTemperature,
            contentColor = rainyColors.content,
        )
    }
}

@Composable
private fun WeatherDetails(
    cityName: String,
    temperature: Int,
    weatherCurrent: String,
    highTemperature: Int,
    lowTemperature: Int,
    contentColor: Color,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(WeatherTheme.spacing.space8),
    ) {
        Text(
            text = cityName,
            style = WeatherTheme.textStyles.city,
            color = contentColor,
        )
        Text(
            text = stringResource(R.string.weather_temperature_format, temperature),
            style = WeatherTheme.textStyles.temperature,
            color = contentColor,
        )
        Text(
            text = weatherCurrent,
            style = WeatherTheme.textStyles.sectionTitle,
            color = contentColor,
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(WeatherTheme.spacing.space8),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(
                    R.string.weather_high_temperature_format,
                    highTemperature,
                ),
                style = WeatherTheme.textStyles.bodyStrong,
                color = contentColor,
            )
            Text(
                text = stringResource(
                    R.string.weather_low_temperature_format,
                    lowTemperature,
                ),
                style = WeatherTheme.textStyles.bodyStrong,
                color = contentColor,
            )
        }
    }
}
