package com.tuan.weatherworld.di

import com.tuan.weatherworld.data.source.weather.remote.GeocodingApi
import com.tuan.weatherworld.data.source.weather.remote.WeatherApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val WEATHER_BASE_URL =
        "https://api.open-meteo.com/"

    private const val GEOCODING_BASE_URL =
        "https://geocoding-api.open-meteo.com/"

    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
        }
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        json: Json,
    ): Retrofit {
        return createRetrofit(
            baseUrl = WEATHER_BASE_URL,
            json = json,
        )
    }

    @Provides
    @Singleton
    fun provideWeatherApi(
        retrofit: Retrofit,
    ): WeatherApi {
        return retrofit.create(WeatherApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGeocodingApi(
        json: Json,
    ): GeocodingApi {
        return createRetrofit(
            baseUrl = GEOCODING_BASE_URL,
            json = json,
        ).create(
            GeocodingApi::class.java,
        )
    }


    private fun createRetrofit(
        baseUrl: String,
        json: Json,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(
                json.asConverterFactory(
                    "application/json".toMediaType(),
                ),
            )
            .build()
    }
}