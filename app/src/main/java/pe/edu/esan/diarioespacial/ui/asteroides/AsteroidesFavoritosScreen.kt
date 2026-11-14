package pe.edu.esan.diarioespacial.ui.asteroides

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import pe.edu.esan.diarioespacial.ui.common.IndicadorCarga
import pe.edu.esan.diarioespacial.ui.common.MensajeVacio
import pe.edu.esan.diarioespacial.ui.common.PantallaConBarra

/** "Mis asteroides": solo los marcados como favoritos. */
@Composable
fun AsteroidesFavoritosScreen(
    onBack: () -> Unit,
    onAbrirDetalle: (String) -> Unit,
    viewModel: AsteroidesFavoritosViewModel = viewModel(factory = AsteroidesViewModelFactory.Factory)
) {
    val estado by viewModel.uiState.collectAsStateWithLifecycle()

    PantallaConBarra(titulo = "Mis asteroides", onBack = onBack) { padding ->
        val modifier = Modifier.padding(padding)
        when {
            estado.cargando -> IndicadorCarga(modifier)
            estado.asteroides.isEmpty() -> MensajeVacio(
                mensaje = "Aún no tienes asteroides favoritos",
                detalle = "Toca la estrella de un asteroide para guardarlo aquí.",
                modifier = modifier
            )
            else -> ListaAsteroides(
                asteroides = estado.asteroides,
                onClick = { onAbrirDetalle(it.id) },
                onFavorito = viewModel::quitarFavorito,
                modifier = modifier
            )
        }
    }
}
