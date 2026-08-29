package com.tuan.weatherworld.feature.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tuan.weatherworld.R
import com.tuan.weatherworld.core.design.WWScreenScaffold
import com.tuan.weatherworld.core.design.WeatherTheme
import com.tuan.weatherworld.core.ui.UiText
import com.tuan.weatherworld.core.ui.asString
import com.tuan.weatherworld.data.model.TemperatureUnit

/**
 * Placeholder của màn cài đặt trong Commit 10.
 * Hiện màn chỉ kiểm tra route và callback quay lại; lựa chọn Celsius/Fahrenheit
 * cùng DataStore cài đặt chưa được triển khai.
 */
@Composable
fun SettingScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    WWScreenScaffold(
        title = stringResource(R.string.title_setting),
        onIconBack = onBack,

    ) { paddingValues ->

        SettingContent(
            temperatureUnit = uiState.temperatureUnit,
            errorMessage = uiState.errorMessage,
            modifier = Modifier.padding(paddingValues)

        )

    }
}
@Composable
fun SettingContent(
    temperatureUnit: TemperatureUnit,
    errorMessage: UiText?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(
                horizontal = WeatherTheme.spacing.screenHorizontal,
                vertical = WeatherTheme.spacing.screenVertical,
            ),

        ) {


        errorMessage?.let { message ->
            Text(
                text = message.asString(),
                color = MaterialTheme.colorScheme.error,
            )
        }
        Text(
            text = stringResource(R.string.title_unit),
            style = WeatherTheme.textStyles.cardTitle,
            color = MaterialTheme.colorScheme.primary,
        )
        SettingCard(
            temperatureUnit = temperatureUnit,
        )
    }
}

@Composable
fun SettingCard(
    temperatureUnit: TemperatureUnit
) {
    val temperatureValue = when (temperatureUnit) {
        TemperatureUnit.CELSIUS -> stringResource(R.string.setting_temperature_celsius)
        TemperatureUnit.FAHRENHEIT -> stringResource(R.string.setting_temperature_fahrenheit)
    }

    Column(
        modifier = Modifier.fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.onBackground, shape = WeatherTheme.shapes.card)
            .padding(WeatherTheme.spacing.space16),
        verticalArrangement = Arrangement.spacedBy(
            WeatherTheme.spacing.space8,
        ),
    ) {

        UnitItem(stringResource(R.string.setting_temperature_title), temperatureValue)
        UnitItem(stringResource(R.string.setting_temperature_title), temperatureValue)


    }
}

@Composable
fun UnitItem(
    titleItem: String,
    valueItem: String,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(WeatherTheme.spacing.cardPadding),
            verticalArrangement = Arrangement.spacedBy(WeatherTheme.spacing.space16),
        ) {
            Text(
                text = titleItem,
                style = WeatherTheme.textStyles.cardTitle,
                color = MaterialTheme.colorScheme.tertiary,
            )

            Text(
                text = valueItem,
                style = WeatherTheme.textStyles.label,
                color = MaterialTheme.colorScheme.primaryFixed,
            )
        }
        HorizontalDivider(thickness = 1.dp)
    }
}
