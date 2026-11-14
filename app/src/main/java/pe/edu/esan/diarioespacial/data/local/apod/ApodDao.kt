package pe.edu.esan.diarioespacial.data.local.apod

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * DAO de la opción Imágenes (Alumno 1).
 * Consultas con Flow para la lista de los últimos 7 días, el detalle y los favoritos.
 */
@Dao
interface ApodDao {

    @Query("SELECT * FROM apod WHERE date >= :fechaInicio ORDER BY date DESC")
    fun getImagenesUltimos7Dias(fechaInicio: String): Flow<List<ApodEntity>>

    @Query("SELECT * FROM apod ORDER BY date DESC")
    fun getTodasLasImagenes(): Flow<List<ApodEntity>>

    @Query("SELECT * FROM apod WHERE date = :date LIMIT 1")
    fun getImagenPorFechaFlow(date: String): Flow<List<ApodEntity>>

    @Query("SELECT * FROM apod WHERE date = :date LIMIT 1")
    fun getImagenSingleFlow(date: String): Flow<ApodEntity?>

    @Query("SELECT * FROM apod WHERE date = :date LIMIT 1")
    suspend fun getImagenPorFecha(date: String): ApodEntity?

    @Query("SELECT * FROM apod WHERE isFavorite = 1 ORDER BY date DESC")
    fun getImagenesFavoritas(): Flow<List<ApodEntity>>

    @Query("SELECT date FROM apod WHERE isFavorite = 1")
    suspend fun getFechasFavoritas(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateImagenes(imagenes: List<ApodEntity>)

    @Query("UPDATE apod SET isFavorite = :isFavorite WHERE date = :date")
    suspend fun updateFavoriteStatus(date: String, isFavorite: Boolean)

    @Query("SELECT COUNT(*) FROM apod")
    suspend fun contar(): Int
}
