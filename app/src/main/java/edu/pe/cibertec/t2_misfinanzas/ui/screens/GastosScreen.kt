package edu.pe.cibertec.t2_misfinanzas.ui.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.pe.cibertec.t2_misfinanzas.MisFinanzasApplication
import edu.pe.cibertec.t2_misfinanzas.data.local.entity.GastoEntity
import edu.pe.cibertec.t2_misfinanzas.data.local.relations.GastoConCategoria
import edu.pe.cibertec.t2_misfinanzas.ui.viewmodel.GastoViewModelFactory
import edu.pe.cibertec.t2_misfinanzas.ui.viewmodel.GastosViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GastosScreen(
    onNavigateToRegistro: () -> Unit,
    gastoViewModel: GastosViewModel = viewModel (
        factory = GastoViewModelFactory (
            (LocalContext.current.applicationContext as MisFinanzasApplication).gastoRepository,
            (LocalContext.current.applicationContext as MisFinanzasApplication).categoriaRepository
        )
    )
) {
    val gastos by gastoViewModel.gastosConCategoria.collectAsStateWithLifecycle()
    val totalGeneral by gastoViewModel.totalGeneralGastos.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var gastoToDelete by remember { mutableStateOf<GastoConCategoria?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        gastoViewModel.snackbarMessage.collect { message ->
            snackbarHostState.showSnackbar(message)
            gastoViewModel.resetSnackbar()
        }
    }
    Scaffold(
        topBar = { TopAppBar(title = { Text("Mis Gastos") }) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToRegistro,
                containerColor = MaterialTheme.colorScheme.tertiary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Nuevo Gasto")
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->

        LazyColumn (contentPadding = paddingValues) {

            items(gastos) { gasto ->
                GastoItem(
                    gasto = gasto,
                    onClick = {
                        gastoToDelete = gasto
                        showBottomSheet = true
                    }
                )
                Divider()
            }

            item {
                TotalFooter(total = totalGeneral)
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false }
            ) {
                ListItem(
                    headlineContent = { Text("🗑️ Eliminar") },
                    leadingContent = { Icon(Icons.Filled.Delete, contentDescription = "Eliminar") },
                    modifier = Modifier.clickable {
                        showBottomSheet = false
                        showDeleteDialog = true
                    }
                )
                Spacer(Modifier.height(32.dp))
            }
        }

        if (showDeleteDialog && gastoToDelete != null) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Confirmar Eliminación") },
                text = {
                    val monto = "%.2f".format(gastoToDelete!!.monto)
                    Text("¿Eliminar este gasto de $$monto?")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            gastoViewModel.deleteGasto(GastoEntity(
                                id = gastoToDelete!!.id,
                                monto = gastoToDelete!!.monto,
                                descripcion = gastoToDelete!!.descripcion,
                                fecha = gastoToDelete!!.fecha,
                                categoriaId = gastoToDelete!!.categoriaId
                            ))
                            showDeleteDialog = false
                            gastoToDelete = null
                        }
                    ) {
                        Text("Eliminar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("✕ Cancelar")
                    }
                }
            )
        }
    }
}

@Composable
fun GastoItem(gasto: GastoConCategoria, onClick: () -> Unit) {
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    val colorHex = gasto.categoriaColorHex

    val colorInt = android.graphics.Color.parseColor(colorHex)

    val composeColor = Color(colorInt)

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(composeColor),
            contentAlignment = Alignment.Center
        ) {
            Text(text = gasto.categoriaIcono, fontSize = 18.sp)
        }

        Spacer(Modifier.width(16.dp))

        Column (modifier = Modifier.weight(1f)) {
            val displayDescription = if (gasto.descripcion.isNullOrBlank()) {
                gasto.categoriaNombre
            } else {
                gasto.descripcion
            }
            Text(displayDescription, style = MaterialTheme.typography.bodyLarge)
            Text(dateFormatter.format(Date(gasto.fecha)), style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }


        val montoTexto = "-$%.2f".format(gasto.monto)
        Text(
            text = montoTexto,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = Color.Red
        )
    }
}

@Composable
fun TotalFooter(total: Double) {
    val totalFormateado = "-$%.2f".format(total)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.Transparent),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Total:",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = totalFormateado,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
            color = Color.Red
        )
    }
}