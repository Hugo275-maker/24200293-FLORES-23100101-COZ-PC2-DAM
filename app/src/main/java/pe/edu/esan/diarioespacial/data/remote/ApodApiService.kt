package pe.edu.esan.diarioespacial.data.remote

import pe.edu.esan.diarioespacial.data.remote.dto.ApodDto
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Servicio Retrofit para consumir la API APOD (Astronomy Picture of the Day) de la NASA.
 *
 * Base URL: https://api.nasa.gov/
 * Endpoint: planetary/apod
 * La api_key se agrega automáticamente mediante el interceptor en RetrofitClient.
 */
interface ApodApiService {

    @GET("planetary/apod")
    suspend fun getApodImages(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String
    ): List<ApodDto>
}
