package pe.appmobile.basedecampo.domain.engine

import pe.appmobile.basedecampo.domain.model.Expedicion
import pe.appmobile.basedecampo.domain.model.PasoProcedimiento
import pe.appmobile.basedecampo.domain.model.TipoVariable
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MotorRepeticionesTest {

    private fun expedicionConMinimo(minimo: Int) = Expedicion(
        id = "exp_test",
        nombre = "Expedición de prueba",
        pregunta = "¿Pregunta?",
        variableAMedir = TipoVariable.TIEMPO,
        instrumentoCorrectoId = "cronometro",
        pasos = listOf(PasoProcedimiento(id = "p1", descripcion = "Único paso")),
        repeticionesMinimas = minimo
    )

    @Test
    fun `repeticiones iguales al minimo son suficientes`() {
        assertTrue(MotorRepeticiones.esRepeticionesSuficientes(expedicionConMinimo(3), 3))
    }

    @Test
    fun `repeticiones por encima del minimo son suficientes`() {
        assertTrue(MotorRepeticiones.esRepeticionesSuficientes(expedicionConMinimo(3), 5))
    }

    @Test
    fun `repeticiones por debajo del minimo son insuficientes`() {
        assertFalse(MotorRepeticiones.esRepeticionesSuficientes(expedicionConMinimo(3), 2))
    }

    @Test
    fun `cero repeticiones nunca es suficiente aunque el minimo sea bajo`() {
        assertFalse(MotorRepeticiones.esRepeticionesSuficientes(expedicionConMinimo(1), 0))
    }
}
