package com.tuan.weatherworld.feature.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.tuan.weatherworld.R
import com.tuan.weatherworld.core.design.WeatherTheme

@Composable
fun SettingScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(
                horizontal = WeatherTheme.spacing.screenHorizontal,
                vertical = WeatherTheme.spacing.screenVertical,
            ),
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier.align(Alignment.TopStart),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                contentDescription = stringResource(R.string.navigation_back),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(WeatherTheme.spacing.space8),
        ) {
            Text(
                text = stringResource(R.string.weather_brand),
                style = WeatherTheme.textStyles.city,
                color = MaterialTheme.colorScheme.onBackground,
            )

        }
    }
}
