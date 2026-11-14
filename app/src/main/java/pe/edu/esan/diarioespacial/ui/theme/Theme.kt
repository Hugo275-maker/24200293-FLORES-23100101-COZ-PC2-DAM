package pe.edu.esan.diarioespacial.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val EsquemaEspacial = darkColorScheme(
    primary = Nebulosa,
    onPrimary = Espacio,
    primaryContainer = NebulosaOscura,
    onPrimaryContainer = Texto,
    secondary = Sol,
    onSecondary = Espacio,
    background = Espacio,
    onBackground = Texto,
    surface = Espacio,
    onSurface = Texto,
    surfaceVariant = EspacioClaro,
    onSurfaceVariant = TextoSecundario,
    surfaceContainer = EspacioClaro,
    error = Peligro
)

/** Tema único de la app (oscuro, temática espacial). */
@Composable
fun DiarioEspacialTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EsquemaEspacial,
        typography = Typography(),
        content = content
    )
}
