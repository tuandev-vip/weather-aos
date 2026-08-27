# Weather World — Android

Weather World là dự án Android học tập về ứng dụng thời tiết, được xây dựng từng bước từ giao diện Jetpack Compose cơ bản đến dữ liệu thời tiết thật.

> Trạng thái hiện tại: **Commit 08 và Commit 09 đã hoàn thành** — app tìm kiếm địa điểm thật bằng Open-Meteo Geocoding, lưu favorites bằng Room, tự tải thời tiết cho danh sách đã lưu và mở Weather theo địa điểm người dùng chọn.

## Mục tiêu học tập

- Luyện xây dựng giao diện bằng Jetpack Compose và Material 3.
- Biết tách màn hình, component, màu sắc, typography và spacing thành các file phù hợp.
- Hiểu state, StateFlow và luồng dữ liệu một chiều trong Compose.
- Học MVVM, Repository và cách thay dữ liệu giả bằng API thật mà không viết lại UI.
- Làm quen dần với Coroutines, Hilt, DataStore, Room và testing.
- Tạo lịch sử Git gồm các commit nhỏ, rõ mục tiêu và dễ review.

## Package name

Package được chốt cho dự án:

```text
com.tuan.weatherworld
```

`namespace`, `applicationId` và package source hiện đều sử dụng tên này.

## Tech stack hiện tại

- Kotlin
- Jetpack Compose
- Material 3
- Navigation Compose
- AndroidX Core SplashScreen
- Hilt + KSP
- ViewModel + Coroutines + StateFlow
- Lifecycle-aware state collection
- Retrofit + Kotlinx Serialization
- Open-Meteo Forecast API
- Open-Meteo Geocoding API
- Room Database
- Gradle Kotlin DSL
- minSdk 26
- targetSdk 36
- compileSdk 36.1

Weather World lấy dự báo từ Open-Meteo qua `WeatherRepositoryImpl` và tìm tên địa điểm qua Geocoding API. Favorites được lưu bền vững bằng Room qua `SavedLocationRepository`; `LocationsViewModel` quan sát `Flow` từ Room rồi tải thời tiết cho từng địa điểm đã lưu. DataStore để nhớ địa điểm đang chọn và vị trí thiết bị trong lần cài đầu chưa được triển khai.

## Chạy project

### Android Studio

1. Mở thư mục `weather-aos` bằng Android Studio.
2. Chờ Gradle Sync hoàn thành.
3. Chọn emulator hoặc thiết bị Android API 26 trở lên.
4. Chọn cấu hình `app` và nhấn Run.

### Build bằng terminal trên Windows

```powershell
.\gradlew.bat :app:assembleDebug
```

APK debug được tạo trong:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## Lộ trình ngắn

```text
Project foundation
    → design system
    → Navigation + Splash
    → model + mock repository
    → Hilt + ViewModel + StateFlow
    → hoàn thiện UI bằng mock data
    → Weather API thật
    → tìm kiếm và thêm địa điểm
    → Room favorites
    → DataStore + current location
    → testing
```

Kế hoạch triển khai chính thức theo từng commit nằm trong [`ROADMAP.md`](ROADMAP.md). Nội dung học mở rộng nằm trong [`LEARNING_PLAN.md`](LEARNING_PLAN.md).

## Nguyên tắc kiến trúc

UI không biết chi tiết Retrofit hoặc Open-Meteo. Luồng hiện tại là:

```text
AppNavGraph
    ├── WeatherScreen → WeatherViewModel
    └── LocationsScreen → LocationsViewModel
                              ↓
                    WeatherRepository
                              ↓
                    WeatherRepositoryImpl
                              ↓
                    WeatherRemoteDataSource
                              ↓
                OpenMeteoWeatherRemoteDataSource
                              ↓
                         WeatherApi
                              ↓
             Retrofit + Kotlinx Serialization
                              ↓
                  OpenMeteoForecastDto
                              ↓
                         toDomain()
                              ↓
                           Weather
```

Luồng tìm kiếm, lưu và mở địa điểm hiện tại:

```text
LocationSearchScreen
→ LocationSearchViewModel
→ WeatherRepository.searchLocations()
→ WeatherRepositoryImpl
→ WeatherRemoteDataSource
→ GeocodingApi
→ OpenMeteoGeocodingResponseDto
→ toDomain()
→ List<WeatherLocation>
→ LocationSearchUiState
→ Compose hiển thị kết quả

Người dùng chọn một WeatherLocation
→ LocationSearchViewModel
→ SavedLocationRepository
→ SavedLocationRepositoryImpl
→ WeatherLocation.toEntity()
→ SavedLocationDao.insert()
→ Room
→ Added / AlreadyExists / Error
→ SavedLocationUiState
→ Added: AppNavGraph.openWeather(location)
→ Routes.weather(name, latitude, longitude)
→ WeatherViewModel đọc argument bằng SavedStateHandle
→ WeatherRepository tải thời tiết địa điểm vừa chọn

Room.saved_locations
→ SavedLocationDao.observeAll()
→ SavedLocationRepository.observeSavedLocations()
→ LocationsViewModel.collectLatest()
→ WeatherRepository.getLocationsWeather(locations)
→ LocationsUiState.Empty / Success / Error
→ LocationsScreen tự cập nhật danh sách card
```

`MockWeatherRepository` và `MockWeatherData` vẫn được giữ trong source để học/test, nhưng `RepositoryModule` runtime đã bind `WeatherRepository` sang `WeatherRepositoryImpl`.

Hilt chịu trách nhiệm tạo và nối dependency. Mapper giữ DTO của Open-Meteo ngoài UI và chuyển chúng thành domain model. Mỗi ViewModel quản lý `UiState` gồm Loading, Success và Error; Compose thu thập state theo lifecycle rồi tự cập nhật UI.

Navigation dùng route chuỗi và key tập trung theo cách tổ chức của SilverCare. `Routes` giữ route mẫu, tên argument và helper tạo route thật; `AppNavGraph` khai báo `navArgument`; `WeatherViewModel` nhận `SavedStateHandle`, đọc tên/kinh độ/vĩ độ rồi gọi Repository. Screen không đọc argument và không giữ `NavController`.

Hiện Splash vẫn mở Đà Nẵng như fallback phát triển. Bước tiếp theo là dùng Preferences DataStore lưu một địa điểm đang chọn, xin quyền vị trí ở lần cài đầu và dùng vị trí thiết bị khi chưa có lựa chọn trước đó. Room tiếp tục chỉ giữ danh sách favorites.

Route `SETTING` và `SettingScreen` hiện mới là khung điều hướng tạo sớm. Phần cài đặt thật vẫn thuộc Commit 10 trong roadmap.
