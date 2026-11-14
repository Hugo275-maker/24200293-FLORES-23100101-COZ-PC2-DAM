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
    // ---------------------------------------------------------------
    val apodRepository: pe.edu.esan.diarioespacial.data.repository.ApodRepository by lazy {
        pe.edu.esan.diarioespacial.data.repository.ApodRepository(
            pe.edu.esan.diarioespacial.data.remote.RetrofitClient.create<pe.edu.esan.diarioespacial.data.remote.ApodApiService>(),
            database.apodDao()
        )
    }



    // ---------------------------------------------------------------
    // Alumno 2 – Asteroides (NeoWs)
    // Ejemplo:
    // val asteroidRepository by lazy {
    //     AsteroidRepository(RetrofitClient.create<NeoApiService>(), database.asteroidDao())
    // }
    // ---------------------------------------------------------------
}
