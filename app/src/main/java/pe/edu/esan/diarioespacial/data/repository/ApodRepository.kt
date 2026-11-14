package pe.edu.esan.diarioespacial.data.repository

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import pe.edu.esan.diarioespacial.BuildConfig
import pe.edu.esan.diarioespacial.data.local.apod.ApodDao
import pe.edu.esan.diarioespacial.data.local.apod.ApodEntity
import pe.edu.esan.diarioespacial.data.remote.ApodApiService
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

    val imagenes: Flow<List<ApodEntity>> = apodDao.getTodasLasImagenes()
    val favoritas: Flow<List<ApodEntity>> = apodDao.getImagenesFavoritas()

    fun getImagenPorFecha(date: String): Flow<ApodEntity?> =
        apodDao.getImagenPorFechaFlow(date)

    suspend fun toggleFavorite(date: String, currentIsFavorite: Boolean) {
        apodDao.updateFavoriteStatus(date, !currentIsFavorite)
    }

    /**
     * Consulta APOD de los últimos 7 días y lo guarda en Room.
     * No lanza excepciones: devuelve un Result con un mensaje en español si falla.
     */
    suspend fun syncUltimos7Dias(): Result<Unit> {
        val (startDate, endDate) = FechaUtils.ultimos7Dias()
        return try {
            val entities = apiService.getApodImages(startDate, endDate)
                .mapNotNull { it.toEntity() }
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
}
