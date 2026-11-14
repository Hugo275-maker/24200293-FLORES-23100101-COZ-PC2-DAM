package pe.edu.esan.diarioespacial.data.local.asteroides

import androidx.room.Dao
import androidx.room.Query

/**
 * DAO de la opción Asteroides (Alumno 2).
 * TODO (Alumno 2): consultas con Flow para la lista (con filtro de peligrosos),
 * el detalle y los favoritos.
 */
@Dao
interface AsteroidDao {

    @Query("SELECT COUNT(*) FROM asteroides")
    suspend fun contar(): Int
}
