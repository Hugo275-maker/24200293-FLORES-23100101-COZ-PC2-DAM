package pe.edu.esan.diarioespacial

import android.app.Application

/**
 * Clase Application: crea el contenedor de dependencias una sola vez
 * para que los ViewModels de ambas opciones lo reutilicen.
 */
class DiarioEspacialApp : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
