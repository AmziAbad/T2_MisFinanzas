package edu.pe.cibertec.t2_misfinanzas.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.pe.cibertec.t2_misfinanzas.data.local.entity.CategoriaEntity
import edu.pe.cibertec.t2_misfinanzas.data.repository.CategoriaRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CategoriaViewModel (
    private val repository: CategoriaRepository
) : ViewModel() {
    private val _categorias = MutableStateFlow<List<CategoriaEntity>>(emptyList())
    val categorias: StateFlow<List<CategoriaEntity>> = _categorias.asStateFlow()

    init {
        collectAllCategories()
    }
    private fun collectAllCategories() {
        viewModelScope.launch {
            repository.getAllCategorias().collect { lista ->
                _categorias.value = lista
            }
        }
    }
}