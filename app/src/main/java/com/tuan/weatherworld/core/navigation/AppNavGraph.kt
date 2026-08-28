package com.tuan.weatherworld.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tuan.weatherworld.data.model.WeatherLocation
import com.tuan.weatherworld.feature.locations.LocationSearchScreen
import com.tuan.weatherworld.feature.locations.LocationsScreen
import com.tuan.weatherworld.feature.setting.SettingScreen
import com.tuan.weatherworld.feature.splash.SplashScreen
import com.tuan.weatherworld.feature.weather.WeatherScreen

/**
 * Nguồn sự thật duy nhất của luồng điều hướng giữa các màn hình Weather World.
 *
 * NavGraph giữ route và cách truyền [WeatherLocation]; từng Screen chỉ phát
 * callback sự kiện nên không phụ thuộc trực tiếp vào NavController.
 */
@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Routes.SPLASH,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        // ---------- Khởi động ----------
        composable(Routes.SPLASH) {
            SplashScreen(
                onLocationReady = { location ->
                    navController.openInitialWeather(location)
                },
                onLocationRequired = {
                    navController.navigate(
                        Routes.locationSearch(isRequired = true),
                    ) {
                        popUpTo(Routes.SPLASH) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
            )
        }

        // ---------- Thời tiết ----------
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

        // ---------- Địa điểm đã lưu và tìm kiếm ----------
        composable(Routes.LOCATIONS) {
            LocationsScreen(
                onAddLocation = {
                    navController.navigate(
                        Routes.locationSearch(),
                    ) {
                        launchSingleTop = true
                    }
                },
                onLocationSelected = navController::openWeather

            )
        }

        composable(
            route = Routes.LOCATION_SEARCH_ROUTE,
            arguments = listOf(
                navArgument(Routes.ARG_LOCATION_REQUIRED) {
                    type = NavType.BoolType
                    defaultValue = false
                },
            ),
        ) { backStackEntry ->
            val isLocationRequired =
                backStackEntry.arguments?.getBoolean(Routes.ARG_LOCATION_REQUIRED) ?: false

            LocationSearchScreen(
                onBack = if (isLocationRequired) { null }
                else { { navController.popBackStack() } },
                onLocationAdded = navController::openWeather,
            )
        }

        // ---------- Cài đặt ----------
        composable(Routes.SETTING) {
            SettingScreen(onBack = navController::popBackStack)
        }
    }
}

// ---------- Helper điều hướng dùng chung ----------

/** Mở Weather lần đầu và xóa Splash khỏi back stack. */
private fun NavHostController.openInitialWeather(
    location: WeatherLocation,
) {
    navigate(
        Routes.weather(
            displayName = location.displayName,
            latitude = location.latitude,
            longitude = location.longitude,
        )
    ) {
        popUpTo(Routes.SPLASH) {
            inclusive = true
        }
        launchSingleTop = true
    }
}

/** Mở Weather của địa điểm mới và loại Weather cũ khỏi back stack. */
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
