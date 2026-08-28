# Bitácora de compilación — Base de Campo

## Parte 1: scaffolding y dominio

- `./gradlew clean testDebugUnitTest`: BUILD SUCCESSFUL, **30 tests, 0 fallos**
  (verificado desde estado limpio, sumando los reportes XML reales de cada motor).
  Nota: la ficha resume "MotorProgreso (6 tests)" y un total de 29, pero el código real de
  `MotorProgresoTest` tiene 7 pruebas (todas válidas, ninguna duplicada) — el total real
  es 5+6+4+8+7 = 30, no 29. Se documenta aquí en vez de forzar el número o borrar una prueba
  real para que cuadre con el resumen de la ficha.
- `./gradlew lintDebug`: BUILD SUCCESSFUL, sin errores.
- Motores (`domain/engine/`, 30 tests): `MotorInstrumentos` (5), `MotorSecuencia` (6, con
  validación por precedencia en vez de orden exacto), `MotorRepeticiones` (4),
  `MotorPlanExperimento` (8, orquestador), `MotorProgreso` (7, insignias calculables desde
  historial + racha).
- Dos correcciones reales encontradas al compilar (no estaban en el plan tal como estaba
  escrito):
  1. **Versión de KSP.** El plan fijaba `2.4.0-2.0.1`, que no existe en ningún repositorio de
     plugins. Se corrigió a `2.3.10`, la misma versión que ya usa Numerópolis (app real ya
     entregada del mismo lote) junto a Kotlin 2.4.0.
  2. **Tema del manifiesto.** El plan usaba `@style/Theme.Material3.DayNight.NoActionBar`, que
     no existe porque el proyecto no depende de `com.google.android.material:material` (solo de
     Compose Material3, que no expone temas XML). Se corrigió con un tema local propio
     (`Theme.BaseDeCampo`, extendiendo `android:Theme.Material.Light.NoActionBar`), el mismo
     patrón que ya usa Numerópolis.
  3. **Carpeta del proyecto.** El plan usaba `66-BaseDeCampo/` como raíz; se usó `base-de-campo/`
     (kebab-case, sin número) para coincidir con la convención real de las 7 apps del lote ya
     entregadas.
  4. No había `gradle` instalado globalmente en la máquina, así que el wrapper de Gradle 9.3.1
     se copió de Numerópolis (mismo proyecto, misma versión fija de la sección 7 del maestro)
     en vez de generarse con `gradle wrapper`.
- Icono de lanzador: versión inicial ya dibujada en Canvas/vector (brújula abierta, paleta de
  la ficha) — se revisa de nuevo en la Parte 3 junto con el resto del arte.
- Sin UI todavía más allá del placeholder, sin Room todavía — eso es la Parte 2 y 3.

## Parte 2: Room y datos semilla

- `./gradlew clean testDebugUnitTest`: BUILD SUCCESSFUL, **54 tests, 0 fallos** (30 de la
  Parte 1 + 6 de `MotorRepaso` + 10 de `AppDatabaseTest` + 8 de `ExpedicionRepositoryTest`).
  Nota: el plan de la Parte 2 preveía "11 tests" para `AppDatabaseTest`, pero el bloque de
  código que el propio plan especifica solo contiene 10 métodos `@Test` (verificado con
  `grep -c '@Test'`); se implementó tal cual venía en el plan, sin inventar un test nº 11.
  Por una coincidencia de dos desvíos que se compensan (Parte 1 real 30 en vez de 29, y esta
  Parte 2 real 10 en vez de 11), el total final de 54 sí coincide con lo que el plan preveía.
- `./gradlew lintDebug`: BUILD SUCCESSFUL, sin errores.
- Persistencia real con Room 2.8.4, probada con Robolectric 4.16.1 sobre una base de datos en
  memoria (`@Config(sdk = [34])` — SDK 37 del proyecto todavía no tiene soporte estable en
  Robolectric; ver Task 1 del plan de la Parte 2 para el detalle completo de por qué se eligió
  Robolectric sobre test instrumentado o `BundledSQLiteDriver`).
- 8 tablas (las 7 de la ficha + `repaso_pendiente`, agregada para la repetición espaciada
  obligatoria de la sección 5.3 del maestro).
- Datos semilla reales: 8 expediciones, 36 pasos de procedimiento, 7 instrumentos, 11 insignias
  (verificado por conteo directo, no de memoria).
- `MotorRepaso` (6 tests): repetición espaciada, motor nuevo de esta Parte, no modifica nada de
  la Parte 1.
- `ExpedicionRepository` (8 tests): compone Room con `MotorPlanExperimento` y `MotorProgreso` ya
  construidos en la Parte 1.
- Grep de higiene (nombres de herramientas IA) y verificación de identidad de git: limpio en
  ambos casos.
- Sin UI todavía — eso es la Parte 3.

## Parte 3: tema, pantallas y navegación

- `./gradlew clean testDebugUnitTest`: BUILD SUCCESSFUL, **64 tests, 0 fallos** (59 del plan
  original — 54 de las Partes 1+2 + 2 de `HomeScreenTest` + 3 de `ExpedicionScreenTest` — más 5
  agregados fuera del plan: 2 de `ExpedicionRepository.obtenerInsignias()` y 3 de la nueva
  `CuadernoScreen`, ver más abajo).
- `./gradlew lintDebug`: BUILD SUCCESSFUL, sin errores.
- `./gradlew assembleDebug`: BUILD SUCCESSFUL, APK real generado e instalado en un emulador.
- **`CuadernoScreen` (3 tests) + `ExpedicionRepository.obtenerInsignias()` (2 tests)**: fuera del
  plan de la Parte 3, que dejaba explícitamente el Cuaderno de Planes "pantalla propia pendiente"
  y el botón `onCuadernoClick` de `HomeScreen` sin acción. Se construyó siguiendo el mismo patrón
  sugerido por el propio plan ("mismo patrón que `ExpedicionScreen`, listando expediciones con su
  historial") — lista las 8 expediciones con su estado y las 11 insignias con su estado real
  (obtenida/pendiente, leído de Room, nunca inventado), y conecta el botón que antes no hacía
  nada.
- Tema con contraste WCAG verificado por cálculo (ver plan) — Secundario/blanco pasa justo en
  el límite (4.50), Acento nunca es texto sobre el fondo claro.
- Las 8 expediciones comparten `ExpedicionScreen`, una sola pantalla parametrizada — no 8.
- Arrastre real (`ZonaSoltar`/`FichaArrastrable`), no selección de opción múltiple.
- **Arte Canvas completo**, construido en varias pasadas después de que la Parte 3 ya compilaba:
  - `IconoExpedicion.kt`: 8 pictogramas (uno por expedición), silueta de un trazo, color de
    trazo que cambia al sellar (Primario → Acento). Usado en `HomeScreen` y `CuadernoScreen`.
  - `IlustracionInstrumento.kt`: 7 fichas de instrumento con degradado + curvas + sombra de
    contacto. Usado en `ExpedicionScreen` (arrastre real).
  - `FondoExpedicion.kt`: los 8 fondos de expedición de la sección 2 de la especificación (cielo,
    siluetas lejanas, mesa de campamento, objeto central propio de cada expedición, luz de
    fogata) — 5 capas, wireado como fondo real de `ExpedicionScreen`.
  - `Insignias.kt`: las 11 insignias de la sección 3 (círculo + anillo + símbolo propio,
    atenuadas cuando no están obtenidas) — wireadas en la nueva `CuadernoScreen`.
  - `IlustracionTuco.kt`: Tuco vía archivo real (no Canvas, por decisión explícita de la sección 1
    de la especificación) — las 8 poses de la hoja de personaje ya validada en
    `Downloads/personajes/` (ver sección 0.1 del handoff) se recortaron con Python/Pillow (fondo
    removido por distancia de color, recorte al contenido,
    ≤1024px, WebP calidad 85) a `res/drawable-nodpi/tuco_<pose>.webp`. Wireado en `OnboardingScreen`
    (una pose por pantalla) y en el resultado de `ExpedicionScreen` (celebrando/confundido).
  - **Nota honesta sobre el grep de verificación de la sección 5 de la especificación de arte**:
    el checklist pide "≥17 composables `Ilustracion*`/`Insignia*`" contando declaraciones `fun`.
    Esta implementación usa 3 composables públicos parametrizados (`IlustracionInstrumento`,
    `Insignia`, `IconoExpedicion`, más `IlustracionTuco` y `FondoExpedicion` sin ese prefijo) que
    despachan internamente a funciones privadas de `DrawScope` por variante — arquitectura
    deliberada para no repetir el boilerplate compartido (círculo+anillo, cielo+mesa+fogata) 35
    veces. El grep literal de nombres da 3, no ≥17. El conteo real de detalle
    (`grep -rEoc 'Brush\.(linear|radial|sweep)Gradient|\.shadow\(|cubicTo|quadraticTo'` sobre
    `ui/art/`) da **68**, muy por encima del umbral — el criterio de fondo (suficiente detalle
    real, no plano) sí se cumple; el criterio literal de conteo de nombres, no. Documentado aquí
    en vez de renombrar funciones artificialmente solo para pasar el grep.
- 5 correcciones reales encontradas al ejecutar el plan literal (no estaban en el texto tal
  como estaba escrito):
  1. **`androidx.compose.material:material-icons-core:1.9.6`** con versión fija no resuelve
     (no existe ese artefacto con ese número exacto en los repositorios). Corregido a dejar que
     el Compose BOM `2026.08.00` fije la versión (sin número explícito), igual que el resto de
     dependencias de Compose.
  2. **Import roto en `Arrastre.kt`**: el plan no importaba `boundsInWindow` (extensión de
     `LayoutCoordinates`) ni `Modifier.offset` (de `androidx.compose.foundation.layout`), pese a
     usarlos — el archivo tal cual no compilaba. Se agregaron ambos imports.
  3. **Import erróneo en `HomeScreen.kt`**: `import androidx.compose.foundation.layout.weight`
     explícito choca con una propiedad interna (`RowColumnParentData.weight`) en el Compose BOM
     `2026.08.00` — `Modifier.weight()` es una función miembro de `ColumnScope`/`RowScope`, no
     hace falta importarla aparte dentro de un bloque `Column{}`/`Row{}`. Se quitó el import.
  4. **`ExpedicionScreen` sin scroll**: el `Column` raíz no tenía `verticalScroll`, así que en
     una pantalla real (o en el entorno de pruebas de Robolectric) el botón "Sellar plan" y el
     contador de repeticiones quedaban fuera del viewport visible — dos tests fallaban porque el
     click no llegaba a un nodo fuera de pantalla, aunque el nodo existía. Se agregó
     `.verticalScroll(rememberScrollState())`, con `.performScrollTo()` en los tests
     correspondientes. Esto además es una corrección real de accesibilidad (sección 6 del
     maestro: la app debe seguir siendo usable con el tamaño de fuente del sistema al máximo,
     que haría esta pantalla todavía más alta).
  5. **Manifiesto**: el plan pedía volver a `@style/Theme.Material3.DayNight.NoActionBar` al
     registrar `BaseDeCampoApplication` — se mantuvo `Theme.BaseDeCampo` (la corrección real ya
     aplicada en la Parte 1, ver más arriba), solo se agregó `android:name`.

### Paso 4 de la Task 9 (ciclo real jugado en un emulador) — bloqueado por infraestructura, no por el código

**Esto es honesto, no un "pasa igual": el ciclo real completo (arrastrar instrumento, ordenar
pasos, sellar un plan) todavía NO se jugó de punta a punta en un dispositivo.** Se intentó con
los dos AVD disponibles en esta máquina (`fabrica34`, `fabrica_test`), con resultados:

- `fabrica34`: arranca, pero `System UI` entra en ANR persistente ("System UI isn't responding")
  antes de que la propia app llegue a mostrarse, incluso esperando varios minutos y tocando
  "Wait" repetidas veces.
- `fabrica_test`: arranca y el APK se **instaló e inició correctamente una vez** (tras un
  `adb reboot` en caliente), pero el `system_server` del emulador se cae poco después de
  cualquier interacción real (`PackageManagerInternal` nulo en `StorageManagerService`, luego
  "Can't find service: package"/"activity") — se confirmó que no es un problema de la app
  (ocurre incluso solo con `pm list packages`, sin tocar `pe.appmobile.basedecampo`).

Ambos síntomas apuntan a inestabilidad del propio emulador en esta máquina/entorno (probablemente
virtualización anidada), no a un bug de Base de Campo — la única corrida donde el sistema estuvo
sano por completo confirmó APK instalable y `MainActivity` arrancable sin excepción.
**Queda pendiente**: repetir este paso en un dispositivo físico o un entorno con emulador estable
antes de dar la Fase 1 por cerrada — la sección 10.3 del maestro es explícita en que ningún test
automatizado lo reemplaza, así que no se marca como hecho aunque los 59 tests y el `assembleDebug`
sí estén en verde.
