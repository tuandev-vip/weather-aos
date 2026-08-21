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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tuan.weatherworld.R
import com.tuan.weatherworld.core.design.WeatherTheme
import com.tuan.weatherworld.data.model.Weather

@Composable
fun WeatherScreen(
    onOpenLocations: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WeatherViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    WeatherContent(
        state = state,
        onOpenLocations = onOpenLocations,
        modifier = modifier,
    )
}

@Composable
private fun WeatherContent(
    state: WeatherUiState,
    onOpenLocations: () -> Unit,
    modifier: Modifier,
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

        when (val currentState = state) {
            WeatherUiState.Loading -> {
                CircularProgressIndicator(
                    color = rainyColors.content,
                )
            }

            is WeatherUiState.Error -> {
                Text(
                    text = currentState.message,
                    color = rainyColors.content,
                )
            }

            is WeatherUiState.Success -> {
                WeatherDetails(
                    weather = currentState.weather,
                    contentColor = rainyColors.content,
                )
            }
        }
    }
}

@Composable
private fun WeatherDetails(
    weather: Weather,
    contentColor: Color,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(WeatherTheme.spacing.space8),
    ) {
        Text(
            text = weather.cityName,
            style = WeatherTheme.textStyles.city,
            color = contentColor,
        )
        Text(
            text = stringResource(R.string.weather_temperature_format, weather.temperature),
            style = WeatherTheme.textStyles.temperature,
            color = contentColor,
        )
        Text(
            text = weather.weatherCondition,
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
                    weather.highTemperature,
                ),
                style = WeatherTheme.textStyles.bodyStrong,
                color = contentColor,
            )
            Text(
                text = stringResource(
                    R.string.weather_low_temperature_format,
                    weather.lowTemperature,
                ),
                style = WeatherTheme.textStyles.bodyStrong,
                color = contentColor,
            )
        }
    }
}
