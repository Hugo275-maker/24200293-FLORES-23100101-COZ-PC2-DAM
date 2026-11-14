package pe.edu.esan.diarioespacial.ui.imagenes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pe.edu.esan.diarioespacial.data.local.apod.ApodEntity
import pe.edu.esan.diarioespacial.data.repository.ApodRepository

/**
 * ViewModel de la opción Imágenes (Alumno 1).
 * Expone las imágenes de los últimos 7 días y los favoritos mediante StateFlow.
 */
class ImagenesViewModel(
    private val repository: ApodRepository
) : ViewModel() {

    val imagenes: StateFlow<List<ApodEntity>> = repository.getImagenesUltimos7Dias()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val favoritas: StateFlow<List<ApodEntity>> = repository.favoritas
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        sincronizar()
    }

    fun sincronizar() {
        viewModelScope.launch {
            _cargando.value = true
            _error.value = null
            val resultado = repository.syncUltimos7Dias()
            resultado.onFailure { throwable ->
                _error.value = throwable.localizedMessage ?: "Error al conectar con la API de la NASA"
            }
            _cargando.value = false
        }
    }

    fun toggleFavorito(date: String, esFavorito: Boolean) {
        viewModelScope.launch {
            repository.toggleFavorite(date, esFavorito)
        }
    }

    fun getImagenPorFecha(date: String): StateFlow<ApodEntity?> {
        return repository.getImagenPorFecha(date)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )
    }

    fun limpiarError() {
        _error.value = null
    }
}

class ImagenesViewModelFactory(
    private val repository: ApodRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ImagenesViewModel(repository) as T
    }
}
