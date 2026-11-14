package pe.edu.esan.diarioespacial.ui.imagenes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pe.edu.esan.diarioespacial.ui.common.IndicadorCarga
import pe.edu.esan.diarioespacial.ui.common.MensajeVacio
import pe.edu.esan.diarioespacial.ui.common.PantallaConBarra

/**
 * Pantalla de Imágenes Favoritas.
 * Muestra el listado de imágenes guardadas como favoritas por el usuario desde Room.
 */
@Composable
fun ImagenesFavoritasScreen(
    viewModel: ImagenesViewModel,
    onNavigateToDetail: (String) -> Unit,
    onVolver: () -> Unit
) {
    val favoritas by viewModel.favoritas.collectAsStateWithLifecycle()

    PantallaConBarra(
        titulo = "Mis imágenes favoritas",
        onBack = onVolver
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            val lista = favoritas
            if (lista == null) {
                IndicadorCarga()
            } else if (lista.isEmpty()) {
                MensajeVacio(
                    mensaje = "Todavía no tienes imágenes favoritas.",
                    detalle = "Guarda tus imágenes APOD favoritas desde la pantalla principal o desde el detalle."
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(lista, key = { it.date }) { imagen ->
                        ImagenApodItem(
                            entity = imagen,
                            onClick = { onNavigateToDetail(imagen.date) },
                            onToggleFavorite = {
                                viewModel.toggleFavorito(imagen.date, imagen.isFavorite)
                            }
                        )
                    }
                }
            }
        }
    }
}
