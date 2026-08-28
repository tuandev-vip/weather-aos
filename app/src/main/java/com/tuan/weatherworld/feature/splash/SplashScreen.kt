package com.tuan.weatherworld.feature.splash

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tuan.weatherworld.R
import com.tuan.weatherworld.core.design.WeatherTheme
import com.tuan.weatherworld.data.model.WeatherLocation
import kotlinx.coroutines.delay

private const val SplashDurationMillis = 1_200L

/**
 * Compose Splash quyết định location khởi động sau khi System Splash kết thúc.
 *
 * DataStore có location thì mở Weather ngay. Lần cài đầu, Screen giữ nhận diện
 * thương hiệu ngắn, xin quyền foreground rồi giao việc GPS/reverse geocoding cho
 * [SplashViewModel]. Screen chỉ phát callback; AppNavGraph quyết định destination.
 */
@Composable
fun SplashScreen(
    onLocationReady: (WeatherLocation) -> Unit,
    onLocationRequired: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
) {
    val sunnyColors = WeatherTheme.conditionColors.sunny

    val context = LocalContext.current
    val currentLocationState by viewModel.currentLocationState.collectAsStateWithLifecycle()
    val locationPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
        ) { permissions ->
            val hasCoarsePermission = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true

            val hasFinePermission = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true

            if (hasCoarsePermission || hasFinePermission) {
                viewModel.loadCurrentLocation()
            } else {
                viewModel.onLocationPermissionDenied()
            }
        }

    LaunchedEffect(Unit) {
        val selectedLocation = viewModel.getStartLocation()

        if (selectedLocation != null) {
            onLocationReady(selectedLocation)
            return@LaunchedEffect
        }

        delay(SplashDurationMillis)

        if (context.hasLocationPermission()) {
            viewModel.loadCurrentLocation()
        } else {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                ),
            )
        }
    }

    LaunchedEffect(currentLocationState) {
        when (val currentState = currentLocationState) {
            is CurrentLocationUiState.Success -> {
                onLocationReady(currentState.location)
            }

            is CurrentLocationUiState.Error -> {
                onLocationRequired()
            }

            CurrentLocationUiState.Idle,
            CurrentLocationUiState.Loading -> Unit
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        sunnyColors.backgroundStart,
                        sunnyColors.backgroundEnd,
                    ),
                ),
            )
            .statusBarsPadding()
            .navigationBarsPadding(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(WeatherTheme.spacing.space8),
        ) {
            Image(
                painter = painterResource(R.mipmap.ic_launcher_foreground),
                contentDescription = null,
                modifier = Modifier.size(150.dp),
            )

            Text(
                text = stringResource(R.string.app_name),
                style = WeatherTheme.textStyles.city,
                color = sunnyColors.content,
            )
        }
    }
}
private fun Context.hasLocationPermission(): Boolean {
    val hasCoarsePermission =
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        ) == PackageManager.PERMISSION_GRANTED

    val hasFinePermission =
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION,
        ) == PackageManager.PERMISSION_GRANTED

    return hasCoarsePermission || hasFinePermission
}
