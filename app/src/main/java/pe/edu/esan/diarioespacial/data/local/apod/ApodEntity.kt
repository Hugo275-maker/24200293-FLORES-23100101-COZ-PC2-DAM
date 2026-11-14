package pe.edu.esan.diarioespacial.data.local.apod

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Tabla de la opción Imágenes (Alumno 1).
 * Almacena las imágenes APOD obtenidas de la API de la NASA y el estado de favoritos.
 */
@Entity(tableName = "apod")
data class ApodEntity(
    @PrimaryKey val date: String,
    val title: String,
    val explanation: String,
    val url: String?,
    val hdurl: String?,
    val mediaType: String,
    val copyright: String?,
    val isFavorite: Boolean = false
)
