package edu.pe.cibertec.t2_misfinanzas

import android.app.Application
import edu.pe.cibertec.t2_misfinanzas.data.local.database.AppDatabase
import edu.pe.cibertec.t2_misfinanzas.data.repository.CategoriaRepository
import edu.pe.cibertec.t2_misfinanzas.data.repository.GastoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MisFinanzasApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())
    private val database by lazy { AppDatabase.getDatabase(this, applicationScope) }


    val categoriaRepository by lazy {
        CategoriaRepository(database.categoriaDao())
    }

    val gastoRepository by lazy {
        GastoRepository(database.gastoDao())
    }

    override fun onCreate() {
        super.onCreate()
    }
}