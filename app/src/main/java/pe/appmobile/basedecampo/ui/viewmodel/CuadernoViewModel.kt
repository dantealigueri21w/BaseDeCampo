package pe.appmobile.basedecampo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.appmobile.basedecampo.data.entity.InsigniaEntity
import pe.appmobile.basedecampo.data.repository.ExpedicionRepository

data class CuadernoUiState(
    val expediciones: List<ExpedicionConEstado> = emptyList(),
    val insignias: List<InsigniaEntity> = emptyList(),
    val cargando: Boolean = true,
)

class CuadernoViewModel(private val repository: ExpedicionRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(CuadernoUiState())
    val uiState: StateFlow<CuadernoUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val expediciones = repository.obtenerExpedicionesConPasos()
            val selladas = repository.obtenerIdsExpedicionesSelladas()
            _uiState.value = CuadernoUiState(
                expediciones = expediciones.map { ExpedicionConEstado(it, it.id in selladas) },
                insignias = repository.obtenerInsignias(),
                cargando = false,
            )
        }
    }

    class Factory(private val repository: ExpedicionRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = CuadernoViewModel(repository) as T
    }
}
