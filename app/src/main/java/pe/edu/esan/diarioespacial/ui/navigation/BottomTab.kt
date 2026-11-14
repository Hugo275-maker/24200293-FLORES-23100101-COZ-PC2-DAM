package pe.edu.esan.diarioespacial.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Public
import androidx.compose.ui.graphics.vector.ImageVector

/** Opciones de la barra inferior. Cada `route` es el grafo de navegación de esa opción. */
sealed class BottomTab(val route: String, val label: String, val icon: ImageVector) {
    data object Imagenes : BottomTab("imagenes", "Imágenes", Icons.Filled.Image)
    data object Asteroides : BottomTab("asteroides", "Asteroides", Icons.Filled.Public)

    companion object {
        val todas = listOf(Imagenes, Asteroides)
    }
}
