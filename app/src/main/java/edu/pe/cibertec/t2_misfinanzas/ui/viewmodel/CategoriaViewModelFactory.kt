package edu.pe.cibertec.t2_misfinanzas.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import edu.pe.cibertec.t2_misfinanzas.data.repository.CategoriaRepository

class CategoriaViewModelFactory (
    private val categoriaRepository: CategoriaRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoriaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CategoriaViewModel(categoriaRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}