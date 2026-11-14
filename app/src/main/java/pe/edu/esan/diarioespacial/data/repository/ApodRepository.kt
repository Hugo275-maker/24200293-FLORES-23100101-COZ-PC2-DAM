package pe.edu.esan.diarioespacial.data.repository

import kotlinx.coroutines.flow.Flow
import pe.edu.esan.diarioespacial.data.local.apod.ApodDao
import pe.edu.esan.diarioespacial.data.local.apod.ApodEntity
import pe.edu.esan.diarioespacial.data.remote.ApodApiService
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

    val imagenes: Flow<List<ApodEntity>> = apodDao.getTodasLasImagenes()
    val favoritas: Flow<List<ApodEntity>> = apodDao.getImagenesFavoritas()

    fun getImagenPorFecha(date: String): Flow<ApodEntity?> =
        apodDao.getImagenPorFechaFlow(date)

    suspend fun toggleFavorite(date: String, currentIsFavorite: Boolean) {
        apodDao.updateFavoriteStatus(date, !currentIsFavorite)
    }

    suspend fun syncUltimos7Dias(): Result<Unit> {
        return runCatching {
            val (startDate, endDate) = FechaUtils.ultimos7Dias()
            val dtos = apiService.getApodImages(startDate, endDate)

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
