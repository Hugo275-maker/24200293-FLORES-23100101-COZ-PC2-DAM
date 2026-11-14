package pe.edu.esan.diarioespacial

import android.content.Context
import pe.edu.esan.diarioespacial.data.local.AppDatabase

/**
 * Contenedor de dependencias compartido (inyección manual).
 *
 * Cada alumno agrega su Repository SOLO dentro de su bloque, así los
 * Pull Requests de ambos no chocan en las mismas líneas.
 */
class AppContainer(context: Context) {

    val database: AppDatabase by lazy { AppDatabase.getInstance(context) }

    // ---------------------------------------------------------------
    // Alumno 1 – Imágenes (APOD)
    // Ejemplo:
    // val apodRepository by lazy {
    //     ApodRepository(RetrofitClient.create<ApodApiService>(), database.apodDao())
    // }
    // ---------------------------------------------------------------



    // ---------------------------------------------------------------
    // Alumno 2 – Asteroides (NeoWs)
    // (nombres completos para no tocar los imports compartidos y evitar conflictos)
    val asteroidRepository: pe.edu.esan.diarioespacial.data.asteroides.AsteroidRepository by lazy {
        pe.edu.esan.diarioespacial.data.asteroides.AsteroidRepository(
            pe.edu.esan.diarioespacial.data.remote.RetrofitClient
                .create<pe.edu.esan.diarioespacial.data.remote.asteroides.NeoApiService>(),
            database.asteroidDao()
        )
    }
    // ---------------------------------------------------------------
}
