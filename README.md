# Base de Campo

Aplicación móvil gamificada para el diseño de experimentos (Diseña tu Experimento).

Nativa Android (Kotlin + Jetpack Compose), sin conexión a internet, con persistencia
local en Room.

## Cómo compilar

Requiere Android SDK (compileSdk 37, minSdk 24) y JDK 17.

```
./gradlew assembleDebug
```

El APK queda en `app/build/outputs/apk/debug/`.

## Cómo correr los tests

```
./gradlew testDebugUnitTest
./gradlew lintDebug
```
