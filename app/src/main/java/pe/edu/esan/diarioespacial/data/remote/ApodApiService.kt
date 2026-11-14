package pe.edu.esan.diarioespacial.data.remote

import pe.edu.esan.diarioespacial.data.remote.dto.ApodDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Servicio Retrofit para consumir la API APOD (Astronomy Picture of the Day) de la NASA.
 *
 * Incluye tanto el endpoint legacy (planetary/apod) como el nuevo endpoint oficial
 * de WordPress de la NASA (wp-json/wp/v2/apod-basic).
 */
interface ApodApiService {

    @GET("planetary/apod")
    suspend fun getApodImages(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String
    ): List<ApodDto>

    @GET("https://science.nasa.gov/wp-json/wp/v2/apod-basic")
    suspend fun getApodImagesWp(
        @Query("per_page") perPage: Int = 10
    ): List<ApodDto>
}
