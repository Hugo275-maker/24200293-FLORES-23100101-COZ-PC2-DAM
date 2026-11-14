package pe.edu.esan.diarioespacial.data.local.asteroides

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Tabla de la opción Asteroides (Alumno 2).
 *
 * Los valores numéricos ya se guardan convertidos a número (la API los manda
 * como texto) y en las unidades que se muestran: metros, km/h y km.
 * Si un dato no llegó en la respuesta se guarda como null.
 */
@Entity(tableName = "asteroides")
data class AsteroidEntity(
    @PrimaryKey val id: String,
    val nombre: String,
    /** Fecha de aproximación en formato AAAA-MM-DD (null si no vino). */
    @ColumnInfo(name = "fecha_aproximacion") val fechaAproximacion: String?,
    /** Momento exacto de la aproximación (epoch en ms), para ordenar dentro del mismo día. */
    @ColumnInfo(name = "epoch_aproximacion") val epochAproximacion: Long?,
    @ColumnInfo(name = "diametro_min_m") val diametroMinM: Double?,
    @ColumnInfo(name = "diametro_max_m") val diametroMaxM: Double?,
    @ColumnInfo(name = "velocidad_kmh") val velocidadKmh: Double?,
    @ColumnInfo(name = "distancia_km") val distanciaKm: Double?,
    @ColumnInfo(name = "es_peligroso") val esPeligroso: Boolean,
    @ColumnInfo(name = "es_favorito") val esFavorito: Boolean = false
)
