package pe.appmobile.basedecampo.domain.engine

import pe.appmobile.basedecampo.domain.model.PasoProcedimiento

object MotorSecuencia {
    fun esSecuenciaValida(pasos: List<PasoProcedimiento>, ordenPropuesto: List<String>): Boolean {
        val idsEsperados = pasos.map { it.id }.toSet()
        val idsPropuestos = ordenPropuesto.toSet()

        if (ordenPropuesto.size != pasos.size) return false // detecta repetidos o faltantes
        if (idsPropuestos != idsEsperados) return false // detecta IDs ajenos a esta expedición

        val posicion = ordenPropuesto.withIndex().associate { (indice, id) -> id to indice }

        for (paso in pasos) {
            val posicionActual = posicion[paso.id] ?: return false
            for (idPosterior in paso.debeIrAntesDe) {
                val posicionPosterior = posicion[idPosterior] ?: return false
                if (posicionActual >= posicionPosterior) return false
            }
        }
        return true
    }
}
