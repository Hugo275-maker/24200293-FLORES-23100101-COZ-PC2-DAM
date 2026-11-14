package pe.edu.esan.diarioespacial.ui.asteroides

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import pe.edu.esan.diarioespacial.data.local.asteroides.AsteroidEntity
import pe.edu.esan.diarioespacial.ui.common.IndicadorCarga
import pe.edu.esan.diarioespacial.ui.common.MensajeVacio
import pe.edu.esan.diarioespacial.ui.common.PantallaConBarra

/** Detalle de un asteroide, leído desde Room por su id. */
@Composable
fun AsteroideDetalleScreen(
    onBack: () -> Unit,
    viewModel: AsteroideDetalleViewModel = viewModel(factory = AsteroidesViewModelFactory.Factory)
) {
    val estado by viewModel.uiState.collectAsStateWithLifecycle()
    val asteroide = estado.asteroide

    PantallaConBarra(
        titulo = "Detalle del asteroide",
        onBack = onBack,
        acciones = {
            if (asteroide != null) {
                BotonFavorito(esFavorito = asteroide.esFavorito, onClick = viewModel::cambiarFavorito)
            }
        }
    ) { padding ->
        val modifier = Modifier.padding(padding)
        when {
            estado.cargando -> IndicadorCarga(modifier)
            asteroide == null -> MensajeVacio(
                mensaje = "No se encontró el asteroide",
                detalle = "Vuelve a la lista y actualiza los datos.",
                modifier = modifier
            )
            else -> DetalleContenido(asteroide, modifier)
        }
    }
}

@Composable
private fun DetalleContenido(asteroide: AsteroidEntity, modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = asteroide.nombre,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        if (asteroide.esPeligroso) EtiquetaPeligroso()

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                FilaDetalle("Nombre", asteroide.nombre)
                FilaDetalle("ID", asteroide.id)
                FilaDetalle("Fecha de aproximación", FormatoAsteroides.fecha(asteroide.fechaAproximacion))
                FilaDetalle("Diámetro mínimo estimado", FormatoAsteroides.metros(asteroide.diametroMinM))
                FilaDetalle("Diámetro máximo estimado", FormatoAsteroides.metros(asteroide.diametroMaxM))
                FilaDetalle("Velocidad relativa", FormatoAsteroides.kmh(asteroide.velocidadKmh))
                FilaDetalle("Distancia de paso", FormatoAsteroides.km(asteroide.distanciaKm))
                FilaDetalle(
                    etiqueta = "Potencialmente peligroso",
                    valor = if (asteroide.esPeligroso) "Sí" else "No",
                    resaltar = asteroide.esPeligroso,
                    ultima = true
                )
            }
        }
    }
}

@Composable
private fun FilaDetalle(
    etiqueta: String,
    valor: String,
    resaltar: Boolean = false,
    ultima: Boolean = false
) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)) {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = valor,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.End,
            color = if (resaltar) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
    }
    if (!ultima) HorizontalDivider()
}
