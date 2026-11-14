package pe.edu.esan.diarioespacial.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pe.edu.esan.diarioespacial.data.local.apod.ApodDao
import pe.edu.esan.diarioespacial.data.local.apod.ApodEntity
import pe.edu.esan.diarioespacial.data.local.asteroides.AsteroidDao
import pe.edu.esan.diarioespacial.data.local.asteroides.AsteroidEntity

/**
 * Base de datos Room compartida. Ya registra las dos tablas (una por opción),
 * así cada alumno solo edita su Entity y su DAO, no este archivo.
 *
 * Mientras se desarrolla, si cambian las columnas de una Entity se borra y
 * recrea la base (fallbackToDestructiveMigration), no hace falta migración.
 */
@Database(
    entities = [ApodEntity::class, AsteroidEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun apodDao(): ApodDao
    abstract fun asteroidDao(): AsteroidDao

    companion object {
        private const val NOMBRE_BD = "diario_espacial.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    NOMBRE_BD
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
    }
}
