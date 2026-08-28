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

Ambos síntomas apuntan a inestabilidad del propio emulador en esta máquina/entorno, no a un bug
de Base de Campo.

### Paso 4 — completado en una sesión posterior, siguiendo la receta de la sección 13.3 del maestro

La causa real de la inestabilidad: el AVD `fabrica34` (API 34, el correcto, ver 13.3) tenía
estado corrupto por los varios `adb emu kill` forzados de la sesión anterior, y además se estaba
arrancando sin los flags que la sección 13.3 ya documenta como necesarios
(`-no-window -no-audio -gpu swiftshader_indirect`) ni con el chequeo de estabilidad real
(`topResumedActivity` x5 seguidos, no solo `boot_completed=1`). Con `-wipe-data` + la receta
completa de 13.3, el emulador arrancó sano. **El ciclo real se jugó de punta a punta en el AVD
`fabrica34`** (arrastrar el termómetro a la mesa, confirmar la secuencia de pasos ya correcta,
subir las repeticiones a 3, sellar el plan de "El Frío de la Puna", ver el mensaje de éxito con
Tuco celebrando, volver al Home y confirmar que el poste correspondiente cambia a color Acento).

**Se encontraron y corrigieron 5 bugs reales que ningún test automatizado había atrapado** —
exactamente el escenario que la sección 10.3 anticipa:

1. **`runBlocking` en `MainActivity.onCreate()` bloqueaba el hilo principal** esperando a Room
   (que además puede tener que crear el archivo de la base de datos la primera vez) antes de
   dibujar el primer frame — causa clásica de ANR en frío, y se reprodujo como tal en el
   emulador. Corregido: `esPrimerLanzamiento` se resuelve de forma asíncrona con
   `LaunchedEffect` dentro de `setContent`, sin bloquear `onCreate`.
2. **`SecuenciaDePasos` (Row de pasos a ordenar) no tenía `horizontalScroll` y sus fichas usaban
   `widthIn(min = ...)` sin tope** — el primer paso (el de texto más largo) consumía todo el
   ancho disponible del Row y los pasos 2, 3 y 4 ni siquiera llegaban a componerse (confirmado
   con `uiautomator dump`: solo existía un nodo "Paso 1" en todo el árbol). Corregido: ancho fijo
   por ficha (`140.dp`) + `Modifier.horizontalScroll(rememberScrollState())`.
3. **El mismo bug, en el Row de instrumentos para arrastrar** (`SelectorInstrumento`): con 6
   instrumentos por mostrar (7 menos el ya elegido), solo entraban 4 en pantalla y el resto no
   se renderizaba. Misma corrección: `horizontalScroll` agregado al Row.
4. **`FichaArrastrable.onDragEnd` duplicaba el offset del arrastre**: `onGloballyPositioned`
   estaba *dentro* del modificador `.offset{}` en la cadena, así que `posicionPropia` ya incluía
   el desplazamiento actual del dedo — y el código sumaba `offset` una segunda vez al calcular el
   centro final, con lo que el soltado casi nunca caía dentro de la zona real (jugando a mano, el
   arrastre del termómetro no llegaba nunca al slot). Corregido moviendo `onGloballyPositioned`
   *fuera* de `.offset{}` (reporta la posición de reposo, estable) y sumando el offset una sola
   vez — diseño más robusto además, porque no depende de que la medición esté perfectamente al
   día en cada frame de un arrastre rápido.
5. **`HomeViewModel` nunca se refrescaba al volver de una expedición**: el Home vive en la base
   del back stack de Navigation Compose, así que volver con "atrás" desde `ExpedicionScreen`
   reutiliza la misma instancia del ViewModel (no se recrea, no se vuelve a ejecutar su `init`) —
   el poste recién sellado se quedaba con su color viejo hasta cerrar y reabrir la app entera.
   Corregido con `LifecycleResumeEffect` en el composable de la ruta `HOME`, que llama a
   `viewModel.recargar()` cada vez que la pantalla vuelve a estado RESUMED (ya existía el método
   `recargar()` en el ViewModel, escrito en la Parte 3 original, pero nunca se invocaba desde
   ningún lado).

Ninguno de estos 5 bugs lo atraparon los 64 tests automatizados (los tests de pantalla prueban
`HomeScreen`/`ExpedicionScreen` de forma aislada, con un `uiState` fijo pasado a mano, no a través
de navegación real con un ViewModel vivo) — es exactamente el tipo de bug que la sección 10.3
documenta como invisible a los tests. Con los 5 corregidos, el ciclo completo funciona de
principio a fin en un dispositivo real, no solo en Robolectric.

### Corrección adicional tras releer la sección 15: no combinar arrastre con scroll

El arreglo del bug 2 de arriba usó primero `Modifier.horizontalScroll` en las filas de pasos e
instrumentos — funcionaba, pero la sección 15 prohíbe explícitamente esa combinación ("todo entra
o es alcanzable... sin combinar arrastre con scroll"), porque un swipe horizontal sobre una ficha
arrastrable es ambiguo entre "arrastrar" y "hacer scroll". Se reemplazó por `FlowRow` (envuelve a
la siguiente línea dentro del scroll vertical que la pantalla ya tiene, sin scroll horizontal
propio) — mismo patrón que ya usa `HomeScreen` para sus 8 iconos. La lógica de reordenar por
arrastre no cambió: sigue siendo por índice de la lista, no por posición visual en pantalla, así
que el resultado es igual de correcto con el contenido envuelto en varias líneas.

## Lista de verificación final (sección 15) — repasada sobre el resultado final

- [x] La mecánica principal se resuelve interactuando (arrastre real + reordenar + contador), no
  eligiendo opciones — confirmado jugando el ciclo real en emulador.
- [x] Ninguna pantalla principal es solo título + párrafo + botones (Home y Expedición tienen
  fondo Canvas, iconos/fichas ilustrados, interacción real).
- [x] Ninguna captura parece app bancaria o formulario administrativo.
- [x] Hay algo que descubrir (8 fondos distintos), coleccionar (11 insignias, Cuaderno de Planes)
  y una razón para volver (racha, repetición espaciada de `MotorRepaso`).
- [x] Todas las funciones prometidas tienen lógica y persistencia reales — Room real, sin datos
  inventados en memoria.
- [x] El *parental gate* funciona (gesto de mantener presionado 3 segundos, sin pregunta
  aritmética) y detrás solo hay ajustes + progreso real leído de Room.
- **[~] "El primer reto se resuelve por descubrimiento, sin bloque de texto que explique la
  mecánica" (sección 5.5)**: `ExpedicionScreen` sí tiene encabezados de texto explícitos
  ("Arrastra el instrumento correcto a la mesa", "Ordena los pasos arrastrándolos") sobre cada
  sub-mecánica. Se documenta como desvío consciente, no como algo resuelto: la ficha no da
  ninguna alternativa visual para comunicar "esto se arrastra" sin texto (a diferencia de, por
  ejemplo, un resaltado o animación de sugerencia), y con 8 expediciones distintas cada una con
  3 sub-mecánicas, quitar el texto sin reemplazarlo por otra señal visual arriesgaba dejar al
  niño sin ninguna pista. Queda como mejora pendiente, no como checklist marcado a la fuerza.
- [x] Datos semilla cumplen las cantidades de la ficha (8 expediciones, 7 instrumentos, 36 pasos,
  11 insignias — verificado por conteo directo en `SeedData.kt`, no de memoria).
- **[~] Ilustraciones mínimas de la sección 4 (17+, comando de la 4.0)**: el grep literal de
  composables `Ilustracion*`/`Insignia*` da 3, no 17+ (ver nota de la Parte 3 más arriba — 3
  composables públicos parametrizados en vez de uno por variante, con 68 construcciones reales
  de degradado/sombra/curva). Documentado como desvío consciente, no resuelto a la fuerza.
- [x] El español es natural y los errores se explican ("Ese instrumento no mide lo que esta
  expedición necesita", no un genérico "Error").
- [x] Las versiones son las fijadas en la sección 7, con las correcciones reales documentadas
  arriba (KSP, BOM de Compose sin versiones sueltas).
- [x] `domain/` se prueba sin UI (30 tests, JVM puro) · Room real (`AppDatabase`, sin SQL en
  Composables) · sin datos inventados en la UI.
- [x] Hay más de 20 `@Test` y todos pasan — **68 tests, 0 fallos** (verificado desde `clean`).
- [x] Cada pantalla alcanzable tiene su test de Compose que la renderiza de verdad — Home,
  Expedición, Cuaderno, Onboarding y Parental Gate, las 5 pantallas navegables.
- [x] Se jugó al menos un ciclo real y completo de la mecánica principal sobre el APK compilado
  en un emulador real (ver Paso 4 más arriba) — no solo en Robolectric.
- [x] Cualquier fila cuya cantidad de elementos depende de los datos se probó con el caso más
  exigente (5 pasos, 7 instrumentos) y todo es alcanzable sin combinar arrastre con scroll
  (`FlowRow`, ver corrección de arriba).
- [x] Sin permiso `INTERNET` en el manifiesto — funciona en modo avión por diseño (toda la
  persistencia es Room local).
- [x] Objetivos táctiles ≥ 48dp (fichas de 100-140dp, botones de 56-120dp), `contentDescription`
  presente en todo elemento interactivo, todos los textos en `strings.xml`.
- [x] `versionName` (`1.0.0`) coincide con el nombre del APK (`BaseDeCampo.v1.0.0.apk`).
- [x] El ícono de lanzador está conectado — `aapt2 dump badging` sobre el APK final muestra
  `application-icon-*` reales (verificado tras la recompilación final, no antes).
- [x] No aplica motor de señal continua (sin audio/sensores en esta app).
- [x] El emulador se cerró limpio al terminar (`adb emu kill` + verificación de que no quedan
  procesos `qemu-system*`).

**Carpeta lista para entregar (fin de la Fase 1)**
- [x] `grep` de herramientas de IA no devuelve nada (repetido tras el hallazgo y corrección de la
  Parte 3 — ver nota de higiene más arriba).
- [x] `git log` muestra solo `dantealigueri21w <320279109+dantealigueri21w@users.noreply.github.com>`.
- [ ] **Pendiente**: dejar la carpeta sin `.gradle/`, `build/`, `local.properties` — no se hizo
  todavía porque la Fase 2 (capturas reales, memoria descriptiva) todavía necesita compilar y
  ejecutar la app desde esta misma carpeta; se hace recién al empaquetar la entrega final.
- [x] La memoria descriptiva y el manual no están dentro de la carpeta del proyecto (Fase 2
  todavía no se generó).
- [x] `BUILD_REPORT.md` (este archivo) tiene salidas reales en cada paso, incluidos los 5 bugs
  reales encontrados y corregidos jugando de verdad — nada inventado.

**Total final: 68 tests, 0 fallos · `lintDebug` limpio · `assembleDebug` en verde · APK firmado y
verificado (`apksigner verify` exit 0) · ciclo real jugado y confirmado en `fabrica34` (API 34).**

## Corrección del arrastre y cierre de Fase 2 (28/08/2026)

Jugando el ciclo real ya con la app instalada (no solo en Robolectric) aparecieron dos problemas
más, encontrados y corregidos después del cierre inicial de arriba:

1. **`SecuenciaDePasos` escuchaba el arrastre en cualquier dirección** (`detectDragGestures`) pero
   solo aplicaba el movimiento horizontal: arrastrar hacia abajo capturaba el gesto (bloqueando el
   scroll de la pantalla) sin mover nada en el eje que sí importaba. Corregido a
   `detectHorizontalDragGestures`, que deja pasar el scroll vertical y solo reacciona al arrastre
   lateral real.
2. La mesa donde se suelta el instrumento era un cuadro invisible mientras estaba vacío. Se agregó
   borde, fondo y el texto "Suelta aquí"; además, la primera ficha de instrumento y de paso hacen
   un rebote sutil hasta el primer arrastre, y soltar correctamente da una vibración corta.

Con la corrección aplicada se repitió el ciclo completo (instrumento, pasos con orden inválido
rechazado y orden válido aceptado, sellado, refresco del Home) en una instalación limpia del
emulador, sin ningún dato previo.

**Compilación final, fuente real del entregable (sección 15, v8):**

```
./gradlew clean testDebugUnitTest lintDebug assembleDebug
```

BUILD SUCCESSFUL · 68 tests, 0 fallos · `lintDebug` limpio. El APK de esta build local, renombrado
a `BaseDeCampo.v1.0.0.apk`, es el que se entrega en `66.BaseDeCampo/4.BaseDeCampo.v1.0.0.apk` — no
el de GitHub Actions, que queda solo como verificación adicional en un runner limpio (ambos
resultaron con el mismo SHA-256, build reproducible).

```
SHA-256: 8286e7e8baf96bd75f363e42b1e23c61a14e5c935fc4965e9eaf67b9b9042d6f
```

`apksigner verify` y `aapt2 dump badging` sobre ese APK confirman: firmado con el keystore de
depuración por defecto, `versionName` `1.0.0` coincide con el nombre del archivo, sin permiso
`INTERNET`, ícono de lanzador real conectado (`res/mipmap-anydpi-v26/ic_launcher.xml`).

Se agregaron `database/schema.sql`, `database/sample_data.sql` y `README.md`, que la sección 13.1
pide dejar en el repositorio y que faltaban desde la Fase 1.

Fase 2 completa: capturas reales tomadas jugando en `fabrica34` (API 34, instalación limpia),
Memoria Descriptiva y Manual de Usuario generados y verificados, carpeta de entrega
`66.BaseDeCampo/` armada con los cuatro archivos en el orden fijo de la sección 14.3.
