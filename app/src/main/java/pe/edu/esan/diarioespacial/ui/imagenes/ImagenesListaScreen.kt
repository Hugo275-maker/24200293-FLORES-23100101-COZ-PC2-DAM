package pe.edu.esan.diarioespacial.ui.imagenes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pe.edu.esan.diarioespacial.data.local.apod.ApodEntity
import pe.edu.esan.diarioespacial.ui.common.IndicadorCarga
import pe.edu.esan.diarioespacial.ui.common.MensajeVacio
import pe.edu.esan.diarioespacial.ui.common.PantallaConBarra
import pe.edu.esan.diarioespacial.util.FechaUtils

/**
 * Pantalla principal de la opción Imágenes NASA APOD.
 * Muestra el listado de imágenes de los últimos 7 días ordenadas descendentemente por fecha.
 */
@Composable
fun ImagenesListaScreen(
    viewModel: ImagenesViewModel,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToFavoritos: () -> Unit
) {
    val imagenes by viewModel.imagenes.collectAsStateWithLifecycle()
    val cargando by viewModel.cargando.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()

    PantallaConBarra(
        titulo = "Imágenes del día",
        acciones = {
            IconButton(onClick = { viewModel.sincronizar() }, enabled = !cargando) {
                Icon(imageVector = Icons.Filled.Refresh, contentDescription = "Actualizar")
            }
            TextButton(onClick = onNavigateToFavoritos) {
                Icon(
                    imageVector = Icons.Outlined.Star,
                    contentDescription = "Mis imágenes favoritas"
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Mis favoritas")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Hay imágenes guardadas y se está actualizando: barra de progreso arriba.
                if (cargando && imagenes.isNotEmpty()) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
                if (error != null) {
                    Surface(
                        color = MaterialTheme.colorScheme.errorContainer,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = error ?: "Error de sincronización",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = { viewModel.sincronizar() }) {
                                Icon(
                                    imageVector = Icons.Filled.Refresh,
                                    contentDescription = "Reintentar",
                                    tint = MaterialTheme.colorScheme.onErrorContainer
                                )
                            }
                        }
                    }
                }

                if (cargando && imagenes.isEmpty()) {
                    IndicadorCarga()
                } else if (imagenes.isEmpty()) {
                    MensajeVacio(
                        mensaje = "No hay imágenes para mostrar",
                        detalle = "Revisa tu conexión y la API key, y vuelve a intentarlo."
                    )
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(imagenes, key = { it.date }) { imagen ->
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
}

@Composable
fun ImagenApodItem(
    entity: ApodEntity,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column {
            // Si es video no se carga la imagen; si la API no la envió se avisa.
            ImagenApodVista(imagen = entity, altura = 200.dp, usarAltaResolucion = false)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = entity.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = FechaUtils.legible(entity.date),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(onClick = onToggleFavorite) {
                    Icon(
                        imageVector = if (entity.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = if (entity.isFavorite) "Quitar de favoritos" else "Agregar a favoritos",
                        tint = if (entity.isFavorite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
