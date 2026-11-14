package pe.edu.esan.diarioespacial.data.local.apod

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Tabla de la opción Imágenes (Alumno 1).
 *
 * Base mínima: solo la clave primaria (la fecha es única por imagen APOD).
 * TODO (Alumno 1): agregar title, explanation, url, hdurl, mediaType,
 * copyright e isFavorite.
 */
@Entity(tableName = "apod")
data class ApodEntity(
    @PrimaryKey val date: String
)
