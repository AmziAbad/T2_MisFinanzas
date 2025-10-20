package edu.pe.cibertec.t2_misfinanzas.ui.screens
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.pe.cibertec.t2_misfinanzas.MisFinanzasApplication
import edu.pe.cibertec.t2_misfinanzas.data.local.entity.CategoriaEntity
import edu.pe.cibertec.t2_misfinanzas.data.local.entity.GastoEntity
import edu.pe.cibertec.t2_misfinanzas.ui.viewmodel.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroGastoScreen(
    onNavigateBack: () -> Unit,
) {
    val context = LocalContext.current
    val application = context.applicationContext as MisFinanzasApplication

    val gastoViewModel: GastosViewModel = viewModel(
        factory = GastoViewModelFactory(
            gastoRepository = application.gastoRepository,
            categoriaRepository = application.categoriaRepository
        )
    )

    val categoriaViewModel: CategoriaViewModel = viewModel(
        factory = CategoriaViewModelFactory(
            categoriaRepository = application.categoriaRepository
        )
    )

    var montoText by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var fechaTimestamp by remember { mutableStateOf(System.currentTimeMillis()) }
    var selectedCategoria by remember { mutableStateOf<CategoriaEntity?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showCategoriaDropdown by remember { mutableStateOf(false) }
    var showAlertDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    val categorias by categoriaViewModel.categorias.collectAsStateWithLifecycle()
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val scope = rememberCoroutineScope()

    if (showAlertDialog) {
        AlertDialog(
            onDismissRequest = { showAlertDialog = false },
            title = { Text("Error de Validación") },
            text = { Text(dialogMessage) },
            confirmButton = {
                TextButton(onClick = { showAlertDialog = false }) {
                    Text("Aceptar")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Registrar Nuevo Gasto") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {


            OutlinedTextField(
                value = montoText,
                onValueChange = { newValue ->
                    val filtered = newValue.filter { it.isDigit() || it == '.' }
                    montoText = filtered
                },
                label = { Text("Monto") },
                leadingIcon = { Text("$") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))


            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))


            OutlinedTextField(
                value = dateFormatter.format(Date(fechaTimestamp)),
                onValueChange = {  },
                label = { Text("Fecha") },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Seleccionar Fecha"
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true }
            )

            if (showDatePicker) {
                AlertDialog(
                    onDismissRequest = { showDatePicker = false },
                    title = { Text("Selector de Fecha (Simulación)") },
                    text = { Text("Implementar DatePickerDialog aquí.") },
                    confirmButton = {
                        TextButton(onClick = {
                            showDatePicker = false
                        }) {
                            Text("Aceptar")
                        }
                    }
                )
            }
            Spacer(Modifier.height(16.dp))

            ExposedDropdownMenuBox(
                expanded = showCategoriaDropdown,
                onExpandedChange = { showCategoriaDropdown = it },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedCategoria?.nombre ?: "Seleccionar categoría",
                    onValueChange = { /* Read-only */ },
                    readOnly = true,
                    label = { Text("Categoría") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showCategoriaDropdown) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = showCategoriaDropdown,
                    onDismissRequest = { showCategoriaDropdown = false }
                ) {
                    categorias.forEach { categoria ->
                        DropdownMenuItem(
                            text = {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("${categoria.icono} ${categoria.nombre}")
                                    Text("$${"%.2f".format(categoria.limiteMensual)}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            },
                            onClick = {
                                selectedCategoria = categoria
                                showCategoriaDropdown = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val monto = montoText.toDoubleOrNull()
                    if (monto == null || monto <= 0.0) {
                        dialogMessage = "El monto debe ser un número válido mayor a 0."
                        showAlertDialog = true
                        return@Button
                    }
                    if (selectedCategoria == null) {
                        dialogMessage = "Debe seleccionar una categoría para registrar el gasto."
                        showAlertDialog = true
                        return@Button
                    }


                    val nuevoGasto = GastoEntity(
                        monto = monto,
                        descripcion = descripcion.ifBlank { null },
                        fecha = fechaTimestamp,
                        categoriaId = selectedCategoria!!.id
                    )

                    gastoViewModel.insertGasto(nuevoGasto)
                    scope.launch {
                        kotlinx.coroutines.delay(100)
                        onNavigateBack()
                    }

                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
            ) {
                Text("Guardar Gasto")
            }
        }
    }
}