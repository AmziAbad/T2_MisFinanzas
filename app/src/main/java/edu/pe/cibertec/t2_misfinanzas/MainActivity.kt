package edu.pe.cibertec.t2_misfinanzas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edu.pe.cibertec.t2_misfinanzas.ui.screens.GastosScreen
import edu.pe.cibertec.t2_misfinanzas.ui.screens.RegistroGastoScreen
import edu.pe.cibertec.t2_misfinanzas.ui.theme.T2_MisFinanzasTheme

object Destinations {
    const val GASTOS_LISTA = "gastos_lista"
    const val REGISTRO_GASTO = "registro_gasto"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            T2_MisFinanzasTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Destinations.GASTOS_LISTA
    ) {
        composable (Destinations.GASTOS_LISTA) {
            GastosScreen(
                onNavigateToRegistro = {
                    navController.navigate(Destinations.REGISTRO_GASTO)
                }
            )
        }

        composable(Destinations.REGISTRO_GASTO) {
            RegistroGastoScreen (
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
