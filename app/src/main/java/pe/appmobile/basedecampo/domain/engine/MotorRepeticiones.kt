package pe.appmobile.basedecampo.domain.engine

import pe.appmobile.basedecampo.domain.model.Expedicion

object MotorRepeticiones {
    fun esRepeticionesSuficientes(expedicion: Expedicion, repeticionesElegidas: Int): Boolean {
        if (repeticionesElegidas <= 0) return false
        return repeticionesElegidas >= expedicion.repeticionesMinimas
    }
}
