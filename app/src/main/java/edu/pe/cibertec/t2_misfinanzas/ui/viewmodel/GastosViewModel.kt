package edu.pe.cibertec.t2_misfinanzas.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.pe.cibertec.t2_misfinanzas.data.local.entity.GastoEntity
import edu.pe.cibertec.t2_misfinanzas.data.local.relations.GastoConCategoria
import edu.pe.cibertec.t2_misfinanzas.data.repository.CategoriaRepository
import edu.pe.cibertec.t2_misfinanzas.data.repository.GastoRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar

class GastosViewModel (
    private val gastoRepository: GastoRepository,
    private val categoriaRepository: CategoriaRepository
) : ViewModel() {
    private val _gastosConCategoria = MutableStateFlow<List<GastoConCategoria>>(emptyList())
    val gastosConCategoria: StateFlow<List<GastoConCategoria>> = _gastosConCategoria.asStateFlow()

    val totalGeneralGastos: StateFlow<Double> = _gastosConCategoria.map { lista ->
        lista.sumOf { it.monto } * -1
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0.0
    )

    private val _snackbarMessage = MutableSharedFlow<String>(replay = 1)
    val snackbarMessage: SharedFlow<String> = _snackbarMessage.asSharedFlow()

    init {
        collectAllGastos()
    }

    private fun collectAllGastos() {
        viewModelScope.launch {
            gastoRepository.getAllGastosConCategoria().collect { lista ->
                _gastosConCategoria.value = lista
            }
        }
    }

    fun insertGasto(gasto: GastoEntity) {
        viewModelScope.launch {
            gastoRepository.insertGasto(gasto)

            val calendarActual = Calendar.getInstance().apply { timeInMillis = gasto.fecha }
            calendarActual.set(Calendar.DAY_OF_MONTH, 1)
            calendarActual.set(Calendar.HOUR_OF_DAY, 0)
            calendarActual.set(Calendar.MINUTE, 0)
            calendarActual.set(Calendar.SECOND, 0)
            calendarActual.set(Calendar.MILLISECOND, 0)
            val inicioDelMes = calendarActual.timeInMillis

            val calendarSiguiente = Calendar.getInstance().apply { timeInMillis = gasto.fecha }
            calendarSiguiente.add(Calendar.MONTH, 1)
            calendarSiguiente.set(Calendar.DAY_OF_MONTH, 1)
            calendarSiguiente.set(Calendar.HOUR_OF_DAY, 0)
            calendarSiguiente.set(Calendar.MINUTE, 0)
            calendarSiguiente.set(Calendar.SECOND, 0)
            calendarSiguiente.set(Calendar.MILLISECOND, 0)
            val inicioDelSiguienteMes = calendarSiguiente.timeInMillis

            val categoria = categoriaRepository.getCategoriaById(gasto.categoriaId)


            val totalGastado = gastoRepository.getTotalGastadoEnMesPorCategoria(
                categoriaId = gasto.categoriaId,
                inicioDelMes = inicioDelMes,
                inicioDelSiguienteMes = inicioDelSiguienteMes
            )

            val limite = categoria?.limiteMensual ?: 0.0

            if (totalGastado > limite) {
                val excedente = totalGastado - limite
                _snackbarMessage.emit("⚠️ Has excedido el límite de ${categoria?.nombre ?: "Categoría Desconocida"}: $${"%.2f".format(excedente)}")
            } else {
                _snackbarMessage.emit("✓ Gasto guardado correctamente")
            }
        }
    }
    fun resetSnackbar() {
        _snackbarMessage.resetReplayCache()
    }

    fun deleteGasto(gasto: GastoEntity) {
        viewModelScope.launch {
            gastoRepository.deleteGasto(gasto)
            _snackbarMessage.emit("Gasto eliminado")
        }
    }

}