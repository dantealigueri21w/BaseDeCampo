package pe.appmobile.basedecampo.ui.screens

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import pe.appmobile.basedecampo.data.entity.InsigniaEntity
import pe.appmobile.basedecampo.domain.model.Expedicion
import pe.appmobile.basedecampo.domain.model.TipoVariable
import pe.appmobile.basedecampo.ui.theme.BaseDeCampoTheme
import pe.appmobile.basedecampo.ui.viewmodel.CuadernoUiState
import pe.appmobile.basedecampo.ui.viewmodel.ExpedicionConEstado

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class CuadernoScreenTest {
    @get:Rule
    val compose = createComposeRule()

    private val expedicionDePrueba = Expedicion(
        id = "frio_puna", nombre = "El Frío de la Puna", pregunta = "¿Pregunta?",
        variableAMedir = TipoVariable.TEMPERATURA, instrumentoCorrectoId = "termometro",
        pasos = emptyList(), repeticionesMinimas = 3,
    )

    @Test
    fun `la pantalla del cuaderno no revienta con expediciones e insignias reales`() {
        compose.setContent {
            BaseDeCampoTheme {
                CuadernoScreen(
                    uiState = CuadernoUiState(
                        expediciones = listOf(ExpedicionConEstado(expedicionDePrueba, sellado = true)),
                        insignias = listOf(InsigniaEntity("primer_plan", "Primer Plan", "Sella tu primer plan", fechaObtenida = 1000L)),
                        cargando = false,
                    ),
                    onVolver = {},
                )
            }
        }
    }

    @Test
    fun `tocar volver dispara el callback`() {
        var volvio = false
        compose.setContent {
            BaseDeCampoTheme {
                CuadernoScreen(
                    uiState = CuadernoUiState(cargando = false),
                    onVolver = { volvio = true },
                )
            }
        }
        compose.onNodeWithContentDescription("Volver").performClick()
        assertTrue(volvio)
    }

    @Test
    fun `una insignia no obtenida muestra su descripcion como pendiente`() {
        compose.setContent {
            BaseDeCampoTheme {
                CuadernoScreen(
                    uiState = CuadernoUiState(
                        insignias = listOf(InsigniaEntity("primer_plan", "Primer Plan", "Sella tu primer plan", fechaObtenida = null)),
                        cargando = false,
                    ),
                    onVolver = {},
                )
            }
        }
        compose.onNodeWithContentDescription("Primer Plan, todavía no obtenida").assertExists()
    }
}
