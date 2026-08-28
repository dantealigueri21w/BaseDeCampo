package pe.appmobile.basedecampo.domain.engine

import pe.appmobile.basedecampo.domain.model.Expedicion

object MotorInstrumentos {
    fun esInstrumentoCorrecto(expedicion: Expedicion, instrumentoElegidoId: String): Boolean {
        if (instrumentoElegidoId.isBlank()) return false
        return instrumentoElegidoId == expedicion.instrumentoCorrectoId
    }
}
