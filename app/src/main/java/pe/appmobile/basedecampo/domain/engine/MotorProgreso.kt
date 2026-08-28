package pe.appmobile.basedecampo.domain.engine

import pe.appmobile.basedecampo.domain.model.PlanSellado
import java.util.concurrent.TimeUnit

object MotorProgreso {
    fun calcularNuevasInsignias(
        historial: List<PlanSellado>,
        insigniasYaGanadas: Set<String>
    ): Set<String> {
        val nuevas = mutableSetOf<String>()

        if (historial.isNotEmpty() && "primer_plan" !in insigniasYaGanadas) {
            nuevas += "primer_plan"
        }

        val expedicionesDistintas = historial.map { it.expedicionId }.toSet()
        if (expedicionesDistintas.size >= 8 && "base_completa" !in insigniasYaGanadas) {
            nuevas += "base_completa"
        }

        val planesAlPrimerIntento = historial.count { it.alPrimerIntento }
        if (planesAlPrimerIntento >= 3 && "planificador_veloz" !in insigniasYaGanadas) {
            nuevas += "planificador_veloz"
        }

        val planesConPiezasCompletas = historial.count { it.ordenPasos.isNotEmpty() }
        if (planesConPiezasCompletas >= 8 && "cuaderno_lleno" !in insigniasYaGanadas) {
            nuevas += "cuaderno_lleno"
        }

        return nuevas
    }

    fun calcularRacha(historial: List<PlanSellado>, hoy: Long): Int {
        if (historial.isEmpty()) return 0
        val diasConActividad = historial
            .map { TimeUnit.MILLISECONDS.toDays(it.fechaSellado) }
            .toSortedSet()

        var racha = 0
        var diaActual = TimeUnit.MILLISECONDS.toDays(hoy)
        while (diaActual in diasConActividad) {
            racha++
            diaActual--
        }
        return racha
    }
}
