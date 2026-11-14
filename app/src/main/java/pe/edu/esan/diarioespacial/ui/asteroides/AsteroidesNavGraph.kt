package pe.edu.esan.diarioespacial.ui.asteroides

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import pe.edu.esan.diarioespacial.ui.navigation.BottomTab

/** Rutas internas de la opción Asteroides (Alumno 2). */
object AsteroidesRoutes {
    const val LISTA = "asteroides/lista"
    // TODO (Alumno 2): DETALLE = "asteroides/detalle/{id}", FAVORITOS = "asteroides/favoritos"
}

/** Grafo de navegación de la opción Asteroides. Lo completa el Alumno 2. */
fun NavGraphBuilder.asteroidesGraph(navController: NavHostController) {
    navigation(startDestination = AsteroidesRoutes.LISTA, route = BottomTab.Asteroides.route) {
        composable(AsteroidesRoutes.LISTA) {
            AsteroidesListaScreen()
        }
    }
}
