package pe.edu.esan.diarioespacial.data.remote.asteroides

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Servicio Retrofit de NeoWs (Alumno 2).
 * La api_key la agrega el interceptor de RetrofitClient, no se pasa aquí.
 * Se obtiene con: RetrofitClient.create<NeoApiService>()
 */
interface NeoApiService {

    /** Asteroides cercanos entre dos fechas (AAAA-MM-DD). Rango máximo: 7 días. */
    @GET("neo/rest/v1/feed")
    suspend fun obtenerFeed(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String
    ): NeoFeedResponseDto
}
