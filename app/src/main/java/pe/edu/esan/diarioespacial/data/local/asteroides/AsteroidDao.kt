package pe.edu.esan.diarioespacial.data.local.asteroides

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

/**
 * DAO de la opción Asteroides (Alumno 2).
 * Las pantallas leen siempre desde aquí (Flow): cuando se guarda la respuesta
 * de la API, la interfaz se actualiza sola.
 */
@Dao
interface AsteroidDao {

    /**
     * Asteroides del rango consultado, de la fecha de aproximación más reciente
     * a la más antigua. Si [soloPeligrosos] es true, solo los potencialmente peligrosos.
     */
    @Query(
        """
        SELECT * FROM asteroides
        WHERE fecha_aproximacion BETWEEN :desde AND :hasta
          AND (:soloPeligrosos = 0 OR es_peligroso = 1)
        ORDER BY fecha_aproximacion DESC, epoch_aproximacion DESC, nombre ASC
        """
    )
    fun observarLista(desde: String, hasta: String, soloPeligrosos: Boolean): Flow<List<AsteroidEntity>>

    @Query("SELECT * FROM asteroides WHERE es_favorito = 1 ORDER BY fecha_aproximacion DESC, epoch_aproximacion DESC")
    fun observarFavoritos(): Flow<List<AsteroidEntity>>

    @Query("SELECT * FROM asteroides WHERE id = :id")
    fun observarPorId(id: String): Flow<AsteroidEntity?>

    @Query("UPDATE asteroides SET es_favorito = :favorito WHERE id = :id")
    suspend fun actualizarFavorito(id: String, favorito: Boolean)

    @Query("SELECT id FROM asteroides WHERE es_favorito = 1")
    suspend fun idsFavoritos(): List<String>

    /** Borra lo descargado antes que no es favorito (los favoritos se conservan). */
    @Query("DELETE FROM asteroides WHERE es_favorito = 0")
    suspend fun borrarNoFavoritos()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodos(asteroides: List<AsteroidEntity>)

    /**
     * Reemplaza los datos con la nueva respuesta de la API sin perder qué
     * asteroides estaban marcados como favoritos.
     */
    @Transaction
    suspend fun reemplazarConservandoFavoritos(nuevos: List<AsteroidEntity>) {
        val favoritos = idsFavoritos().toSet()
        borrarNoFavoritos()
        insertarTodos(nuevos.map { it.copy(esFavorito = it.id in favoritos) })
    }
}
