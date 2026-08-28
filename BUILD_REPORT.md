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
