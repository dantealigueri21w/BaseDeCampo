package pe.appmobile.basedecampo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pe.appmobile.basedecampo.data.repository.ExpedicionRepository
import pe.appmobile.basedecampo.domain.model.Expedicion

data class ExpedicionConEstado(val expedicion: Expedicion, val sellado: Boolean)

data class HomeUiState(
    val expediciones: List<ExpedicionConEstado> = emptyList(),
    val cargando: Boolean = true,
)

class HomeViewModel(private val repository: ExpedicionRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.sembrarSiEsPrimerLanzamiento()
            cargar()
        }
    }

    fun recargar() {
        viewModelScope.launch { cargar() }
    }

    private suspend fun cargar() {
        val expediciones = repository.obtenerExpedicionesConPasos()
        val selladas = repository.obtenerIdsExpedicionesSelladas()
        _uiState.value = HomeUiState(
            expediciones = expediciones.map { ExpedicionConEstado(it, it.id in selladas) },
            cargando = false,
        )
    }

    class Factory(private val repository: ExpedicionRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = HomeViewModel(repository) as T
    }
}
