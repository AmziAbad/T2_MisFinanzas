package edu.pe.cibertec.t2_misfinanzas.data.repository

import edu.pe.cibertec.t2_misfinanzas.data.local.dao.GastoDao
import edu.pe.cibertec.t2_misfinanzas.data.local.entity.GastoEntity
import edu.pe.cibertec.t2_misfinanzas.data.local.relations.GastoConCategoria
import kotlinx.coroutines.flow.Flow

class GastoRepository (private val gastoDao: GastoDao){

    fun getAllGastosConCategoria(): Flow<List<GastoConCategoria>> {
        return gastoDao.getAllGastosConCategoria()
    }
    suspend fun insertGasto(gasto: GastoEntity): Long {
        return gastoDao.insertGasto(gasto)
    }
    suspend fun deleteGasto(gasto: GastoEntity) {
        gastoDao.deletedGasto(gasto)
    }
    suspend fun getTotalGastadoEnMesPorCategoria(
        categoriaId: Int,
        inicioDelMes: Long,
        inicioDelSiguienteMes: Long
    ): Double {
        return gastoDao.getTotalGastadoEnMesPorCategoria(
            categoriaId,
            inicioDelMes,
            inicioDelSiguienteMes
        ) ?: 0.0
    }
}