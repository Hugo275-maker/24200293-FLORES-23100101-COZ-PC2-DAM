package pe.edu.esan.diarioespacial.data.local.asteroides

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Tabla de la opción Asteroides (Alumno 2).
 *
 * Base mínima: solo la clave primaria (id que entrega NeoWs).
 * TODO (Alumno 2): agregar name, fecha de aproximación, diámetro mín./máx. (m),
 * velocidad (km/h), distancia (km), isHazardous e isFavorite.
 */
@Entity(tableName = "asteroides")
data class AsteroidEntity(
    @PrimaryKey val id: String
)
