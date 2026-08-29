package com.tuan.weatherworld.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.tuan.weatherworld.core.common.AppResult
import com.tuan.weatherworld.core.error.AppError
import com.tuan.weatherworld.data.model.TemperatureUnit
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private val Context.settingsDataStore by preferencesDataStore(
    name = "weather_settings",
)

/**
 * Cài đặt [SettingsRepository] bằng Preferences DataStore.
 *
 * Lớp này chứa chi tiết lưu trữ; ViewModel chỉ phụ thuộc vào
 * [SettingsRepository] và không biết DataStore tồn tại.
 */
@Singleton
class SettingsRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context,
) : SettingsRepository {

    private val dataStore = context.settingsDataStore

    override val temperatureUnit: Flow<TemperatureUnit> =
        dataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }
            .map { preferences ->
                val savedValue = preferences[KEY_TEMPERATURE_UNIT]

                TemperatureUnit.entries.firstOrNull { unit ->
                    unit.name == savedValue
                } ?: TemperatureUnit.CELSIUS
            }

    override suspend fun saveTemperatureUnit(
        unit: TemperatureUnit,
    ): AppResult<Unit> {
        return try {
            dataStore.edit { preferences ->
                preferences[KEY_TEMPERATURE_UNIT] = unit.name
            }

            AppResult.Success(Unit)
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            AppResult.Error(
                error = AppError.StorageFailure,
                throwable = exception,
            )
        }
    }

    private companion object {
        val KEY_TEMPERATURE_UNIT =
            stringPreferencesKey("temperature_unit")
    }
}