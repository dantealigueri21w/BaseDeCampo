package pe.appmobile.basedecampo.ui.art

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import pe.appmobile.basedecampo.R

enum class PoseTuco(val resId: Int) {
    SALUDO(R.drawable.tuco_saludo),
    VOLANDO(R.drawable.tuco_volando),
    ESPERANDO(R.drawable.tuco_esperando),
    DE_PIE(R.drawable.tuco_de_pie),
    CELEBRANDO(R.drawable.tuco_celebrando),
    CONFUNDIDO(R.drawable.tuco_confundido),
    EXPLORANDO(R.drawable.tuco_explorando),
    EXPLORANDO_LATERAL(R.drawable.tuco_explorando_lateral),
}

/**
 * Tuco, el personaje guía -- vía archivo real (Downloads/personajes), no Canvas, por decisión
 * de Rodrigo (ver sección 1 de la especificación de arte). Las 8 poses ya están recortadas
 * con fondo transparente en res/drawable-nodpi/.
 */
@Composable
fun IlustracionTuco(pose: PoseTuco, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(pose.resId),
        contentDescription = null,
        modifier = modifier,
    )
}
