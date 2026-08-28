package pe.appmobile.basedecampo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val EsquemaClaro = lightColorScheme(
    primary = AzulPizarra,
    onPrimary = Color.White,
    secondary = Kaki,
    onSecondary = Color.White,
    tertiary = NaranjaFogata,
    onTertiary = MarronNoche,
    background = CremaMapa,
    onBackground = MarronNoche,
    surface = CremaMapa,
    onSurface = MarronNoche,
    error = ErrorClaro,
    onError = Color.White,
)

private val EsquemaOscuro = darkColorScheme(
    primary = AzulPizarra,
    onPrimary = Color.White,
    secondary = Kaki,
    onSecondary = Color.White,
    tertiary = NaranjaFogata,
    onTertiary = MarronNoche,
    background = MarronNoche,
    onBackground = CremaMapa,
    surface = MarronNoche,
    onSurface = CremaMapa,
    error = ErrorOscuro,
    onError = MarronNoche,
)

@Composable
fun BaseDeCampoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) EsquemaOscuro else EsquemaClaro,
        typography = BaseDeCampoTypography,
        content = content,
    )
}
