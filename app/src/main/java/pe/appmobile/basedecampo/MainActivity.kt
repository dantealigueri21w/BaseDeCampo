package pe.appmobile.basedecampo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import pe.appmobile.basedecampo.ui.navigation.NavGraph
import pe.appmobile.basedecampo.ui.theme.BaseDeCampoTheme

/**
 * El primer-lanzamiento se decide de forma asíncrona (LaunchedEffect), nunca con runBlocking en
 * onCreate: bloquear el hilo principal esperando a Room (que además puede tener que crear el
 * archivo de la base de datos la primera vez) es la causa clásica de ANR al arrancar en frío.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as BaseDeCampoApplication
        setContent {
            BaseDeCampoTheme {
                var esPrimerLanzamiento by remember { mutableStateOf<Boolean?>(null) }
                LaunchedEffect(Unit) {
                    esPrimerLanzamiento = app.repository.obtenerExpedicionesConPasos().isEmpty()
                }
                esPrimerLanzamiento?.let { primero ->
                    NavGraph(repository = app.repository, esPrimerLanzamiento = primero)
                }
            }
        }
    }
}
