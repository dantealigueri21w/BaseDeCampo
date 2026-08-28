package pe.appmobile.basedecampo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import pe.appmobile.basedecampo.data.repository.ExpedicionRepository
import pe.appmobile.basedecampo.ui.screens.ExpedicionScreen
import pe.appmobile.basedecampo.ui.screens.HomeScreen
import pe.appmobile.basedecampo.ui.screens.OnboardingScreen
import pe.appmobile.basedecampo.ui.screens.ParentalGateScreen
import pe.appmobile.basedecampo.ui.viewmodel.ExpedicionViewModel
import pe.appmobile.basedecampo.ui.viewmodel.HomeViewModel

object Rutas {
    const val ONBOARDING = "onboarding"
    const val HOME = "home"
    const val EXPEDICION = "expedicion/{expedicionId}"
    const val PARENTAL_GATE = "parental_gate"
    fun expedicion(id: String) = "expedicion/$id"
}

@Composable
fun NavGraph(repository: ExpedicionRepository, esPrimerLanzamiento: Boolean) {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = if (esPrimerLanzamiento) Rutas.ONBOARDING else Rutas.HOME) {
        composable(Rutas.ONBOARDING) {
            OnboardingScreen(onTerminar = {
                navController.navigate(Rutas.HOME) { popUpTo(Rutas.ONBOARDING) { inclusive = true } }
            })
        }
        composable(Rutas.HOME) {
            val viewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory(repository))
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            HomeScreen(
                uiState = uiState,
                onExpedicionClick = { navController.navigate(Rutas.expedicion(it)) },
                onCuadernoClick = { /* Cuaderno de Planes: pantalla propia pendiente, fuera del alcance de este plan */ },
                onPerfilClick = { navController.navigate(Rutas.PARENTAL_GATE) },
            )
        }
        composable(
            Rutas.EXPEDICION,
            arguments = listOf(navArgument("expedicionId") { type = NavType.StringType }),
        ) { backStackEntry ->
            val expedicionId = backStackEntry.arguments?.getString("expedicionId") ?: return@composable
            val viewModel: ExpedicionViewModel = viewModel(
                factory = ExpedicionViewModel.Factory(repository, expedicionId),
            )
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            ExpedicionScreen(
                uiState = uiState,
                onElegirInstrumento = viewModel::elegirInstrumento,
                onMoverPaso = viewModel::moverPaso,
                onCambiarRepeticiones = viewModel::cambiarRepeticiones,
                onSellarPlan = viewModel::sellarPlan,
            )
        }
        composable(Rutas.PARENTAL_GATE) {
            ParentalGateScreen(repository = repository)
        }
    }
}
