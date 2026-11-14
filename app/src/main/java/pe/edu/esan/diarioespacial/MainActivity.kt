package pe.edu.esan.diarioespacial

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import pe.edu.esan.diarioespacial.ui.navigation.AppNavigation
import pe.edu.esan.diarioespacial.ui.theme.DiarioEspacialTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DiarioEspacialTheme {
                AppNavigation()
            }
        }
    }
}
