package pe.appmobile.basedecampo.domain.engine

import pe.appmobile.basedecampo.domain.model.Expedicion
import pe.appmobile.basedecampo.domain.model.PasoProcedimiento
import pe.appmobile.basedecampo.domain.model.TipoVariable
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MotorInstrumentosTest {

    private fun expedicionDePrueba(variable: TipoVariable, instrumentoCorrectoId: String) =
        Expedicion(
            id = "exp_test",
            nombre = "Expedición de prueba",
            pregunta = "¿Pregunta de prueba?",
            variableAMedir = variable,
            instrumentoCorrectoId = instrumentoCorrectoId,
            pasos = listOf(PasoProcedimiento(id = "p1", descripcion = "Paso único")),
            repeticionesMinimas = 3
        )

    @Test
    fun `instrumento correcto es aceptado`() {
        val exp = expedicionDePrueba(TipoVariable.VOLUMEN, "vaso_graduado")
        assertTrue(MotorInstrumentos.esInstrumentoCorrecto(exp, "vaso_graduado"))
    }

    @Test
    fun `instrumento incorrecto es rechazado`() {
        val exp = expedicionDePrueba(TipoVariable.PESO, "balanza")
        assertFalse(MotorInstrumentos.esInstrumentoCorrecto(exp, "termometro"))
    }

    @Test
    fun `instrumento vacio es rechazado`() {
        val exp = expedicionDePrueba(TipoVariable.TEMPERATURA, "termometro")
        assertFalse(MotorInstrumentos.esInstrumentoCorrecto(exp, ""))
    }

    @Test
    fun `las 8 expediciones reales de la ficha aceptan su propio instrumento correcto`() {
        val casos = listOf(
            expedicionDePrueba(TipoVariable.VOLUMEN, "vaso_graduado") to "vaso_graduado",
            expedicionDePrueba(TipoVariable.PESO, "balanza") to "balanza",
            expedicionDePrueba(TipoVariable.TEMPERATURA, "termometro") to "termometro",
            expedicionDePrueba(TipoVariable.LONGITUD, "regla") to "regla",
            expedicionDePrueba(TipoVariable.TIEMPO, "cronometro") to "cronometro",
            expedicionDePrueba(TipoVariable.DETALLE_VISUAL, "lupa") to "lupa",
            expedicionDePrueba(TipoVariable.DISTANCIA, "cinta_metrica") to "cinta_metrica"
        )
        casos.forEach { (exp, instrumento) ->
            assertTrue(
                "Falló para ${exp.instrumentoCorrectoId}",
                MotorInstrumentos.esInstrumentoCorrecto(exp, instrumento)
            )
        }
    }

    @Test
    fun `un instrumento que mide otra variable no engaña al motor aunque suene parecido`() {
        // El Frio de la Puna mide TEMPERATURA con termometro -- una regla (mide LONGITUD)
        // no deberia aceptarse aunque el niño la elija por error.
        val exp = expedicionDePrueba(TipoVariable.TEMPERATURA, "termometro")
        assertFalse(MotorInstrumentos.esInstrumentoCorrecto(exp, "regla"))
    }
}
