# Weather World — Android

Weather World là dự án Android học tập về ứng dụng thời tiết, được xây dựng từng bước từ giao diện Jetpack Compose cơ bản đến dữ liệu thời tiết thật.

> Trạng thái hiện tại: **Commit 08 và Commit 09 đã hoàn thành; Commit 10 đang triển khai** — app đã lưu selected location bằng DataStore, lấy GPS, nối reverse geocoding và chuyển sang tìm kiếm thủ công khi người dùng từ chối quyền hoặc không lấy được vị trí; phần tên tỉnh/thành phố đang chờ kiểm thử trên thiết bị thật.

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
- Preferences DataStore
- Google Play services Location
- Android Geocoder cho reverse geocoding
- Foreground location permission (`COARSE`/`FINE`)
- Gradle Kotlin DSL
- minSdk 26
- targetSdk 36
- compileSdk 36.1

Weather World lấy dự báo từ Open-Meteo qua `WeatherRepositoryImpl` và tìm địa điểm theo chữ nhập qua Geocoding API. Favorites được lưu bền vững bằng Room qua `SavedLocationRepository`; `LocationsViewModel` quan sát `Flow` từ Room rồi tải thời tiết cho từng địa điểm đã lưu. `SelectedLocationRepository` dùng Preferences DataStore để nhớ đúng một địa điểm mặc định. Khi chưa có địa điểm đã lưu, Compose Splash xin quyền foreground location, `FusedDeviceLocationProvider` lấy tọa độ và `AndroidLocationNameResolver` dùng Android Geocoder để đổi tọa độ thành tên tỉnh/thành phố.

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
→ Added hoặc AlreadyExists: SelectedLocationRepository.saveSelectedLocation(location)
→ Preferences DataStore lưu location mặc định
→ SavedLocationUiState.Added(location)
→ AppNavGraph.openWeather(location)
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

Luồng khởi động và vị trí hiện tại đang triển khai trong Commit 10:

```text
System Splash
→ Compose Splash
→ SelectedLocationRepository đọc DataStore
    ├── Có selected location → mở Weather ngay
    └── Chưa có → kiểm tra/xin COARSE + FINE permission
          ├── Từ chối/GPS lỗi
          │       ↓
          │  LocationSearch bắt buộc
          │       ↓
          │  chọn location → Room + DataStore → Weather
          │
          └── Được cấp quyền
                     ↓
              DeviceLocationProvider
                     ↓
          FusedDeviceLocationProvider
                     ↓
               latitude/longitude
                     ↓
              LocationNameResolver
                     ↓
          AndroidLocationNameResolver
                     ↓
             tên tỉnh/thành phố
                     ↓
        lưu Room + lưu Preferences DataStore
                     ↓
             mở WeatherScreen
```

`MockWeatherRepository` và `MockWeatherData` vẫn được giữ trong source để học/test, nhưng `RepositoryModule` runtime đã bind `WeatherRepository` sang `WeatherRepositoryImpl`.

Hilt chịu trách nhiệm tạo và nối dependency. Mapper giữ DTO của Open-Meteo ngoài UI và chuyển chúng thành domain model. Mỗi ViewModel quản lý `UiState` gồm Loading, Success và Error; Compose thu thập state theo lifecycle rồi tự cập nhật UI.

Navigation dùng route chuỗi và key tập trung theo cách tổ chức của SilverCare. `Routes` giữ route mẫu, tên argument và helper tạo route thật; `AppNavGraph` khai báo `navArgument`; `WeatherViewModel` nhận `SavedStateHandle`, đọc tên/kinh độ/vĩ độ rồi gọi Repository. Screen không đọc argument và không giữ `NavController`.

Reverse geocoding đã được tách thành contract `LocationNameResolver` và implementation `AndroidLocationNameResolver`, được Hilt bind trong `DataSourceModule` rồi truyền vào `SplashViewModel`. Nếu Geocoder không tìm được tên, app vẫn có thể dùng tọa độ để tải dự báo và dùng tên dự phòng `Vị trí hiện tại`. Phần này đang chờ test trên thiết bị thật. Nếu người dùng từ chối quyền hoặc lấy GPS lỗi, `AppNavGraph` mở `LocationSearchScreen` ở chế độ bắt buộc, xóa Splash khỏi back stack và chỉ mở Weather sau khi người dùng chọn một địa điểm đã được lưu vào Room + DataStore.

Source Kotlin có KDoc tiếng Việt giải thích trách nhiệm và ranh giới giữa Screen, ViewModel, repository, data source, SDK, DTO, Room/DataStore và Hilt. `Routes`, `AppNavGraph` và `strings.xml` còn được chia thành section theo chức năng để dễ quét file. Comment tập trung vào quyết định không thể hiểu đầy đủ chỉ từ tên code, không diễn giải lại từng câu lệnh Compose.

Route `SETTING` và `SettingScreen` hiện mới là khung điều hướng tạo sớm. Phần cài đặt thật vẫn thuộc Commit 10 trong roadmap.
