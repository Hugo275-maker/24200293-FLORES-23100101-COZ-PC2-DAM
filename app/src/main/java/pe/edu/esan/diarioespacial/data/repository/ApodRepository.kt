package pe.edu.esan.diarioespacial.data.repository

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import pe.edu.esan.diarioespacial.BuildConfig
import pe.edu.esan.diarioespacial.data.local.apod.ApodDao
import pe.edu.esan.diarioespacial.data.local.apod.ApodEntity
import pe.edu.esan.diarioespacial.data.remote.ApodApiService
import pe.edu.esan.diarioespacial.data.remote.dto.ApodDto
import pe.edu.esan.diarioespacial.util.FechaUtils
import retrofit2.HttpException
import java.io.IOException

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
        val (startDate, _) = FechaUtils.ultimos7Dias()
        return apodDao.getImagenesUltimos7Dias(startDate)
    }

    val favoritas: Flow<List<ApodEntity>> = apodDao.getImagenesFavoritas()

    fun getImagenPorFecha(date: String): Flow<ApodEntity?> =
        apodDao.getImagenSingleFlow(date)

    suspend fun toggleFavorite(date: String, currentIsFavorite: Boolean) {
        apodDao.updateFavoriteStatus(date, !currentIsFavorite)
    }

    /**
     * Consulta APOD y guarda el resultado en Room.
     *
     * 1. Primero usa api.nasa.gov (planetary/apod) con start_date y end_date.
     * 2. Desde que apod.nasa.gov se mudó a science.nasa.gov, ese endpoint devuelve el
     *    logo y el título genérico "NASA Science". En ese caso (o si falla) se usa el
     *    endpoint oficial nuevo de la NASA: science.nasa.gov/wp-json/wp/v2/apod-basic.
     *
     * No lanza excepciones: devuelve un Result con un mensaje en español si falla.
     */
    suspend fun syncUltimos7Dias(): Result<Unit> {
        val (startDate, endDate) = FechaUtils.ultimos7Dias()
        return try {
            val dtos = try {
                val legacy = apiService.getApodImages(startDate, endDate)
                if (legacy.isEmpty() || legacy.all { it.esRespuestaGenerica() }) {
                    apiService.getApodImagesWp(perPage = 10)
                } else legacy
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                apiService.getApodImagesWp(perPage = 10)
            }

            // Solo los últimos 7 días (el endpoint nuevo devuelve los más recientes).
            val entities = dtos
                .mapNotNull { it.toEntity() }
                .filter { it.date in startDate..endDate }

            if (entities.isNotEmpty()) {
                apodDao.guardarConservandoFavoritas(entities)
            }
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: HttpException) {
            val mensaje = when (e.code()) {
                401, 403 -> if (BuildConfig.NASA_API_KEY.isBlank())
                    "Falta la API key: agrega NASA_API_KEY=tu_clave en local.properties y vuelve a ejecutar."
                else "La API rechazó la clave. Revisa NASA_API_KEY en local.properties."
                429 -> "Se superó el límite de consultas de la API. Intenta más tarde."
                else -> "Error del servidor de la NASA (${e.code()})."
            }
            Result.failure(Exception(mensaje, e))
        } catch (e: IOException) {
            Result.failure(Exception("Sin conexión. Se muestran las imágenes guardadas.", e))
        } catch (e: Exception) {
            Result.failure(Exception("No se pudieron leer los datos de la API.", e))
        }
    }

    /** Respuesta "rota" de api.nasa.gov: título genérico o solo la página HTML, sin imagen. */
    private fun ApodDto.esRespuestaGenerica(): Boolean =
        title == "NASA Science" ||
            (hdurl.isNullOrBlank() && url?.contains("/image-article/") == true)
}
