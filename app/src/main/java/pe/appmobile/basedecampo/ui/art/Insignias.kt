package pe.appmobile.basedecampo.ui.art

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

private val AzulPizarra = Color(0xFF4A6670)
private val KakiOscuro = Color(0xFF8A7355)
private val MarronNoche = Color(0xFF2A2521)
private val NaranjaFogata = Color(0xFFD97B3F)
private val CremaMapa = Color(0xFFF5F1E8)

private val TODAS_LAS_INSIGNIAS: Map<String, DrawScope.(Offset, Float) -> Unit> = mapOf(
    "primer_plan" to { c, r -> dibujarSimboloPrimerPlan(c, r) },
    "instrumento_correcto" to { c, r -> dibujarSimboloInstrumentoCorrecto(c, r) },
    "secuencia_perfecta" to { c, r -> dibujarSimboloSecuenciaPerfecta(c, r) },
    "repeticion_sabia" to { c, r -> dibujarSimboloRepeticionSabia(c, r) },
    "base_completa" to { c, r -> dibujarSimboloBaseCompleta(c, r) },
    "planificador_veloz" to { c, r -> dibujarSimboloPlanificadorVeloz(c, r) },
    "cuaderno_lleno" to { c, r -> dibujarSimboloCuadernoLleno(c, r) },
    "ruta_dificil" to { c, r -> dibujarSimboloRutaDificil(c, r) },
    "sin_atajos" to { c, r -> dibujarSimboloSinAtajos(c, r) },
    "mentor_de_tuco" to { c, r -> dibujarSimboloMentorDeTuco(c, r) },
    "racha_de_campamento" to { c, r -> dibujarSimboloRachaDeCampamento(c, r) },
)

/**
 * Una de las 11 insignias -- círculo base con degradado, anillo de contorno y un símbolo propio
 * dibujado con Path/curvas en el centro (sección 3 de la especificación de arte). [obtenida]
 * atenúa el círculo cuando todavía no se ganó, para que el progreso se lea de un vistazo.
 */
@Composable
fun Insignia(insigniaId: String, obtenida: Boolean, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val radio = size.minDimension / 2
        val centro = Offset(size.width / 2, size.height / 2)
        val alpha = if (obtenida) 1f else 0.35f

        drawCircle(
            brush = Brush.radialGradient(colors = listOf(CremaMapa, AzulPizarra), center = centro, radius = radio),
            radius = radio, center = centro, alpha = alpha,
        )
        drawCircle(color = MarronNoche, radius = radio, center = centro, style = Stroke(width = radio * 0.12f), alpha = alpha)

        TODAS_LAS_INSIGNIAS[insigniaId]?.invoke(this, centro, radio)
    }
}

private fun DrawScope.dibujarSimboloPrimerPlan(centro: Offset, radio: Float) {
    val pergamino = Path().apply {
        moveTo(centro.x - radio * 0.35f, centro.y - radio * 0.3f)
        lineTo(centro.x + radio * 0.2f, centro.y - radio * 0.3f)
        cubicTo(
            centro.x + radio * 0.35f, centro.y - radio * 0.25f,
            centro.x + radio * 0.35f, centro.y - radio * 0.1f,
            centro.x + radio * 0.2f, centro.y - radio * 0.1f,
        )
        lineTo(centro.x - radio * 0.35f, centro.y - radio * 0.1f)
        close()
    }
    drawPath(pergamino, color = CremaMapa)
    drawLine(NaranjaFogata, Offset(centro.x - radio * 0.2f, centro.y - radio * 0.2f), Offset(centro.x + radio * 0.05f, centro.y - radio * 0.2f), strokeWidth = radio * 0.05f)
}

private fun DrawScope.dibujarSimboloInstrumentoCorrecto(centro: Offset, radio: Float) {
    drawLine(CremaMapa, Offset(centro.x - radio * 0.3f, centro.y - radio * 0.15f), Offset(centro.x + radio * 0.3f, centro.y + radio * 0.15f), strokeWidth = radio * 0.08f, cap = StrokeCap.Round)
    drawCircle(color = CremaMapa, radius = radio * 0.28f, center = Offset(centro.x - radio * 0.15f, centro.y + radio * 0.15f), style = Stroke(width = radio * 0.05f))
    drawLine(CremaMapa, Offset(centro.x + radio * 0.15f, centro.y - radio * 0.25f), Offset(centro.x - radio * 0.15f, centro.y + radio * 0.05f), strokeWidth = radio * 0.06f, cap = StrokeCap.Round)
}

private fun DrawScope.dibujarSimboloSecuenciaPerfecta(centro: Offset, radio: Float) {
    val camino = Path().apply {
        moveTo(centro.x - radio * 0.35f, centro.y)
        quadraticTo(centro.x - radio * 0.1f, centro.y - radio * 0.25f, centro.x, centro.y)
        quadraticTo(centro.x + radio * 0.1f, centro.y + radio * 0.25f, centro.x + radio * 0.35f, centro.y)
    }
    drawPath(camino, color = CremaMapa, style = Stroke(width = radio * 0.05f))
    listOf(-0.35f, 0f, 0.35f).forEach { dx ->
        drawCircle(color = CremaMapa, radius = radio * 0.07f, center = Offset(centro.x + radio * dx, centro.y))
    }
}

private fun DrawScope.dibujarSimboloRepeticionSabia(centro: Offset, radio: Float) {
    listOf(0.35f, 0.22f, 0.1f).forEach { factor ->
        val anillo = Path().apply {
            moveTo(centro.x, centro.y - radio * factor)
            cubicTo(
                centro.x + radio * factor, centro.y - radio * factor * 0.5f,
                centro.x + radio * factor, centro.y + radio * factor * 0.5f,
                centro.x, centro.y + radio * factor,
            )
            cubicTo(
                centro.x - radio * factor, centro.y + radio * factor * 0.5f,
                centro.x - radio * factor, centro.y - radio * factor * 0.5f,
                centro.x, centro.y - radio * factor,
            )
        }
        drawPath(anillo, color = CremaMapa, style = Stroke(width = radio * 0.035f))
    }
}

private fun DrawScope.dibujarSimboloBaseCompleta(centro: Offset, radio: Float) {
    val escudo = Path().apply {
        moveTo(centro.x, centro.y - radio * 0.4f)
        lineTo(centro.x + radio * 0.3f, centro.y - radio * 0.25f)
        lineTo(centro.x + radio * 0.3f, centro.y + radio * 0.05f)
        cubicTo(centro.x + radio * 0.3f, centro.y + radio * 0.35f, centro.x, centro.y + radio * 0.45f, centro.x, centro.y + radio * 0.45f)
        cubicTo(centro.x, centro.y + radio * 0.45f, centro.x - radio * 0.3f, centro.y + radio * 0.35f, centro.x - radio * 0.3f, centro.y + radio * 0.05f)
        lineTo(centro.x - radio * 0.3f, centro.y - radio * 0.25f)
        close()
    }
    drawPath(escudo, color = CremaMapa, style = Stroke(width = radio * 0.04f))
    for (i in 0 until 8) {
        val angulo = (i / 8f) * 2 * Math.PI
        val px = centro.x + (radio * 0.42f * kotlin.math.cos(angulo)).toFloat()
        val py = centro.y + (radio * 0.42f * kotlin.math.sin(angulo)).toFloat()
        drawCircle(color = NaranjaFogata, radius = radio * 0.035f, center = Offset(px, py))
    }
}

private fun DrawScope.dibujarSimboloPlanificadorVeloz(centro: Offset, radio: Float) {
    val rollo = Path().apply {
        moveTo(centro.x - radio * 0.4f, centro.y + radio * 0.15f)
        lineTo(centro.x + radio * 0.4f, centro.y + radio * 0.15f)
        cubicTo(centro.x + radio * 0.45f, centro.y + radio * 0.15f, centro.x + radio * 0.45f, centro.y + radio * 0.3f, centro.x + radio * 0.4f, centro.y + radio * 0.3f)
        lineTo(centro.x - radio * 0.4f, centro.y + radio * 0.3f)
        cubicTo(centro.x - radio * 0.45f, centro.y + radio * 0.3f, centro.x - radio * 0.45f, centro.y + radio * 0.15f, centro.x - radio * 0.4f, centro.y + radio * 0.15f)
        close()
    }
    drawPath(rollo, color = KakiOscuro.copy(alpha = 0.5f), style = Stroke(width = radio * 0.03f))
    val rayo = Path().apply {
        moveTo(centro.x + radio * 0.1f, centro.y - radio * 0.4f)
        lineTo(centro.x - radio * 0.15f, centro.y - radio * 0.05f)
        lineTo(centro.x + radio * 0.05f, centro.y - radio * 0.05f)
        lineTo(centro.x - radio * 0.1f, centro.y + radio * 0.1f)
    }
    drawPath(rayo, color = NaranjaFogata, style = Stroke(width = radio * 0.05f, cap = StrokeCap.Round))
}

private fun DrawScope.dibujarSimboloCuadernoLleno(centro: Offset, radio: Float) {
    val cuaderno = Path().apply {
        moveTo(centro.x - radio * 0.3f, centro.y - radio * 0.35f)
        lineTo(centro.x + radio * 0.3f, centro.y - radio * 0.35f)
        lineTo(centro.x + radio * 0.3f, centro.y + radio * 0.35f)
        cubicTo(centro.x, centro.y + radio * 0.25f, centro.x, centro.y + radio * 0.45f, centro.x - radio * 0.3f, centro.y + radio * 0.35f)
        close()
    }
    drawPath(cuaderno, color = CremaMapa, style = Stroke(width = radio * 0.04f))
    drawLine(NaranjaFogata, Offset(centro.x, centro.y - radio * 0.35f), Offset(centro.x, centro.y + radio * 0.4f), strokeWidth = radio * 0.025f)
}

private fun DrawScope.dibujarSimboloRutaDificil(centro: Offset, radio: Float) {
    listOf(-0.15f, 0f, 0.15f).forEachIndexed { i, offsetX ->
        val linea = Path().apply {
            moveTo(centro.x - radio * 0.3f + radio * offsetX, centro.y - radio * 0.35f)
            cubicTo(
                centro.x + radio * 0.1f, centro.y - radio * 0.1f,
                centro.x - radio * 0.1f, centro.y + radio * 0.1f,
                centro.x + radio * 0.05f, centro.y + radio * 0.35f,
            )
        }
        drawPath(linea, color = CremaMapa, style = Stroke(width = radio * (0.02f + i * 0.015f)))
    }
}

private fun DrawScope.dibujarSimboloSinAtajos(centro: Offset, radio: Float) {
    drawLine(
        CremaMapa,
        Offset(centro.x - radio * 0.4f, centro.y),
        Offset(centro.x + radio * 0.4f, centro.y),
        strokeWidth = radio * 0.08f,
        cap = StrokeCap.Round,
    )
}

private fun DrawScope.dibujarSimboloMentorDeTuco(centro: Offset, radio: Float) {
    val pluma = Path().apply {
        moveTo(centro.x - radio * 0.1f, centro.y - radio * 0.4f)
        cubicTo(centro.x + radio * 0.15f, centro.y - radio * 0.3f, centro.x + radio * 0.15f, centro.y, centro.x - radio * 0.05f, centro.y + radio * 0.1f)
        cubicTo(centro.x - radio * 0.2f, centro.y, centro.x - radio * 0.2f, centro.y - radio * 0.3f, centro.x - radio * 0.1f, centro.y - radio * 0.4f)
        close()
    }
    drawPath(pluma, color = CremaMapa)
    val mano = Path().apply {
        moveTo(centro.x - radio * 0.3f, centro.y + radio * 0.3f)
        quadraticTo(centro.x, centro.y + radio * 0.15f, centro.x + radio * 0.3f, centro.y + radio * 0.3f)
    }
    drawPath(mano, color = CremaMapa, style = Stroke(width = radio * 0.04f, cap = StrokeCap.Round))
}

private fun DrawScope.dibujarSimboloRachaDeCampamento(centro: Offset, radio: Float) {
    val llama = Path().apply {
        moveTo(centro.x, centro.y - radio * 0.4f)
        cubicTo(centro.x + radio * 0.25f, centro.y - radio * 0.1f, centro.x + radio * 0.2f, centro.y + radio * 0.2f, centro.x, centro.y + radio * 0.35f)
        cubicTo(centro.x - radio * 0.2f, centro.y + radio * 0.2f, centro.x - radio * 0.25f, centro.y - radio * 0.1f, centro.x, centro.y - radio * 0.4f)
        close()
    }
    drawPath(llama, brush = Brush.verticalGradient(listOf(NaranjaFogata, Color(0xFFF2B85B))))
    listOf(0.1f, 0.2f, 0.3f).forEach { dx ->
        drawLine(KakiOscuro, Offset(centro.x - radio * 0.35f, centro.y + radio * (0.4f + dx)), Offset(centro.x + radio * 0.35f, centro.y + radio * (0.4f + dx)), strokeWidth = radio * 0.03f)
    }
}
