package pe.appmobile.basedecampo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.appmobile.basedecampo.data.entity.InstrumentoEntity
import pe.appmobile.basedecampo.data.repository.ExpedicionRepository
import pe.appmobile.basedecampo.domain.model.Expedicion
import pe.appmobile.basedecampo.domain.model.PlanPropuesto
import pe.appmobile.basedecampo.domain.model.ResultadoValidacion

data class ExpedicionUiState(
    val expedicion: Expedicion? = null,
    val instrumentos: List<InstrumentoEntity> = emptyList(),
    val instrumentoElegidoId: String? = null,
    val ordenPasos: List<String> = emptyList(),
    val repeticionesElegidas: Int = 0,
    val resultado: ResultadoValidacion? = null,
)

class ExpedicionViewModel(
    private val repository: ExpedicionRepository,
    private val expedicionId: String,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ExpedicionUiState())
    val uiState: StateFlow<ExpedicionUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val expedicion = repository.obtenerExpedicionesConPasos().first { it.id == expedicionId }
            _uiState.value = ExpedicionUiState(
                expedicion = expedicion,
                instrumentos = repository.obtenerCatalogoInstrumentos(),
                ordenPasos = expedicion.pasos.map { it.id },
            )
        }
    }

    fun elegirInstrumento(instrumentoId: String) {
        _uiState.value = _uiState.value.copy(instrumentoElegidoId = instrumentoId)
    }

    fun moverPaso(pasoId: String, indiceDestino: Int) {
        val estado = _uiState.value
        val listaActual = estado.ordenPasos.toMutableList()
        val indiceOrigen = listaActual.indexOf(pasoId)
        if (indiceOrigen == -1) return
        val destinoValido = indiceDestino.coerceIn(0, listaActual.lastIndex)
        listaActual.removeAt(indiceOrigen)
        listaActual.add(destinoValido, pasoId)
        _uiState.value = estado.copy(ordenPasos = listaActual)
    }

    fun cambiarRepeticiones(nuevoValor: Int) {
        _uiState.value = _uiState.value.copy(repeticionesElegidas = nuevoValor.coerceAtLeast(0))
    }

    fun sellarPlan() {
        val estado = _uiState.value
        val expedicion = estado.expedicion ?: return
        val instrumentoElegido = estado.instrumentoElegidoId ?: return
        viewModelScope.launch {
            val resultado = repository.sellarPlan(
                expedicion,
                PlanPropuesto(
                    expedicionId = expedicion.id,
                    instrumentoElegidoId = instrumentoElegido,
                    ordenPasos = estado.ordenPasos,
                    repeticionesElegidas = estado.repeticionesElegidas,
                ),
            )
            _uiState.value = estado.copy(resultado = resultado)
        }
    }

    class Factory(
        private val repository: ExpedicionRepository,
        private val expedicionId: String,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            ExpedicionViewModel(repository, expedicionId) as T
    }
}
