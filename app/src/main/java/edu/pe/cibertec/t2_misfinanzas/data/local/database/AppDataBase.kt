package edu.pe.cibertec.t2_misfinanzas.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import edu.pe.cibertec.t2_misfinanzas.data.local.dao.CategoriaDao
import edu.pe.cibertec.t2_misfinanzas.data.local.dao.GastoDao
import edu.pe.cibertec.t2_misfinanzas.data.local.entity.CategoriaEntity
import edu.pe.cibertec.t2_misfinanzas.data.local.entity.GastoEntity
import kotlinx.coroutines.CoroutineScope

@Database(
    entities = [
        CategoriaEntity::class,
        GastoEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase(){

    abstract fun categoriaDao(): CategoriaDao
    abstract fun gastoDao(): GastoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase (context: Context,applicationScope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "misfinanzas_database"
                )
                    .addCallback(AppDatabaseCallback(applicationScope, context.applicationContext))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}