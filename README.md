# T-tasks

Android-клиент для работы с поездками: регистрация и вход, список и фильтры поездок, создание поездки, подробности, панель показателей и профиль. Используются Kotlin, Jetpack Compose, Room, Paging, Koin и Firebase.

## Сборка и запуск

Откройте корень проекта в Android Studio, установите Android SDK 36 и выполните синхронизацию Gradle. Используйте JDK, совместимый с версией Android Gradle Plugin проекта; настройки Java указаны в `app/build.gradle.kts`. Минимальная версия устройства — Android API 29.

Для сборки отладочного APK:

```sh
sh gradlew :app:assembleDebug
```

В Windows: `gradlew.bat :app:assembleDebug`. APK появится в `app/build/outputs/apk/debug/`. Для запуска выберите модуль `app` и устройство или эмулятор в Android Studio.

## Устройство проекта

Адрес сервера задаётся в `app/src/main/java/com/prod/t_tasks/common/Constants.kt` (`BASE_API_URL`). Для Firebase используйте конфигурацию своего проекта в `app/google-services.json`. Схемы локальной БД хранятся в `app/schemas/`, готовый демонстрационный APK — в `app-debug.apk`.
