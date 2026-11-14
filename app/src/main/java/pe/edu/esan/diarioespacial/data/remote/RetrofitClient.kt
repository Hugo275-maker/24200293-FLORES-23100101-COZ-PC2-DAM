package pe.edu.esan.diarioespacial.data.remote

import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pe.edu.esan.diarioespacial.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Configuración compartida de Retrofit para la API de la NASA.
 *
 * - La api_key se agrega automáticamente a TODAS las peticiones con un
 *   interceptor, así los servicios no necesitan recibirla como parámetro.
 * - La clave viene de BuildConfig.NASA_API_KEY (leída de local.properties).
 *
 * Cada alumno crea su propia interfaz de servicio (ApodApiService / NeoApiService)
 * y la obtiene con: RetrofitClient.create<MiServicio>()
 */
object RetrofitClient {

    private const val BASE_URL = "https://api.nasa.gov/"
    private const val TAG = "NasaApi"

    private const val HOST_API = "api.nasa.gov"

    // La api_key solo se manda a api.nasa.gov; otros hosts (p. ej. science.nasa.gov)
    // no la necesitan y no deben recibirla.
    private val apiKeyInterceptor = Interceptor { chain ->
        val original = chain.request()
        if (original.url.host != HOST_API) return@Interceptor chain.proceed(original)
        val url = original.url.newBuilder()
            .addQueryParameter("api_key", BuildConfig.NASA_API_KEY)
            .build()
        chain.proceed(original.newBuilder().url(url).build())
    }

    // Log de peticiones en Logcat ocultando la clave.
    private val loggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { mensaje ->
        val key = BuildConfig.NASA_API_KEY
        Log.d(TAG, if (key.isNotEmpty()) mensaje.replace(key, "***") else mensaje)
    }).apply {
        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BASIC
        else HttpLoggingInterceptor.Level.NONE
    }

    private val okHttpClient: OkHttpClient by lazy {
        if (BuildConfig.NASA_API_KEY.isEmpty()) {
            Log.w(TAG, "NASA_API_KEY vacía: agrégala en local.properties (ver README).")
        }
        OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    inline fun <reified T> create(): T = retrofit.create(T::class.java)
}
