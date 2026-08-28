package pe.appmobile.basedecampo.ui.screens

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import pe.appmobile.basedecampo.domain.model.Expedicion
import pe.appmobile.basedecampo.domain.model.TipoVariable
import pe.appmobile.basedecampo.ui.theme.BaseDeCampoTheme
import pe.appmobile.basedecampo.ui.viewmodel.ExpedicionConEstado
import pe.appmobile.basedecampo.ui.viewmodel.HomeUiState

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class HomeScreenTest {
    @get:Rule
    val compose = createComposeRule()

    private val expedicionDePrueba = Expedicion(
        id = "frio_puna", nombre = "El Frío de la Puna", pregunta = "¿Pregunta?",
        variableAMedir = TipoVariable.TEMPERATURA, instrumentoCorrectoId = "termometro",
        pasos = emptyList(), repeticionesMinimas = 3,
    )

    @Test
    fun `la pantalla de inicio no revienta con datos reales`() {
        compose.setContent {
            BaseDeCampoTheme {
                HomeScreen(
                    uiState = HomeUiState(
                        expediciones = listOf(ExpedicionConEstado(expedicionDePrueba, sellado = false)),
                        cargando = false,
                    ),
                    onExpedicionClick = {},
                    onCuadernoClick = {},
                    onPerfilClick = {},
                )
            }
        }
    }

    @Test
    fun `tocar el poste de una expedicion sin sellar dispara la navegacion con su id`() {
        var idTocado: String? = null
        compose.setContent {
            BaseDeCampoTheme {
                HomeScreen(
                    uiState = HomeUiState(
                        expediciones = listOf(ExpedicionConEstado(expedicionDePrueba, sellado = false)),
                        cargando = false,
                    ),
                    onExpedicionClick = { idTocado = it },
                    onCuadernoClick = {},
                    onPerfilClick = {},
                )
            }
        }
        compose.onNodeWithContentDescription("El Frío de la Puna, Sin plan todavía").performClick()
        assertEquals("frio_puna", idTocado)
    }
}
