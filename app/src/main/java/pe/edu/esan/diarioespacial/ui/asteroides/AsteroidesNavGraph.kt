package pe.edu.esan.diarioespacial.ui.asteroides

import android.net.Uri
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import pe.edu.esan.diarioespacial.ui.navigation.BottomTab

/** Rutas internas de la opción Asteroides (Alumno 2). */
object AsteroidesRoutes {
    const val ARG_ID = "id"
    const val LISTA = "asteroides/lista"
    const val DETALLE = "asteroides/detalle/{$ARG_ID}"
    const val FAVORITOS = "asteroides/favoritos"

    fun detalle(id: String) = "asteroides/detalle/${Uri.encode(id)}"
}

/** Grafo de navegación de la opción Asteroides: lista -> detalle, lista -> favoritos -> detalle. */
fun NavGraphBuilder.asteroidesGraph(navController: NavHostController) {
    navigation(startDestination = AsteroidesRoutes.LISTA, route = BottomTab.Asteroides.route) {

        composable(AsteroidesRoutes.LISTA) {
            AsteroidesListaScreen(
                onAbrirDetalle = { id -> navController.navigate(AsteroidesRoutes.detalle(id)) },
                onAbrirFavoritos = { navController.navigate(AsteroidesRoutes.FAVORITOS) }
            )
        }

        composable(
            route = AsteroidesRoutes.DETALLE,
            arguments = listOf(navArgument(AsteroidesRoutes.ARG_ID) { type = NavType.StringType })
        ) {
            AsteroideDetalleScreen(onBack = { navController.popBackStack() })
        }

        composable(AsteroidesRoutes.FAVORITOS) {
            AsteroidesFavoritosScreen(
                onBack = { navController.popBackStack() },
                onAbrirDetalle = { id -> navController.navigate(AsteroidesRoutes.detalle(id)) }
            )
        }
    }
}
