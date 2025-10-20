package edu.pe.cibertec.t2_misfinanzas.data.local.relations

data class GastoConCategoria(
    val id: Int,
    val monto: Double,
    val descripcion: String?,
    val fecha: Long,
    val categoriaId: Int,
    val categoriaNombre: String,
    val categoriaIcono: String,
    val categoriaColorHex: String
)
