package pe.appmobile.basedecampo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import kotlinx.coroutines.runBlocking
import pe.appmobile.basedecampo.ui.navigation.NavGraph
import pe.appmobile.basedecampo.ui.theme.BaseDeCampoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as BaseDeCampoApplication
        val esPrimerLanzamiento = runBlocking { app.repository.obtenerExpedicionesConPasos().isEmpty() }
        setContent {
            BaseDeCampoTheme {
                NavGraph(repository = app.repository, esPrimerLanzamiento = esPrimerLanzamiento)
            }
        }
    }
}
