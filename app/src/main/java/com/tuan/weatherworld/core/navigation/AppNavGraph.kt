package com.tuan.weatherworld.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tuan.weatherworld.feature.locations.LocationsScreen
import com.tuan.weatherworld.feature.setting.SettingScreen
import com.tuan.weatherworld.feature.splash.SplashScreen
import com.tuan.weatherworld.feature.weather.WeatherScreen

/** Single source of truth for navigation between Weather World's destinations. */
@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Routes.SPLASH,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        composable(Routes.SPLASH) {
            SplashScreen(
                onFinished = {
                    navController.navigate(Routes.WEATHER) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                        launchSingleTop = true
                    }
                },
            )
        }

        composable(Routes.WEATHER) {
            WeatherScreen(
                onOpenLocations = {
                    navController.navigate(Routes.LOCATIONS) {
                        launchSingleTop = true
                    }
                },

                onOpenSetting = {
                    navController.navigate(Routes.SETTING) {
                        launchSingleTop = true
                    }
                },
            )
        }

        composable(Routes.LOCATIONS) {
            LocationsScreen(
                onAddLocations = {
                    // Search and add behavior is implemented in Commit 08.
                },
            )
        }

        composable(Routes.SETTING) {
            SettingScreen(onBack = navController::popBackStack)
        }
    }
}
