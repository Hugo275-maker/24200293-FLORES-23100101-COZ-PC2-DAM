package pe.edu.esan.diarioespacial.data.remote.dto

import com.google.gson.annotations.SerializedName
import pe.edu.esan.diarioespacial.data.local.apod.ApodEntity

/**
 * Modelo de transferencia de datos (DTO) para la respuesta JSON de la API NASA APOD.
 */
data class ApodDto(
    @SerializedName("date") val date: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("explanation") val explanation: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("hdurl") val hdurl: String?,
    @SerializedName("media_type") val mediaType: String?,
    @SerializedName("copyright") val copyright: String?
) {
    /**
     * Convierte el DTO a una Entidad de Room ApodEntity.
     * Retorna null si la fecha es inválida o nula.
     */
    fun toEntity(isFavorite: Boolean = false): ApodEntity? {
        val fechaValidada = date?.trim()?.takeIf { it.isNotBlank() } ?: return null
        return ApodEntity(
            date = fechaValidada,
            title = title?.trim()?.ifBlank { "Sin título" } ?: "Sin título",
            explanation = explanation?.trim()?.ifBlank { "Sin descripción disponible." } ?: "Sin descripción disponible.",
            url = url.urlDeImagenValida(),
            hdurl = hdurl.urlDeImagenValida(),
            mediaType = mediaType?.trim()?.ifBlank { "image" } ?: "image",
            copyright = copyright?.trim()?.takeIf { it.isNotBlank() },
            isFavorite = isFavorite
        )
    }
}

/**
 * Desde que apod.nasa.gov se mudó a science.nasa.gov/apod/, la API devuelve
 * para todos los días el logo del sitio en lugar de la foto del día.
 * Ese logo no es la imagen astronómica, así que se trata como "sin imagen".
 */
internal fun esLogoGenericoNasa(url: String): Boolean =
    url.contains("nasa-logo", ignoreCase = true)

/** URL limpia, o null si viene vacía o es el logo genérico de la NASA. */
internal fun String?.urlDeImagenValida(): String? =
    this?.trim()?.takeIf { it.isNotBlank() && !esLogoGenericoNasa(it) }
