package edu.pe.cibertec.t2_misfinanzas.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import edu.pe.cibertec.t2_misfinanzas.data.repository.CategoriaRepository
import edu.pe.cibertec.t2_misfinanzas.data.repository.GastoRepository

class GastoViewModelFactory (
    private val gastoRepository: GastoRepository,
    private val categoriaRepository: CategoriaRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GastosViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GastosViewModel(gastoRepository, categoriaRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}