package edu.pe.cibertec.t2_misfinanzas.data.local.dao

import androidx.room.*
import edu.pe.cibertec.t2_misfinanzas.data.local.entity.CategoriaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriaDao {

    @Query("SELECT * FROM categorias ORDER BY nombre ASC")
    fun getAllCategorias(): Flow<List<CategoriaEntity>>

    @Query("SELECT * FROM categorias WHERE id = :categoriaId")
    suspend fun getCategoriaById(categoriaId: Int): CategoriaEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategoria(categoria: CategoriaEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(categorias: List<CategoriaEntity>)

    @Query("SELECT COUNT(*) FROM categorias")
    suspend fun getCount(): Int
}