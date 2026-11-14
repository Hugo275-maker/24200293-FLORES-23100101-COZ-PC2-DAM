package pe.edu.esan.diarioespacial.ui.asteroides

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import pe.edu.esan.diarioespacial.ui.common.MensajeVacio
import pe.edu.esan.diarioespacial.ui.common.PantallaConBarra

/** Pantalla provisional de la opción Asteroides. La reemplaza el Alumno 2. */
@Composable
fun AsteroidesListaScreen() {
    PantallaConBarra(titulo = "Asteroides cercanos") { padding ->
        MensajeVacio(
            mensaje = "Asteroides NeoWs",
            detalle = "En construcción (Alumno 2)",
            modifier = Modifier.padding(padding)
        )
    }
}
