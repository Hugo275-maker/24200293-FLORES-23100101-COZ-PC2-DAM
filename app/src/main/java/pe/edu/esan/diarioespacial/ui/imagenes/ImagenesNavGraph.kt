package pe.edu.esan.diarioespacial.ui.imagenes

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import pe.edu.esan.diarioespacial.ui.navigation.BottomTab

/** Rutas internas de la opción Imágenes (Alumno 1). */
object ImagenesRoutes {
    const val LISTA = "imagenes/lista"
    // TODO (Alumno 1): DETALLE = "imagenes/detalle/{date}", FAVORITOS = "imagenes/favoritos"
}

/** Grafo de navegación de la opción Imágenes. Lo completa el Alumno 1. */
fun NavGraphBuilder.imagenesGraph(navController: NavHostController) {
    navigation(startDestination = ImagenesRoutes.LISTA, route = BottomTab.Imagenes.route) {
        composable(ImagenesRoutes.LISTA) {
            ImagenesListaScreen()
        }
    }
}
