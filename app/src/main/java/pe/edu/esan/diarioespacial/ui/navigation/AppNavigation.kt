package pe.edu.esan.diarioespacial.ui.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import pe.edu.esan.diarioespacial.ui.asteroides.asteroidesGraph
import pe.edu.esan.diarioespacial.ui.imagenes.imagenesGraph

/**
 * Navegación principal (compartida): barra inferior + NavHost.
 *
 * Cada opción tiene su propio grafo anidado en su paquete:
 *  - ui/imagenes/ImagenesNavGraph.kt   -> Alumno 1
 *  - ui/asteroides/AsteroidesNavGraph.kt -> Alumno 2
 * Las rutas internas (lista, detalle, favoritos) se agregan allí,
 * no en este archivo.
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val backStackEntry by navController.currentBackStackEntryAsState()
                val destinoActual = backStackEntry?.destination

                BottomTab.todas.forEach { tab ->
                    val seleccionado =
                        destinoActual?.hierarchy?.any { it.route == tab.route } == true
                    NavigationBarItem(
                        selected = seleccionado,
                        onClick = {
                            navController.navigate(tab.route) {
                                // Conserva el estado de cada pestaña al cambiar entre ellas.
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(tab.icon, contentDescription = tab.label) },
                        label = { Text(tab.label) }
                    )
                }
            }
        },
        // Las barras superiores de cada pantalla manejan el espacio del status bar.
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomTab.Imagenes.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            imagenesGraph(navController)
            asteroidesGraph(navController)
        }
    }
}
