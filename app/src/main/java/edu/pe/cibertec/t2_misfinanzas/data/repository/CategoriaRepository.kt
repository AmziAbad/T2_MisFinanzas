package edu.pe.cibertec.t2_misfinanzas.data.repository

import edu.pe.cibertec.t2_misfinanzas.data.local.dao.CategoriaDao
import edu.pe.cibertec.t2_misfinanzas.data.local.entity.CategoriaEntity
import kotlinx.coroutines.flow.Flow

class CategoriaRepository(private val categoriaDao: CategoriaDao) {
    fun getAllCategorias(): Flow<List<CategoriaEntity>> {
        return categoriaDao.getAllCategorias()
    }

    suspend fun insertCategoria(categoria: CategoriaEntity) {
        categoriaDao.insertCategoria(categoria)
    }

    suspend fun getCategoriaById(categoriaId: Int): CategoriaEntity? {
        return categoriaDao.getCategoriaById(categoriaId)
    }
    suspend fun countCategorias(): Int {
        return categoriaDao.getCount()
    }

}