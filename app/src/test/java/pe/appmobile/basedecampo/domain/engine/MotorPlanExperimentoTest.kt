package pe.appmobile.basedecampo.domain.engine

import pe.appmobile.basedecampo.domain.model.Expedicion
import pe.appmobile.basedecampo.domain.model.PasoProcedimiento
import pe.appmobile.basedecampo.domain.model.PlanPropuesto
import pe.appmobile.basedecampo.domain.model.TipoVariable
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class MotorPlanExperimentoTest {

    // Modela "El Frío de la Puna": Termómetro, 2 pasos sin precedencia cruzada, mínimo 3 repeticiones.
    private val expedicion = Expedicion(
        id = "frio_puna",
        nombre = "El Frío de la Puna",
        pregunta = "¿Cuánto baja la temperatura de noche a distintas alturas?",
        variableAMedir = TipoVariable.TEMPERATURA,
        instrumentoCorrectoId = "termometro",
        pasos = listOf(
            PasoProcedimiento(id = "colocar", descripcion = "Colocar el termómetro", debeIrAntesDe = setOf("leer")),
            PasoProcedimiento(id = "leer", descripcion = "Leer la temperatura")
        ),
        repeticionesMinimas = 3
    )

    @Test
    fun `plan completamente correcto es valido y sin mensaje de error`() {
        val plan = PlanPropuesto(expedicion.id, "termometro", listOf("colocar", "leer"), 3)
        val resultado = MotorPlanExperimento.validar(expedicion, plan)
        assertTrue(resultado.esValido)
        assertNull(resultado.mensajeError)
    }

    @Test
    fun `plan con instrumento incorrecto es invalido`() {
        val plan = PlanPropuesto(expedicion.id, "regla", listOf("colocar", "leer"), 3)
        val resultado = MotorPlanExperimento.validar(expedicion, plan)
        assertFalse(resultado.esValido)
        assertFalse(resultado.instrumentoCorrecto)
    }

    @Test
    fun `plan con instrumento incorrecto explica que el instrumento esta mal`() {
        val plan = PlanPropuesto(expedicion.id, "regla", listOf("colocar", "leer"), 3)
        val resultado = MotorPlanExperimento.validar(expedicion, plan)
        assertNotNull(resultado.mensajeError)
        assertEquals("Ese instrumento no mide lo que esta expedición necesita.", resultado.mensajeError)
    }

    @Test
    fun `plan con secuencia invertida es invalido`() {
        val plan = PlanPropuesto(expedicion.id, "termometro", listOf("leer", "colocar"), 3)
        val resultado = MotorPlanExperimento.validar(expedicion, plan)
        assertFalse(resultado.esValido)
        assertFalse(resultado.secuenciaCorrecta)
    }

    @Test
    fun `plan con pocas repeticiones es invalido`() {
        val plan = PlanPropuesto(expedicion.id, "termometro", listOf("colocar", "leer"), 1)
        val resultado = MotorPlanExperimento.validar(expedicion, plan)
        assertFalse(resultado.esValido)
        assertFalse(resultado.repeticionesCorrectas)
    }

    @Test
    fun `plan con dos fallas a la vez reporta el primero segun prioridad instrumento-secuencia-repeticiones`() {
        val plan = PlanPropuesto(expedicion.id, "regla", listOf("leer", "colocar"), 1)
        val resultado = MotorPlanExperimento.validar(expedicion, plan)
        assertFalse(resultado.esValido)
        assertEquals("Ese instrumento no mide lo que esta expedición necesita.", resultado.mensajeError)
    }

    @Test
    fun `plan con instrumento y secuencia correctos pero repeticiones insuficientes explica repeticiones`() {
        val plan = PlanPropuesto(expedicion.id, "termometro", listOf("colocar", "leer"), 1)
        val resultado = MotorPlanExperimento.validar(expedicion, plan)
        assertEquals(
            "Necesitas repetir la medición más veces para confiar en el resultado.",
            resultado.mensajeError
        )
    }

    @Test
    fun `plan valido con repeticiones muy por encima del minimo sigue siendo valido`() {
        val plan = PlanPropuesto(expedicion.id, "termometro", listOf("colocar", "leer"), 10)
        val resultado = MotorPlanExperimento.validar(expedicion, plan)
        assertTrue(resultado.esValido)
    }
}
