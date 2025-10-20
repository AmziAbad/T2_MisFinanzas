package edu.pe.cibertec.t2_misfinanzas.data.local.dao

import androidx.room.*
import edu.pe.cibertec.t2_misfinanzas.data.local.entity.GastoEntity
import edu.pe.cibertec.t2_misfinanzas.data.local.relations.GastoConCategoria
import kotlinx.coroutines.flow.Flow

@Dao
interface GastoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    // Devuelve el ID de la fila insertada (Long)
    suspend fun insertGasto(gasto: GastoEntity): Long

    @Update
    suspend fun updateGasto(gasto: GastoEntity)

    @Delete
    suspend fun deletedGasto(gasto: GastoEntity)

    @Query("SELECT * FROM gastos ORDER BY fecha DESC")
    fun getAllGastos(): Flow<List<GastoEntity>>

    @Query("SELECT * FROM gastos WHERE categoria_id = :categoriaId ORDER BY fecha DESC")
    fun getGastosByCategoria(categoriaId: Int): Flow<List<GastoEntity>>

    @Query("SELECT * FROM gastos WHERE id = :gastoId")
    suspend fun getGastoById(gastoId: Int): GastoEntity?

    @Query("""
    SELECT 
    G.id, G.monto, G.descripcion, G.fecha,
    G.categoria_id AS categoriaId,
    C.nombre AS categoriaNombre, 
    C.icono AS categoriaIcono,
    C.color_hex AS categoriaColorHex 
    FROM gastos AS G 
    INNER JOIN categorias AS C ON G.categoria_id = C.id
    ORDER BY G.fecha DESC
    """)
    fun getAllGastosConCategoria(): Flow<List<GastoConCategoria>>

    @Query("""
    SELECT SUM(monto) 
    FROM gastos 
    WHERE categoria_id = :categoriaId 
    AND fecha >= :inicioDelMes 
    AND fecha < :inicioDelSiguienteMes
    """)
    suspend fun getTotalGastadoEnMesPorCategoria(
        categoriaId: Int,
        inicioDelMes: Long,
        inicioDelSiguienteMes: Long
    ): Double?
}