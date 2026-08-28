package pe.appmobile.basedecampo.domain.model

enum class TipoVariable {
    VOLUMEN, PESO, TEMPERATURA, LONGITUD, TIEMPO, DETALLE_VISUAL, DISTANCIA
}

enum class TipoInstrumento {
    VASO_GRADUADO, BALANZA, TERMOMETRO, REGLA, CRONOMETRO, LUPA, CINTA_METRICA
}

data class Instrumento(
    val id: String,
    val nombre: String,
    val tipo: TipoInstrumento,
    val mideVariable: TipoVariable
)

/**
 * [debeIrAntesDe] son los IDs de otros pasos que deben quedar DESPUÉS de este en cualquier
 * orden válido. No se exige una única secuencia: solo que estas restricciones de precedencia
 * se respeten (riesgo técnico de la ficha, sección "Requisitos especiales", punto 1).
 */
data class PasoProcedimiento(
    val id: String,
    val descripcion: String,
    val debeIrAntesDe: Set<String> = emptySet()
)

data class Expedicion(
    val id: String,
    val nombre: String,
    val pregunta: String,
    val variableAMedir: TipoVariable,
    val instrumentoCorrectoId: String,
    val pasos: List<PasoProcedimiento>,
    val repeticionesMinimas: Int
)

data class PlanPropuesto(
    val expedicionId: String,
    val instrumentoElegidoId: String,
    val ordenPasos: List<String>,
    val repeticionesElegidas: Int
)

data class ResultadoValidacion(
    val esValido: Boolean,
    val instrumentoCorrecto: Boolean,
    val secuenciaCorrecta: Boolean,
    val repeticionesCorrectas: Boolean,
    val mensajeError: String? = null
)

data class PlanSellado(
    val expedicionId: String,
    val instrumentoElegidoId: String,
    val ordenPasos: List<String>,
    val repeticionesElegidas: Int,
    val fechaSellado: Long,
    val alPrimerIntento: Boolean
)
