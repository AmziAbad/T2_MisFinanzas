package edu.pe.cibertec.t2_misfinanzas.data.local.database

import android.content.Context
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import edu.pe.cibertec.t2_misfinanzas.data.local.dao.CategoriaDao
import edu.pe.cibertec.t2_misfinanzas.data.local.entity.CategoriaEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppDatabaseCallback(
    private val scope: CoroutineScope,
    private val context: Context
) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        scope.launch (Dispatchers.IO) {
            val database = AppDatabase.getDatabase(context, scope)
            database?.let { dbInstance ->
                populateDatabase(dbInstance.categoriaDao())
            }
        }
    }

    private suspend fun populateDatabase(categoriaDao: CategoriaDao) {

        val categorias = listOf(
            CategoriaEntity(id = 1, nombre = "Alimentación", icono = "🍽️", colorHex = "#4CAF50", limiteMensual = 800.00),
            CategoriaEntity(id = 2, nombre = "Transporte", icono = "🚌", colorHex = "#2196F3", limiteMensual = 300.00),
            CategoriaEntity(id = 3, nombre = "Entretenimiento", icono = "🎬", colorHex = "#9C27B0", limiteMensual = 200.00),
            CategoriaEntity(id = 4, nombre = "Vivienda", icono = "🏠", colorHex = "#F44336", limiteMensual = 1500.00),
            CategoriaEntity(id = 5, nombre = "Salud", icono = "💊", colorHex = "#F44336", limiteMensual = 400.00),
            CategoriaEntity(id = 6, nombre = "Café/Bebidas", icono = "☕", colorHex = "#795548", limiteMensual = 150.00),
            CategoriaEntity(id = 7, nombre = "Compras", icono = "🛒", colorHex = "#FF9800", limiteMensual = 500.00),
            CategoriaEntity(id = 8, nombre = "Otros", icono = "📦", colorHex = "#9E9E9E", limiteMensual = 300.00)
        )
        categoriaDao.insertAll(categorias)
    }
}