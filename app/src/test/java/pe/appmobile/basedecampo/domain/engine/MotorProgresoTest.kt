package pe.appmobile.basedecampo.domain.engine

import pe.appmobile.basedecampo.domain.model.PlanSellado
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MotorProgresoTest {

    private fun plan(expedicionId: String, alPrimerIntento: Boolean = true, fecha: Long = 0L) =
        PlanSellado(
            expedicionId = expedicionId,
            instrumentoElegidoId = "cualquiera",
            ordenPasos = listOf("p1"),
            repeticionesElegidas = 3,
            fechaSellado = fecha,
            alPrimerIntento = alPrimerIntento
        )

    @Test
    fun `sin planes sellados no hay insignias nuevas`() {
        val nuevas = MotorProgreso.calcularNuevasInsignias(emptyList(), emptySet())
        assertTrue(nuevas.isEmpty())
    }

    @Test
    fun `el primer plan sellado otorga Primer Plan`() {
        val nuevas = MotorProgreso.calcularNuevasInsignias(listOf(plan("exp1")), emptySet())
        assertTrue("primer_plan" in nuevas)
    }

    @Test
    fun `Primer Plan no se repite si ya estaba ganada`() {
        val nuevas = MotorProgreso.calcularNuevasInsignias(listOf(plan("exp1")), setOf("primer_plan"))
        assertFalse("primer_plan" in nuevas)
    }

    @Test
    fun `8 expediciones distintas selladas otorgan Base Completa`() {
        val historial = (1..8).map { plan("exp$it") }
        val nuevas = MotorProgreso.calcularNuevasInsignias(historial, setOf("primer_plan"))
        assertTrue("base_completa" in nuevas)
    }

    @Test
    fun `7 expediciones distintas NO otorgan Base Completa todavia`() {
        val historial = (1..7).map { plan("exp$it") }
        val nuevas = MotorProgreso.calcularNuevasInsignias(historial, setOf("primer_plan"))
        assertFalse("base_completa" in nuevas)
    }

    @Test
    fun `3 planes validos al primer intento otorgan Planificador Veloz`() {
        val historial = listOf(
            plan("exp1", alPrimerIntento = true),
            plan("exp2", alPrimerIntento = true),
            plan("exp3", alPrimerIntento = true)
        )
        val nuevas = MotorProgreso.calcularNuevasInsignias(historial, setOf("primer_plan"))
        assertTrue("planificador_veloz" in nuevas)
    }

    @Test
    fun `calcularRacha cuenta dias consecutivos con al menos un plan avanzado`() {
        val unDiaEnMillis = 24L * 60 * 60 * 1000
        val historial = listOf(
            plan("exp1", fecha = 0L),
            plan("exp2", fecha = unDiaEnMillis),
            plan("exp3", fecha = unDiaEnMillis * 2)
        )
        assertEquals(3, MotorProgreso.calcularRacha(historial, hoy = unDiaEnMillis * 2))
    }
}
