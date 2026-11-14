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
            url = url?.trim()?.takeIf { it.isNotBlank() },
            hdurl = hdurl?.trim()?.takeIf { it.isNotBlank() },
            mediaType = mediaType?.trim()?.ifBlank { "image" } ?: "image",
            copyright = copyright?.trim()?.takeIf { it.isNotBlank() },
            isFavorite = isFavorite
        )
    }
}
