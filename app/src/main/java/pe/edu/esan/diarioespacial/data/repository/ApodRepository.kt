package pe.edu.esan.diarioespacial.data.repository

import kotlinx.coroutines.flow.Flow
import pe.edu.esan.diarioespacial.data.local.apod.ApodDao
import pe.edu.esan.diarioespacial.data.local.apod.ApodEntity
import pe.edu.esan.diarioespacial.data.remote.ApodApiService
import pe.edu.esan.diarioespacial.data.remote.dto.ApodDto
import pe.edu.esan.diarioespacial.util.FechaUtils

/**
 * Repositorio para la opción Imágenes (Alumno 1).
 * Coordina las operaciones entre Retrofit (capa remota) y Room (capa local).
 *
 * Regla principal: Las pantallas siempre observan los datos desde Room.
 * Cuando se sincroniza con la API, los registros se guardan en Room preservando el estado local de favoritos.
 */
class ApodRepository(
    private val apiService: ApodApiService,
    private val apodDao: ApodDao
) {

    fun getImagenesUltimos7Dias(): Flow<List<ApodEntity>> {
        val (startDate, endDate) = FechaUtils.ultimos7Dias()
        return apodDao.getImagenesUltimos7Dias(startDate, endDate)
    }

    val favoritas: Flow<List<ApodEntity>> = apodDao.getImagenesFavoritas()

    fun getImagenPorFecha(date: String): Flow<ApodEntity?> =
        apodDao.getImagenPorFechaFlow(date)

    suspend fun toggleFavorite(date: String, currentIsFavorite: Boolean) {
        apodDao.updateFavoriteStatus(date, !currentIsFavorite)
    }

    suspend fun syncUltimos7Dias(): Result<Unit> {
        return runCatching {
            var dtos: List<ApodDto>

            // 1. Intentar API legacy (planetary/apod)
            try {
                val (startDate, endDate) = FechaUtils.ultimos7Dias()
                dtos = apiService.getApodImages(startDate, endDate)

                // Si la respuesta legacy devuelve datos vacíos o sólo títulos genéricos "NASA Science",
                // recurrir al endpoint oficial de WordPress de la NASA
                if (dtos.isEmpty() || dtos.all { (it.title == "NASA Science") || (it.hdurl.isNullOrBlank() && it.url?.contains("/image-article/") == true) }) {
                    dtos = apiService.getApodImagesWp(perPage = 10)
                }
            } catch (_: Exception) {
                // 2. Si el endpoint legacy falla (ej. error 429 por límite de peticiones de DEMO_KEY),
                // consultar el endpoint oficial de la NASA science.nasa.gov
                dtos = apiService.getApodImagesWp(perPage = 10)
            }

            val favoritasSet = apodDao.getFechasFavoritas().toSet()

            val entities = dtos.mapNotNull { dto ->
                val date = dto.date
                if (date.isNullOrBlank()) {
                    null
                } else {
                    val isFav = favoritasSet.contains(date)
                    dto.toEntity(isFavorite = isFav)
                }
            }

            if (entities.isNotEmpty()) {
                apodDao.insertOrUpdateImagenes(entities)
            }
        }
    }
}
