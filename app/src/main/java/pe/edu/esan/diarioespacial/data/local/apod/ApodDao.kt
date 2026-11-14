package pe.edu.esan.diarioespacial.data.local.apod

import androidx.room.Dao
import androidx.room.Query

/**
 * DAO de la opción Imágenes (Alumno 1).
 * TODO (Alumno 1): consultas con Flow para la lista, el detalle y los favoritos.
 */
@Dao
interface ApodDao {

    @Query("SELECT COUNT(*) FROM apod")
    suspend fun contar(): Int
}
