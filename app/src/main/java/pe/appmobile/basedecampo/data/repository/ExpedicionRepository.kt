package pe.appmobile.basedecampo.data.repository

import pe.appmobile.basedecampo.data.AppDatabase
import pe.appmobile.basedecampo.data.entity.InstrumentoEntity
import pe.appmobile.basedecampo.data.entity.PlanSelladoEntity
import pe.appmobile.basedecampo.data.entity.RachaEntity
import pe.appmobile.basedecampo.data.entity.RepasoPendienteEntity
import pe.appmobile.basedecampo.data.seed.SeedData
import pe.appmobile.basedecampo.domain.engine.MotorPlanExperimento
import pe.appmobile.basedecampo.domain.engine.MotorProgreso
import pe.appmobile.basedecampo.domain.engine.MotorRepaso
import pe.appmobile.basedecampo.domain.model.Expedicion
import pe.appmobile.basedecampo.domain.model.PasoProcedimiento
import pe.appmobile.basedecampo.domain.model.PlanPropuesto
import pe.appmobile.basedecampo.domain.model.PlanSellado
import pe.appmobile.basedecampo.domain.model.RepasoPendiente
import pe.appmobile.basedecampo.domain.model.ResultadoValidacion
import pe.appmobile.basedecampo.domain.model.TipoVariable

class ExpedicionRepository(private val db: AppDatabase) {

    suspend fun sembrarSiEsPrimerLanzamiento() {
        if (db.expedicionDao().obtenerTodas().isNotEmpty()) return
        db.instrumentoDao().insertarTodos(SeedData.instrumentos)
        db.expedicionDao().insertarTodas(SeedData.expediciones)
        db.pasoProcedimientoDao().insertarTodos(SeedData.pasos)
        db.insigniaDao().insertarTodas(SeedData.insignias)
    }

    suspend fun obtenerExpedicionesConPasos(): List<Expedicion> =
        db.expedicionDao().obtenerTodas().map { entidad ->
            val pasos = db.pasoProcedimientoDao().obtenerPorExpedicion(entidad.id).map { pasoEntidad ->
                PasoProcedimiento(
                    id = pasoEntidad.pasoId,
                    descripcion = pasoEntidad.descripcion,
                    debeIrAntesDe = if (pasoEntidad.debeIrAntesDeCsv.isBlank()) emptySet()
                    else pasoEntidad.debeIrAntesDeCsv.split(",").toSet(),
                )
            }
            Expedicion(
                id = entidad.id,
                nombre = entidad.nombre,
                pregunta = entidad.pregunta,
                variableAMedir = TipoVariable.valueOf(entidad.variableAMedir),
                instrumentoCorrectoId = entidad.instrumentoCorrectoId,
                pasos = pasos,
                repeticionesMinimas = entidad.repeticionesMinimas,
            )
        }

    suspend fun sellarPlan(expedicion: Expedicion, plan: PlanPropuesto): ResultadoValidacion {
        val resultado = MotorPlanExperimento.validar(expedicion, plan)
        val ahora = System.currentTimeMillis()
        val yaHabiaIntentoPrevio = db.planSelladoDao().obtenerTodos().any { it.expedicionId == expedicion.id }

        if (resultado.esValido) {
            db.planSelladoDao().insertar(
                PlanSelladoEntity(
                    expedicionId = expedicion.id,
                    instrumentoElegidoId = plan.instrumentoElegidoId,
                    ordenPasosCsv = plan.ordenPasos.joinToString(","),
                    repeticionesElegidas = plan.repeticionesElegidas,
                    fechaSellado = ahora,
                    alPrimerIntento = !yaHabiaIntentoPrevio,
                ),
            )
            actualizarProgreso()
        } else {
            registrarFalloParaRepaso(expedicion.id, ahora)
        }
        return resultado
    }

    suspend fun obtenerPendientesDeRepasoHoy(hoy: Long): List<RepasoPendiente> =
        db.repasoPendienteDao().obtenerPendientesParaHoy(hoy).map {
            RepasoPendiente(it.itemId, it.fechaUltimoFallo, it.intervaloDias, it.proximaRevision)
        }

    suspend fun obtenerIdsExpedicionesSelladas(): Set<String> =
        db.planSelladoDao().obtenerTodos().map { it.expedicionId }.toSet()

    suspend fun obtenerCatalogoInstrumentos(): List<InstrumentoEntity> = db.instrumentoDao().obtenerTodos()

    private suspend fun registrarFalloParaRepaso(expedicionId: String, ahora: Long) {
        val existente = db.repasoPendienteDao().obtenerPorId(expedicionId)
        val nuevoIntervalo = MotorRepaso.calcularProximoIntervalo(existente?.intervaloDias ?: 1, acerto = false)
        db.repasoPendienteDao().guardar(
            RepasoPendienteEntity(
                itemId = expedicionId,
                fechaUltimoFallo = ahora,
                intervaloDias = nuevoIntervalo,
                proximaRevision = MotorRepaso.calcularProximaRevision(ahora, nuevoIntervalo),
            ),
        )
    }

    private suspend fun actualizarProgreso() {
        val historial = db.planSelladoDao().obtenerTodos().map {
            PlanSellado(
                expedicionId = it.expedicionId,
                instrumentoElegidoId = it.instrumentoElegidoId,
                ordenPasos = it.ordenPasosCsv.split(","),
                repeticionesElegidas = it.repeticionesElegidas,
                fechaSellado = it.fechaSellado,
                alPrimerIntento = it.alPrimerIntento,
            )
        }
        val yaGanadas = db.insigniaDao().obtenerIdsGanadas().toSet()
        val nuevas = MotorProgreso.calcularNuevasInsignias(historial, yaGanadas)
        val ahora = System.currentTimeMillis()
        nuevas.forEach { db.insigniaDao().marcarObtenida(it, ahora) }

        val racha = MotorProgreso.calcularRacha(historial, hoy = ahora)
        db.rachaDao().guardar(RachaEntity(diasConsecutivos = racha, ultimaFechaActividad = ahora))
    }
}
