package pe.edu.esan.diarioespacial.ui.asteroides

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import pe.edu.esan.diarioespacial.data.local.asteroides.AsteroidEntity
import pe.edu.esan.diarioespacial.ui.common.IndicadorCarga
import pe.edu.esan.diarioespacial.ui.common.MensajeVacio
import pe.edu.esan.diarioespacial.ui.common.PantallaConBarra

/** Lista de asteroides de los últimos 7 días (pantalla inicial de la opción). */
@Composable
fun AsteroidesListaScreen(
    onAbrirDetalle: (String) -> Unit,
    onAbrirFavoritos: () -> Unit,
    viewModel: AsteroidesListaViewModel = viewModel(factory = AsteroidesViewModelFactory.Factory)
) {
    val estado by viewModel.uiState.collectAsStateWithLifecycle()

    PantallaConBarra(
        titulo = "Asteroides cercanos",
        acciones = {
            IconButton(onClick = viewModel::refrescar, enabled = !estado.cargando) {
                Icon(Icons.Filled.Refresh, contentDescription = "Actualizar")
            }
            IconButton(onClick = onAbrirFavoritos) {
                Icon(Icons.Filled.Star, contentDescription = "Mis asteroides")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            FiltroPeligrosos(
                activado = estado.soloPeligrosos,
                onCambiar = viewModel::cambiarFiltro
            )
            ContenidoLista(
                estado = estado,
                onAbrirDetalle = { onAbrirDetalle(it.id) },
                onFavorito = viewModel::cambiarFavorito,
                onReintentar = viewModel::refrescar
            )
        }
    }
}

@Composable
private fun FiltroPeligrosos(activado: Boolean, onCambiar: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Solo potencialmente peligrosos",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        Switch(checked = activado, onCheckedChange = onCambiar)
    }
}

@Composable
private fun ContenidoLista(
    estado: AsteroidesListaUiState,
    onAbrirDetalle: (AsteroidEntity) -> Unit,
    onFavorito: (AsteroidEntity) -> Unit,
    onReintentar: () -> Unit
) {
    val lista = estado.asteroides

    // Sin datos aún y consultando la API: indicador de carga a pantalla completa.
    if (estado.cargando && lista.isEmpty()) {
        IndicadorCarga()
        return
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Hay datos guardados y se está actualizando: barra de progreso arriba.
        if (estado.cargando) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
        estado.error?.let { mensaje ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = mensaje,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f)
                )
                TextButton(onClick = onReintentar) { Text("Reintentar") }
            }
        }

        if (lista.isEmpty()) {
            MensajeVacio(
                mensaje = if (estado.soloPeligrosos) "No hay asteroides potencialmente peligrosos"
                else "No hay asteroides para mostrar",
                detalle = if (estado.soloPeligrosos) "Desactiva el filtro para ver todos."
                else "Revisa tu conexión y la API key, y vuelve a intentarlo."
            )
        } else {
            Text(
                text = "${lista.size} asteroides · del ${FormatoAsteroides.fecha(estado.desde)} " +
                    "al ${FormatoAsteroides.fecha(estado.hasta)}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
            ListaAsteroides(
                asteroides = lista,
                onClick = onAbrirDetalle,
                onFavorito = onFavorito
            )
        }
    }
}
