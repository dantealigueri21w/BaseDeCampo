package pe.appmobile.basedecampo.ui.screens

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import pe.appmobile.basedecampo.ui.theme.BaseDeCampoTheme

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class OnboardingScreenTest {
    @get:Rule
    val compose = createComposeRule()

    @Test
    fun `la primera pantalla no revienta y muestra el titulo de bienvenida`() {
        compose.setContent {
            BaseDeCampoTheme {
                OnboardingScreen(onTerminar = {})
            }
        }
        compose.onNodeWithText("Bienvenido a Base de Campo").assertExists()
    }

    @Test
    fun `tocar Continuar tres veces llega a la ultima pantalla con el boton Empezar`() {
        compose.setContent {
            BaseDeCampoTheme {
                OnboardingScreen(onTerminar = {})
            }
        }
        repeat(3) {
            compose.onNodeWithText("Continuar").performClick()
        }
        compose.onNodeWithText("Empezar").assertExists()
    }

    @Test
    fun `tocar Empezar en la ultima pantalla dispara onTerminar`() {
        var terminado = false
        compose.setContent {
            BaseDeCampoTheme {
                OnboardingScreen(onTerminar = { terminado = true })
            }
        }
        repeat(3) {
            compose.onNodeWithText("Continuar").performClick()
        }
        compose.onNodeWithText("Empezar").performClick()
        assertTrue(terminado)
    }
}
