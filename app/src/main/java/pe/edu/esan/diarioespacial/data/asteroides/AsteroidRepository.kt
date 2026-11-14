package pe.edu.esan.diarioespacial.data.asteroides

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import pe.edu.esan.diarioespacial.BuildConfig
import pe.edu.esan.diarioespacial.data.local.asteroides.AsteroidDao
import pe.edu.esan.diarioespacial.data.local.asteroides.AsteroidEntity
import pe.edu.esan.diarioespacial.data.remote.asteroides.NeoApiService
import pe.edu.esan.diarioespacial.util.FechaUtils
import retrofit2.HttpException
import java.io.IOException

/**
 * Repository de la opción Asteroides (Alumno 2).
 *
 * - La API solo se usa para refrescar: su respuesta se convierte a Entity
 *   y se guarda en Room.
 * - Las pantallas observan siempre lo guardado en Room (Flow).
 */
class AsteroidRepository(
    private val api: NeoApiService,
    private val dao: AsteroidDao
) {

    /** Rango consultado: los últimos 7 días (desde hace 6 días hasta hoy). */
    val rango: Pair<String, String>
        get() = FechaUtils.ultimos7Dias()

    fun observarLista(soloPeligrosos: Boolean): Flow<List<AsteroidEntity>> {
        val (desde, hasta) = rango
        return dao.observarLista(desde, hasta, soloPeligrosos)
    }

    fun observarFavoritos(): Flow<List<AsteroidEntity>> = dao.observarFavoritos()

    fun observarAsteroide(id: String): Flow<AsteroidEntity?> = dao.observarPorId(id)

    /**
     * Consulta el feed de NeoWs y guarda el resultado en Room.
     * No lanza excepciones: devuelve un Result con un mensaje legible si falla.
     */
    suspend fun refrescar(): Result<Unit> {
        val (desde, hasta) = rango
        return try {
            val respuesta = api.obtenerFeed(startDate = desde, endDate = hasta)
            dao.reemplazarConservandoFavoritos(respuesta.aEntidades())
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
            Result.failure(Exception("Sin conexión. Se muestran los datos guardados.", e))
        } catch (e: Exception) {
            Result.failure(Exception("No se pudieron leer los datos de la API.", e))
        }
    }

    suspend fun cambiarFavorito(id: String, favorito: Boolean) =
        dao.actualizarFavorito(id, favorito)
}
