package pe.appmobile.basedecampo.ui.screens

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import pe.appmobile.basedecampo.data.entity.InstrumentoEntity
import pe.appmobile.basedecampo.domain.model.Expedicion
import pe.appmobile.basedecampo.domain.model.PasoProcedimiento
import pe.appmobile.basedecampo.domain.model.TipoVariable
import pe.appmobile.basedecampo.ui.theme.BaseDeCampoTheme
import pe.appmobile.basedecampo.ui.viewmodel.ExpedicionUiState

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExpedicionScreenTest {
    @get:Rule
    val compose = createComposeRule()

    private val expedicionDePrueba = Expedicion(
        id = "frio_puna", nombre = "El Frío de la Puna", pregunta = "¿Cuánto baja la temperatura?",
        variableAMedir = TipoVariable.TEMPERATURA, instrumentoCorrectoId = "termometro",
        pasos = listOf(
            PasoProcedimiento("colocar", "Colocar el termómetro", setOf("leer")),
            PasoProcedimiento("leer", "Leer la temperatura"),
        ),
        repeticionesMinimas = 3,
    )

    @Test
    fun `la pantalla de expedicion no revienta con datos reales`() {
        compose.setContent {
            BaseDeCampoTheme {
                ExpedicionScreen(
                    uiState = ExpedicionUiState(
                        expedicion = expedicionDePrueba,
                        instrumentos = listOf(InstrumentoEntity("termometro", "Termómetro", "TERMOMETRO", "TEMPERATURA")),
                        ordenPasos = listOf("colocar", "leer"),
                    ),
                    onElegirInstrumento = {}, onMoverPaso = { _, _ -> }, onCambiarRepeticiones = {}, onSellarPlan = {},
                )
            }
        }
    }

    @Test
    fun `tocar Sellar plan dispara el callback`() {
        var sellado = false
        compose.setContent {
            BaseDeCampoTheme {
                ExpedicionScreen(
                    uiState = ExpedicionUiState(
                        expedicion = expedicionDePrueba,
                        instrumentos = listOf(InstrumentoEntity("termometro", "Termómetro", "TERMOMETRO", "TEMPERATURA")),
                        ordenPasos = listOf("colocar", "leer"),
                        instrumentoElegidoId = "termometro",
                        repeticionesElegidas = 3,
                    ),
                    onElegirInstrumento = {}, onMoverPaso = { _, _ -> }, onCambiarRepeticiones = {},
                    onSellarPlan = { sellado = true },
                )
            }
        }
        compose.onNodeWithText("Sellar plan").performScrollTo().performClick()
        assertTrue(sellado)
    }

    @Test
    fun `tocar agregar repeticion dispara el callback con el valor incrementado`() {
        var valorRecibido: Int? = null
        compose.setContent {
            BaseDeCampoTheme {
                ExpedicionScreen(
                    uiState = ExpedicionUiState(
                        expedicion = expedicionDePrueba,
                        instrumentos = emptyList(),
                        ordenPasos = listOf("colocar", "leer"),
                        repeticionesElegidas = 2,
                    ),
                    onElegirInstrumento = {}, onMoverPaso = { _, _ -> },
                    onCambiarRepeticiones = { valorRecibido = it }, onSellarPlan = {},
                )
            }
        }
        compose.onNodeWithContentDescription("Agregar una repetición").performScrollTo().performClick()
        assertEquals(3, valorRecibido)
    }
}
