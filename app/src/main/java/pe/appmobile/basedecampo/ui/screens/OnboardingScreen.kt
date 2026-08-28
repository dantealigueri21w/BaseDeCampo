package pe.appmobile.basedecampo.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pe.appmobile.basedecampo.R
import pe.appmobile.basedecampo.ui.art.IlustracionTuco
import pe.appmobile.basedecampo.ui.art.PoseTuco

private data class PantallaOnboarding(val tituloRes: Int, val textoRes: Int, val pose: PoseTuco)

private val PANTALLAS = listOf(
    PantallaOnboarding(R.string.onboarding_1_titulo, R.string.onboarding_1_texto, PoseTuco.SALUDO),
    PantallaOnboarding(R.string.onboarding_2_titulo, R.string.onboarding_2_texto, PoseTuco.EXPLORANDO),
    PantallaOnboarding(R.string.onboarding_3_titulo, R.string.onboarding_3_texto, PoseTuco.CELEBRANDO),
    PantallaOnboarding(R.string.onboarding_4_titulo, R.string.onboarding_4_texto, PoseTuco.DE_PIE),
)

@Composable
fun OnboardingScreen(onTerminar: () -> Unit) {
    var indice by remember { mutableIntStateOf(0) }
    val pantalla = PANTALLAS[indice]
    val esUltima = indice == PANTALLAS.lastIndex

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        IlustracionTuco(pose = pantalla.pose, modifier = Modifier.size(180.dp))
        Text(stringResource(pantalla.tituloRes), style = MaterialTheme.typography.headlineLarge)
        Text(stringResource(pantalla.textoRes), style = MaterialTheme.typography.bodyLarge)
        Button(onClick = { if (esUltima) onTerminar() else indice++ }) {
            Text(stringResource(if (esUltima) R.string.onboarding_empezar else R.string.onboarding_continuar))
        }
    }
}
