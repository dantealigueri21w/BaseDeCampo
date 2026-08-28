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
