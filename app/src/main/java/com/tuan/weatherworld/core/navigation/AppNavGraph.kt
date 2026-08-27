package com.tuan.weatherworld.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tuan.weatherworld.data.location.DefaultWeatherLocations
import com.tuan.weatherworld.data.model.WeatherLocation
import com.tuan.weatherworld.feature.locations.LocationSearchScreen
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
            val defaultLocation = DefaultWeatherLocations.daNang
            SplashScreen(
                onFinished = {
                    navController.navigate(
                        Routes.weather(
                            displayName = defaultLocation.displayName,
                            latitude = defaultLocation.latitude,
                            longitude = defaultLocation.longitude
                        )
                    ) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                        launchSingleTop = true
                    }
                },
            )
        }

        composable(
            route = Routes.WEATHER_ROUTE,
            arguments = listOf(
                navArgument(Routes.ARG_LOCATION_NAME) { type = NavType.StringType },
                navArgument(Routes.ARG_LOCATION_LATITUDE) { type = NavType.StringType },
                navArgument(Routes.ARG_LOCATION_LONGITUDE) { type = NavType.StringType },
            ),
        ) {
            WeatherScreen(
                onOpenLocations = {
                    navController.navigate(Routes.LOCATIONS) { launchSingleTop = true }
                },

                onOpenSetting = {
                    navController.navigate(Routes.SETTING) { launchSingleTop = true }
                },

            )
        }

        composable(Routes.LOCATIONS) {
            LocationsScreen(
                onAddLocation = {
                    navController.navigate(Routes.LOCATION_SEARCH) {
                        launchSingleTop = true
                    }
                },
                onLocationSelected = navController::openWeather

            )
        }

        composable(Routes.SETTING) {
            SettingScreen(onBack = navController::popBackStack)
        }

        composable(Routes.LOCATION_SEARCH) {
            LocationSearchScreen(
                onBack = navController::popBackStack,
                onLocationAdded = navController::openWeather,
            )
        }
    }
}
private fun NavHostController.openWeather(
    location: WeatherLocation,
) {
    navigate(
        Routes.weather(
            displayName = location.displayName,
            latitude = location.latitude,
            longitude = location.longitude,
        ),
    ) {
        popUpTo(Routes.WEATHER_ROUTE) {
            inclusive = true
        }
        launchSingleTop = true
    }
}
