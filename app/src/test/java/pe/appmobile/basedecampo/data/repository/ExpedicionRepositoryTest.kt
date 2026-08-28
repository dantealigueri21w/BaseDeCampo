package pe.appmobile.basedecampo.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import pe.appmobile.basedecampo.data.AppDatabase
import pe.appmobile.basedecampo.domain.model.PlanPropuesto
import java.util.concurrent.TimeUnit

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExpedicionRepositoryTest {
    private lateinit var db: AppDatabase
    private lateinit var repository: ExpedicionRepository

    @Before
    fun crearDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).allowMainThreadQueries().build()
        repository = ExpedicionRepository(db)
    }

    @After
    fun cerrarDb() {
        db.close()
    }

    @Test
    fun `sembrar en una base de datos vacia inserta las 8 expediciones reales`() = runTest {
        repository.sembrarSiEsPrimerLanzamiento()
        assertEquals(8, repository.obtenerExpedicionesConPasos().size)
    }

    @Test
    fun `sembrar dos veces no duplica las expediciones`() = runTest {
        repository.sembrarSiEsPrimerLanzamiento()
        repository.sembrarSiEsPrimerLanzamiento()
        assertEquals(8, repository.obtenerExpedicionesConPasos().size)
    }

    @Test
    fun `cada expedicion sembrada trae sus pasos reales, no una lista vacia`() = runTest {
        repository.sembrarSiEsPrimerLanzamiento()
        val frioPuna = repository.obtenerExpedicionesConPasos().first { it.id == "frio_puna" }
        assertEquals(4, frioPuna.pasos.size)
    }

    @Test
    fun `sellar un plan valido lo guarda y otorga la insignia Primer Plan`() = runTest {
        repository.sembrarSiEsPrimerLanzamiento()
        val expedicion = repository.obtenerExpedicionesConPasos().first { it.id == "frio_puna" }
        val plan = PlanPropuesto(
            expedicionId = "frio_puna",
            instrumentoElegidoId = "termometro",
            ordenPasos = expedicion.pasos.map { it.id },
            repeticionesElegidas = 3,
        )
        val resultado = repository.sellarPlan(expedicion, plan)
        assertTrue(resultado.esValido)
        assertTrue("primer_plan" in db.insigniaDao().obtenerIdsGanadas())
    }

    @Test
    fun `sellar un plan invalido no lo guarda y lo registra para repaso`() = runTest {
        repository.sembrarSiEsPrimerLanzamiento()
        val expedicion = repository.obtenerExpedicionesConPasos().first { it.id == "frio_puna" }
        val planConInstrumentoIncorrecto = PlanPropuesto(
            expedicionId = "frio_puna",
            instrumentoElegidoId = "regla",
            ordenPasos = expedicion.pasos.map { it.id },
            repeticionesElegidas = 3,
        )
        val resultado = repository.sellarPlan(expedicion, planConInstrumentoIncorrecto)
        assertTrue(!resultado.esValido)
        val dosDiasDespues = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(2)
        assertEquals(1, repository.obtenerPendientesDeRepasoHoy(hoy = dosDiasDespues).size)
    }

    @Test
    fun `un intento fallido queda pendiente de repaso recien al dia siguiente, no el mismo dia`() = runTest {
        repository.sembrarSiEsPrimerLanzamiento()
        val expedicion = repository.obtenerExpedicionesConPasos().first { it.id == "frio_puna" }
        val planInvalido = PlanPropuesto("frio_puna", "regla", expedicion.pasos.map { it.id }, 3)
        repository.sellarPlan(expedicion, planInvalido)
        assertTrue(repository.obtenerPendientesDeRepasoHoy(hoy = System.currentTimeMillis()).isEmpty())
    }

    @Test
    fun `una expedicion sellada aparece en los ids sellados, una sin plan no`() = runTest {
        repository.sembrarSiEsPrimerLanzamiento()
        val expedicion = repository.obtenerExpedicionesConPasos().first { it.id == "frio_puna" }
        val plan = PlanPropuesto("frio_puna", "termometro", expedicion.pasos.map { it.id }, 3)
        repository.sellarPlan(expedicion, plan)
        val selladas = repository.obtenerIdsExpedicionesSelladas()
        assertTrue("frio_puna" in selladas)
        assertTrue("peso_piedra" !in selladas)
    }

    @Test
    fun `el catalogo de instrumentos trae los 7 instrumentos reales sembrados`() = runTest {
        repository.sembrarSiEsPrimerLanzamiento()
        assertEquals(7, repository.obtenerCatalogoInstrumentos().size)
    }
}
