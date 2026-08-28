package pe.appmobile.basedecampo.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import pe.appmobile.basedecampo.data.entity.ExpedicionEntity
import pe.appmobile.basedecampo.data.entity.InsigniaEntity
import pe.appmobile.basedecampo.data.entity.PasoProcedimientoEntity
import pe.appmobile.basedecampo.data.entity.PlanSelladoEntity
import pe.appmobile.basedecampo.data.entity.RachaEntity
import pe.appmobile.basedecampo.data.entity.RepasoPendienteEntity

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class AppDatabaseTest {
    private lateinit var db: AppDatabase

    @Before
    fun crearDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun cerrarDb() {
        db.close()
    }

    @Test
    fun `base de datos recien creada no tiene expediciones`() = runTest {
        assertTrue(db.expedicionDao().obtenerTodas().isEmpty())
    }

    @Test
    fun `insertar y leer una expedicion la devuelve completa`() = runTest {
        val expedicion = ExpedicionEntity(
            id = "frio_puna", nombre = "El Frío de la Puna", pregunta = "¿Pregunta?",
            variableAMedir = "TEMPERATURA", instrumentoCorrectoId = "termometro",
            repeticionesMinimas = 3, orden = 3,
        )
        db.expedicionDao().insertarTodas(listOf(expedicion))
        val leidas = db.expedicionDao().obtenerTodas()
        assertEquals(1, leidas.size)
        assertEquals("El Frío de la Puna", leidas.first().nombre)
    }

    @Test
    fun `las expediciones se leen ordenadas por el campo orden`() = runTest {
        val exp8 = ExpedicionEntity("e8", "Octava", "p", "DISTANCIA", "cinta_metrica", 4, orden = 8)
        val exp1 = ExpedicionEntity("e1", "Primera", "p", "VOLUMEN", "vaso_graduado", 3, orden = 1)
        db.expedicionDao().insertarTodas(listOf(exp8, exp1))
        val leidas = db.expedicionDao().obtenerTodas()
        assertEquals("Primera", leidas.first().nombre)
        assertEquals("Octava", leidas.last().nombre)
    }

    @Test
    fun `los pasos de una expedicion se leen ordenados y solo los de esa expedicion`() = runTest {
        db.expedicionDao().insertarTodas(listOf(
            ExpedicionEntity("e1", "Uno", "p", "VOLUMEN", "vaso_graduado", 3, 1),
            ExpedicionEntity("e2", "Dos", "p", "PESO", "balanza", 3, 2),
        ))
        db.pasoProcedimientoDao().insertarTodos(listOf(
            PasoProcedimientoEntity(pasoId = "b", expedicionId = "e1", descripcion = "B", orden = 2, debeIrAntesDeCsv = ""),
            PasoProcedimientoEntity(pasoId = "a", expedicionId = "e1", descripcion = "A", orden = 1, debeIrAntesDeCsv = "b"),
            PasoProcedimientoEntity(pasoId = "x", expedicionId = "e2", descripcion = "X", orden = 1, debeIrAntesDeCsv = ""),
        ))
        val pasosE1 = db.pasoProcedimientoDao().obtenerPorExpedicion("e1")
        assertEquals(2, pasosE1.size)
        assertEquals("a", pasosE1.first().pasoId)
    }

    @Test
    fun `borrar una expedicion borra en cascada sus pasos`() = runTest {
        db.expedicionDao().insertarTodas(listOf(ExpedicionEntity("e1", "Uno", "p", "VOLUMEN", "vaso_graduado", 3, 1)))
        db.pasoProcedimientoDao().insertarTodos(listOf(
            PasoProcedimientoEntity(pasoId = "a", expedicionId = "e1", descripcion = "A", orden = 1, debeIrAntesDeCsv = ""),
        ))
        db.expedicionDao().eliminar("e1")
        assertTrue(db.pasoProcedimientoDao().obtenerPorExpedicion("e1").isEmpty())
    }

    @Test
    fun `un plan sellado insertado queda en el historial`() = runTest {
        db.expedicionDao().insertarTodas(listOf(ExpedicionEntity("e1", "Uno", "p", "VOLUMEN", "vaso_graduado", 3, 1)))
        db.planSelladoDao().insertar(
            PlanSelladoEntity(expedicionId = "e1", instrumentoElegidoId = "vaso_graduado", ordenPasosCsv = "a,b", repeticionesElegidas = 3, fechaSellado = 1000L, alPrimerIntento = true),
        )
        val historial = db.planSelladoDao().obtenerTodos()
        assertEquals(1, historial.size)
        assertEquals("e1", historial.first().expedicionId)
    }

    @Test
    fun `marcar una insignia como obtenida la refleja en los ids ganados`() = runTest {
        db.insigniaDao().insertarTodas(listOf(InsigniaEntity("primer_plan", "Primer Plan", "Sella tu primer plan", fechaObtenida = null)))
        assertTrue(db.insigniaDao().obtenerIdsGanadas().isEmpty())
        db.insigniaDao().marcarObtenida("primer_plan", fecha = 5000L)
        assertEquals(listOf("primer_plan"), db.insigniaDao().obtenerIdsGanadas())
    }

    @Test
    fun `guardar la racha dos veces reemplaza el valor anterior, no lo duplica`() = runTest {
        db.rachaDao().guardar(RachaEntity(diasConsecutivos = 1, ultimaFechaActividad = 1000L))
        db.rachaDao().guardar(RachaEntity(diasConsecutivos = 2, ultimaFechaActividad = 2000L))
        val racha = db.rachaDao().obtener()
        assertEquals(2, racha?.diasConsecutivos)
    }

    @Test
    fun `sin racha guardada todavia obtener devuelve null`() = runTest {
        assertNull(db.rachaDao().obtener())
    }

    @Test
    fun `un item de repaso solo aparece pendiente para hoy cuando su fecha ya llego`() = runTest {
        val unDia = 24L * 60 * 60 * 1000
        db.repasoPendienteDao().guardar(RepasoPendienteEntity("frio_puna", fechaUltimoFallo = 0L, intervaloDias = 1, proximaRevision = unDia))
        assertTrue(db.repasoPendienteDao().obtenerPendientesParaHoy(hoy = unDia - 1000).isEmpty())
        assertEquals(1, db.repasoPendienteDao().obtenerPendientesParaHoy(hoy = unDia).size)
    }
}
