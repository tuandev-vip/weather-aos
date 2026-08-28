package com.tuan.weatherworld.data.source.location.device

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation [DeviceLocationProvider] bằng Fused Location Provider của Google.
 *
 * Lớp tự kiểm tra quyền vị trí trước khi gọi SDK và chỉ trả [DeviceCoordinates];
 * kiểu `android.location.Location` không bị đẩy lên ViewModel hoặc UI.
 */
@Singleton
class FusedDeviceLocationProvider @Inject constructor(
    @param:ApplicationContext private val context: Context,
) : DeviceLocationProvider {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentCoordinates(): Result<DeviceCoordinates> {
        if (!hasLocationPermission()) {
            return Result.failure(
                SecurityException("Chưa được cấp quyền truy cập vị trí"),
            )
        }

        return try {
            val location = fusedLocationClient
                .getCurrentLocation(
                    Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                    null,
                )
                .await()

            if (location == null) {
                Result.failure(
                    IllegalStateException("Không thể lấy vị trí hiện tại"),
                )
            } else {
                Result.success(
                    DeviceCoordinates(
                        latitude = location.latitude,
                        longitude = location.longitude,
                    ),
                )
            }
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    private fun hasLocationPermission(): Boolean {
        val hasCoarseLocation =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            ) == PackageManager.PERMISSION_GRANTED

        val hasFineLocation =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ) == PackageManager.PERMISSION_GRANTED

        return hasCoarseLocation || hasFineLocation
    }
}
