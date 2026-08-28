package com.tuan.weatherworld

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.tuan.weatherworld.core.design.WeatherTheme
import com.tuan.weatherworld.core.navigation.AppNavGraph
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activity duy nhất: cài System Splash, bật edge-to-edge rồi dựng cây Compose.
 * `@AndroidEntryPoint` kết nối Activity và các ViewModel của NavGraph với Hilt.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherTheme {
                AppNavGraph()
            }
        }
    }
}
