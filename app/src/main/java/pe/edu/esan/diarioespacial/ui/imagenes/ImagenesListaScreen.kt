package pe.edu.esan.diarioespacial.ui.imagenes

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import pe.edu.esan.diarioespacial.ui.common.MensajeVacio
import pe.edu.esan.diarioespacial.ui.common.PantallaConBarra

/** Pantalla provisional de la opción Imágenes. La reemplaza el Alumno 1. */
@Composable
fun ImagenesListaScreen() {
    PantallaConBarra(titulo = "Imágenes del día") { padding ->
        MensajeVacio(
            mensaje = "Imágenes APOD",
            detalle = "En construcción (Alumno 1)",
            modifier = Modifier.padding(padding)
        )
    }
}
