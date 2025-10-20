package edu.pe.cibertec.t2_misfinanzas.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "gastos",
    foreignKeys = [
        ForeignKey(
            entity = CategoriaEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoria_id"],
            onDelete = ForeignKey.RESTRICT
        )
    ]
)
data class GastoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "monto")
    val monto: Double,

    @ColumnInfo(name = "descripcion")
    val descripcion: String?,

    @ColumnInfo(name = "fecha")
    val fecha: Long,

    @ColumnInfo(name = "categoria_id")
    val categoriaId: Int
)
