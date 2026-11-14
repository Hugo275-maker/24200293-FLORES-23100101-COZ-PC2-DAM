package pe.edu.esan.diarioespacial.ui.imagenes

import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import pe.edu.esan.diarioespacial.DiarioEspacialApp
import pe.edu.esan.diarioespacial.ui.navigation.BottomTab

/** Rutas internas de la opción Imágenes (Alumno 1). */
object ImagenesRoutes {
    const val LISTA = "imagenes/lista"
    const val FAVORITOS = "imagenes/favoritos"
    const val DETALLE = "imagenes/detalle/{date}"

    fun detalleRuta(date: String): String = "imagenes/detalle/${android.net.Uri.encode(date)}"
}

/** Grafo de navegación de la opción Imágenes (Alumno 1). */
fun NavGraphBuilder.imagenesGraph(navController: NavHostController) {
    navigation(startDestination = ImagenesRoutes.LISTA, route = BottomTab.Imagenes.route) {
        composable(ImagenesRoutes.LISTA) {
            val context = LocalContext.current
            val container = (context.applicationContext as DiarioEspacialApp).container
            val viewModel: ImagenesViewModel = viewModel(
                factory = ImagenesViewModelFactory(container.apodRepository, sincronizarAlIniciar = true)
            )

            ImagenesListaScreen(
                viewModel = viewModel,
                onNavigateToDetail = { date ->
                    navController.navigate(ImagenesRoutes.detalleRuta(date))
                },
                onNavigateToFavoritos = {
                    navController.navigate(ImagenesRoutes.FAVORITOS)
                }
            )
        }

        composable(ImagenesRoutes.FAVORITOS) {
            val context = LocalContext.current
            val container = (context.applicationContext as DiarioEspacialApp).container
            val viewModel: ImagenesViewModel = viewModel(
                factory = ImagenesViewModelFactory(container.apodRepository)
            )

            ImagenesFavoritasScreen(
                viewModel = viewModel,
                onNavigateToDetail = { date ->
                    navController.navigate(ImagenesRoutes.detalleRuta(date))
                },
                onVolver = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = ImagenesRoutes.DETALLE,
            arguments = listOf(
                navArgument("date") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val date = backStackEntry.arguments?.getString("date").orEmpty()
            val context = LocalContext.current
            val container = (context.applicationContext as DiarioEspacialApp).container
            val viewModel: ImagenesViewModel = viewModel(
                factory = ImagenesViewModelFactory(container.apodRepository)
            )

            ImagenDetalleScreen(
                date = date,
                viewModel = viewModel,
                onVolver = {
                    navController.popBackStack()
                }
            )
        }
    }
}
