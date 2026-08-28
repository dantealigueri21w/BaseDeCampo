package pe.appmobile.basedecampo.domain.engine

import pe.appmobile.basedecampo.domain.model.Expedicion
import pe.appmobile.basedecampo.domain.model.PlanPropuesto
import pe.appmobile.basedecampo.domain.model.ResultadoValidacion

object MotorPlanExperimento {
    fun validar(expedicion: Expedicion, plan: PlanPropuesto): ResultadoValidacion {
        val instrumentoOk = MotorInstrumentos.esInstrumentoCorrecto(expedicion, plan.instrumentoElegidoId)
        val secuenciaOk = MotorSecuencia.esSecuenciaValida(expedicion.pasos, plan.ordenPasos)
        val repeticionesOk = MotorRepeticiones.esRepeticionesSuficientes(expedicion, plan.repeticionesElegidas)

        val mensaje = when {
            !instrumentoOk -> "Ese instrumento no mide lo que esta expedición necesita."
            !secuenciaOk -> "Este orden de pasos no tiene sentido para el método."
            !repeticionesOk -> "Necesitas repetir la medición más veces para confiar en el resultado."
            else -> null
        }

        return ResultadoValidacion(
            esValido = instrumentoOk && secuenciaOk && repeticionesOk,
            instrumentoCorrecto = instrumentoOk,
            secuenciaCorrecta = secuenciaOk,
            repeticionesCorrectas = repeticionesOk,
            mensajeError = mensaje
        )
    }
}
