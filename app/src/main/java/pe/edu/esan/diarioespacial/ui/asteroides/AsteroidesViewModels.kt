package pe.edu.esan.diarioespacial.ui.asteroides

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pe.edu.esan.diarioespacial.DiarioEspacialApp
import pe.edu.esan.diarioespacial.data.asteroides.AsteroidRepository
import pe.edu.esan.diarioespacial.data.local.asteroides.AsteroidEntity

// ------------------------------------------------------------------
// Lista
// ------------------------------------------------------------------

data class AsteroidesListaUiState(
    val asteroides: List<AsteroidEntity> = emptyList(),
    val soloPeligrosos: Boolean = false,
    val cargando: Boolean = true,
    val error: String? = null,
    val desde: String = "",
    val hasta: String = ""
)

@OptIn(ExperimentalCoroutinesApi::class)
class AsteroidesListaViewModel(private val repository: AsteroidRepository) : ViewModel() {

    private val soloPeligrosos = MutableStateFlow(false)
    private val cargando = MutableStateFlow(true)
    private val error = MutableStateFlow<String?>(null)

    /** La lista sale de Room; cambia sola al guardar datos nuevos o al mover el filtro. */
    private val asteroides = soloPeligrosos.flatMapLatest { repository.observarLista(it) }

    val uiState: StateFlow<AsteroidesListaUiState> =
        combine(asteroides, soloPeligrosos, cargando, error) { lista, filtro, cargandoAhora, mensaje ->
            val (desde, hasta) = repository.rango
            AsteroidesListaUiState(lista, filtro, cargandoAhora, mensaje, desde, hasta)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AsteroidesListaUiState())

    init {
        refrescar()
    }

    /** Consulta la API y guarda en Room. Mientras tanto se muestra el indicador de carga. */
    fun refrescar() {
        viewModelScope.launch {
            cargando.value = true
            error.value = null
            repository.refrescar().onFailure { error.value = it.message }
            cargando.value = false
        }
    }

    fun cambiarFiltro(solo: Boolean) {
        soloPeligrosos.value = solo
    }

    fun cambiarFavorito(asteroide: AsteroidEntity) {
        viewModelScope.launch { repository.cambiarFavorito(asteroide.id, !asteroide.esFavorito) }
    }
}

// ------------------------------------------------------------------
// Detalle
// ------------------------------------------------------------------

data class AsteroideDetalleUiState(
    val cargando: Boolean = true,
    val asteroide: AsteroidEntity? = null
)

class AsteroideDetalleViewModel(
    private val repository: AsteroidRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val id: String = savedStateHandle[AsteroidesRoutes.ARG_ID] ?: ""

    val uiState: StateFlow<AsteroideDetalleUiState> =
        repository.observarAsteroide(id)
            .map { AsteroideDetalleUiState(cargando = false, asteroide = it) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AsteroideDetalleUiState())

    fun cambiarFavorito() {
        val actual = uiState.value.asteroide ?: return
        viewModelScope.launch { repository.cambiarFavorito(actual.id, !actual.esFavorito) }
    }
}

// ------------------------------------------------------------------
// Favoritos ("Mis asteroides")
// ------------------------------------------------------------------

data class AsteroidesFavoritosUiState(
    val cargando: Boolean = true,
    val asteroides: List<AsteroidEntity> = emptyList()
)

class AsteroidesFavoritosViewModel(private val repository: AsteroidRepository) : ViewModel() {

    val uiState: StateFlow<AsteroidesFavoritosUiState> =
        repository.observarFavoritos()
            .map { AsteroidesFavoritosUiState(cargando = false, asteroides = it) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AsteroidesFavoritosUiState())

    fun quitarFavorito(asteroide: AsteroidEntity) {
        viewModelScope.launch { repository.cambiarFavorito(asteroide.id, false) }
    }
}

// ------------------------------------------------------------------
// Fábrica: toma el repository del AppContainer compartido.
// ------------------------------------------------------------------

object AsteroidesViewModelFactory {
    val Factory: ViewModelProvider.Factory = viewModelFactory {
        initializer { AsteroidesListaViewModel(repositorio()) }
        initializer { AsteroideDetalleViewModel(repositorio(), createSavedStateHandle()) }
        initializer { AsteroidesFavoritosViewModel(repositorio()) }
    }

    private fun CreationExtras.repositorio(): AsteroidRepository =
        (this[APPLICATION_KEY] as DiarioEspacialApp).container.asteroidRepository
}
