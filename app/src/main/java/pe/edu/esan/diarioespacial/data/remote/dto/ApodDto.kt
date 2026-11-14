package pe.edu.esan.diarioespacial.data.remote.dto

import com.google.gson.annotations.SerializedName
import pe.edu.esan.diarioespacial.data.local.apod.ApodEntity

/**
 * Modelo DTO para deserializar respuestas de la API NASA APOD
 * (compatible tanto con api.nasa.gov como con science.nasa.gov/wp-json/wp/v2/apod-basic).
 */
data class ApodDto(
    @SerializedName("date") val date: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("explanation") val explanation: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("hdurl") val hdurl: String?,
    @SerializedName("media_type") val mediaType: String?,
    @SerializedName("copyright") val copyright: String?,
    @SerializedName("credit") val credit: String? = null
) {
    /**
     * Convierte el DTO a ApodEntity limpiando etiquetas HTML, resolviendo URLs de imágenes directas
     * y preservando el estado local de favoritos.
     */
    fun toEntity(isFavorite: Boolean = false): ApodEntity? {
        val fechaValidada = date?.trim()?.takeIf { it.isNotBlank() } ?: return null
        val tituloLimpio = limpiarHtml(title).ifBlank { "Sin título" }

        val explicacionLimpia = limpiarHtml(explanation)
            .removePrefix("Explanation:")
            .removePrefix("Explanation :")
            .trim()
            .ifBlank { "Sin descripción disponible." }

        val autorLimpio = limpiarHtml(copyright ?: credit).takeIf { it.isNotBlank() }

        // Extraer URL directa de imagen omitiendo páginas web HTML o logotipos genéricos
        val directHdUrl = hdurl?.trim()?.takeIf {
            it.isNotBlank() && (it.startsWith("http://") || it.startsWith("https://")) && !it.contains("/image-article/")
        }

        val directUrl = url?.trim()?.takeIf {
            it.isNotBlank() && (it.startsWith("http://") || it.startsWith("https://")) && !it.contains("/image-article/") && !it.contains("/nasa-logo")
        }

        val imagenFinalUrl = directHdUrl ?: directUrl

        return ApodEntity(
            date = fechaValidada,
            title = tituloLimpio,
            explanation = explicacionLimpia,
            url = imagenFinalUrl,
            hdurl = directHdUrl ?: imagenFinalUrl,
            mediaType = mediaType?.trim()?.ifBlank { "image" } ?: "image",
            copyright = autorLimpio,
            isFavorite = isFavorite
        )
    }

    private fun limpiarHtml(texto: String?): String {
        if (texto.isNullOrBlank()) return ""
        return texto
            .replace(Regex("<[^>]*>"), "")
            .replace("&nbsp;", " ")
            .replace("&amp;", "&")
            .replace("&quot;", "\"")
            .replace("&#39;", "'")
            .replace("&lt;", "<")
            .replace("&gt;", ">")
            .replace("&#8211;", "–")
            .trim()
    }
}
