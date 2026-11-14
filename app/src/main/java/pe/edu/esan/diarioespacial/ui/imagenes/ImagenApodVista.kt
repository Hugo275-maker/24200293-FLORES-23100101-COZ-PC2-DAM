package pe.edu.esan.diarioespacial.ui.imagenes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.outlined.HideImage
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import pe.edu.esan.diarioespacial.data.local.apod.ApodEntity

/**
 * Zona de imagen de un APOD, usada en la lista, en favoritas y en el detalle.
 *  - Video: no se carga imagen, se muestra "Contenido en video".
 *  - Sin URL (o la API mandó el logo genérico): "Imagen no disponible".
 *  - Imagen: se carga con Coil, con indicador mientras carga y aviso si falla.
 */
@Composable
fun ImagenApodVista(
    imagen: ApodEntity,
    altura: Dp,
    usarAltaResolucion: Boolean,
    modifier: Modifier = Modifier
) {
    val url = if (usarAltaResolucion) imagen.hdurl ?: imagen.url else imagen.url
    val zona = modifier.fillMaxWidth().height(altura)

    when {
        imagen.mediaType == "video" -> AvisoSinImagen(
            icono = Icons.Filled.PlayCircle,
            titulo = "Contenido en video",
            modifier = zona
        )
        url == null -> AvisoSinImagen(
            icono = Icons.Outlined.HideImage,
            titulo = "Imagen no disponible",
            detalle = "La API de la NASA no envió la imagen de este día.",
            modifier = zona
        )
        else -> SubcomposeAsyncImage(
            model = url,
            contentDescription = imagen.title,
            contentScale = if (usarAltaResolucion) ContentScale.Fit else ContentScale.Crop,
            modifier = zona,
            loading = {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(modifier = Modifier.size(32.dp))
                }
            },
            error = {
                AvisoSinImagen(
                    icono = Icons.Outlined.HideImage,
                    titulo = "No se pudo cargar la imagen",
                    modifier = Modifier.fillMaxSize()
                )
            }
        )
    }
}

@Composable
private fun AvisoSinImagen(
    icono: ImageVector,
    titulo: String,
    modifier: Modifier = Modifier,
    detalle: String? = null
) {
    Surface(modifier = modifier, color = MaterialTheme.colorScheme.secondaryContainer) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.size(48.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            if (detalle != null) {
                Text(
                    text = detalle,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}
