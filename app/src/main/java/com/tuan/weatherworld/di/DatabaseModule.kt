package com.tuan.weatherworld.di

import android.content.Context
import androidx.room.Room
import com.tuan.weatherworld.data.source.location.local.WeatherDatabase
import com.tuan.weatherworld.data.source.location.local.dao.SavedLocationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideWeatherDatabase(
        @ApplicationContext context: Context,
    ): WeatherDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = WeatherDatabase::class.java,
            name = WeatherDatabase.DATABASE_NAME,
        ).build()
    }

    @Provides
    fun provideSavedLocationDao(
        database: WeatherDatabase,
    ): SavedLocationDao {
        return database.savedLocationDao()
    }
}