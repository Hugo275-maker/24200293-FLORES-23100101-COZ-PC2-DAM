package pe.edu.esan.diarioespacial.ui.asteroides

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import pe.edu.esan.diarioespacial.data.local.asteroides.AsteroidEntity

/** Lista de asteroides reutilizada por la pantalla principal y por "Mis asteroides". */
@Composable
fun ListaAsteroides(
    asteroides: List<AsteroidEntity>,
    onClick: (AsteroidEntity) -> Unit,
    onFavorito: (AsteroidEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(asteroides, key = { it.id }) { asteroide ->
            AsteroideItem(
                asteroide = asteroide,
                onClick = { onClick(asteroide) },
                onFavorito = { onFavorito(asteroide) }
            )
        }
    }
}

/**
 * Elemento de la lista: nombre, fecha de aproximación, diámetro máximo,
 * velocidad y distancia. Los peligrosos llevan borde rojo y etiqueta.
 */
@Composable
fun AsteroideItem(
    asteroide: AsteroidEntity,
    onClick: () -> Unit,
    onFavorito: () -> Unit,
    modifier: Modifier = Modifier
) {
    val peligroso = asteroide.esPeligroso
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        border = if (peligroso) BorderStroke(2.dp, MaterialTheme.colorScheme.error) else null,
        colors = CardDefaults.cardColors(
            containerColor = if (peligroso) MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.35f)
            else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 12.dp, end = 4.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = asteroide.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = FormatoAsteroides.fecha(asteroide.fechaAproximacion),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (peligroso) {
                    Spacer(Modifier.size(6.dp))
                    EtiquetaPeligroso()
                }
                Spacer(Modifier.size(8.dp))
                DatoFila("Diámetro máx.", FormatoAsteroides.metros(asteroide.diametroMaxM))
                DatoFila("Velocidad", FormatoAsteroides.kmh(asteroide.velocidadKmh))
                DatoFila("Distancia", FormatoAsteroides.km(asteroide.distanciaKm))
            }
            BotonFavorito(esFavorito = asteroide.esFavorito, onClick = onFavorito)
        }
    }
}

@Composable
private fun DatoFila(etiqueta: String, valor: String) {
    Row(modifier = Modifier.padding(vertical = 1.dp)) {
        Text(
            text = "$etiqueta: ",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(text = valor, style = MaterialTheme.typography.bodyMedium)
    }
}

/** Etiqueta roja "Potencialmente peligroso". */
@Composable
fun EtiquetaPeligroso(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.error,
        contentColor = MaterialTheme.colorScheme.onError,
        shape = MaterialTheme.shapes.small
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.Warning, contentDescription = null, modifier = Modifier.size(14.dp))
            Spacer(Modifier.width(4.dp))
            Text("Potencialmente peligroso", style = MaterialTheme.typography.labelMedium)
        }
    }
}

/** Estrella para marcar/desmarcar favorito; cambia de aspecto según el estado. */
@Composable
fun BotonFavorito(esFavorito: Boolean, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = if (esFavorito) Icons.Filled.Star else Icons.Filled.StarBorder,
            contentDescription = if (esFavorito) "Quitar de favoritos" else "Marcar como favorito",
            tint = if (esFavorito) MaterialTheme.colorScheme.tertiary
            else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
