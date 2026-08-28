package com.tuan.weatherworld.data.repository

import android.content.Context
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.tuan.weatherworld.data.model.WeatherLocation
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private val Context.selectedLocationDataStore by preferencesDataStore(
    name = "selected_location",
)

/**
 * Lưu đúng một địa điểm đang được chọn bằng Preferences DataStore.
 *
 * Dữ liệu này quyết định lần mở app sau Weather World hiển thị thời tiết ở đâu;
 * nó khác với danh sách nhiều địa điểm yêu thích do [SavedLocationRepository]
 * quản lý trong Room. Giá trị đọc ra luôn được kiểm tra tên và phạm vi tọa độ.
 */
@Singleton
class SelectedLocationRepository @Inject constructor(
    @ApplicationContext context: Context,
) {
    private val dataStore = context.selectedLocationDataStore

    val selectedLocation: Flow<WeatherLocation?> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val displayName =
                preferences[KEY_DISPLAY_NAME]?.takeIf { it.isNotBlank() } ?: return@map null

            val latitude = preferences[KEY_LATITUDE] ?: return@map null

            val longitude = preferences[KEY_LONGITUDE] ?: return@map null

            if (latitude !in -90.0..90.0 || longitude !in -180.0..180.0) {
                return@map null
            }

            WeatherLocation(
                displayName = displayName,
                latitude = latitude,
                longitude = longitude,
            )
        }

    suspend fun saveSelectedLocation(
        location: WeatherLocation,
    ): Result<Unit> {
        return try {
            require(location.displayName.isNotBlank()) { "Tên địa điểm không được để trống" }
            require(location.latitude in -90.0..90.0) { "Vĩ độ không hợp lệ" }
            require(location.longitude in -180.0..180.0) { "Kinh độ không hợp lệ" }

            dataStore.edit { preferences ->
                preferences[KEY_DISPLAY_NAME] = location.displayName
                preferences[KEY_LATITUDE] = location.latitude
                preferences[KEY_LONGITUDE] = location.longitude
            }

            Result.success(Unit)
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    suspend fun clearSelectedLocation(): Result<Unit> {
        return try {
            dataStore.edit { preferences ->
                preferences.remove(KEY_DISPLAY_NAME)
                preferences.remove(KEY_LATITUDE)
                preferences.remove(KEY_LONGITUDE)
            }

            Result.success(Unit)
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    private companion object {
        val KEY_DISPLAY_NAME = stringPreferencesKey("selected_location_name")
        val KEY_LATITUDE = doublePreferencesKey("selected_location_latitude")
        val KEY_LONGITUDE = doublePreferencesKey("selected_location_longitude")
    }
}
