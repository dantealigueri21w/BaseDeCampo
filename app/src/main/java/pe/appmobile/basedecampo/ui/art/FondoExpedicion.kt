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

private val AzulPizarra = Color(0xFF4A6670)
private val KakiOscuro = Color(0xFF8A7355)
private val KakiClaro = Color(0xFFB8A688)
private val MarronNoche = Color(0xFF2A2521)
private val NaranjaFogata = Color(0xFFD97B3F)
private val CremaMapa = Color(0xFFF5F1E8)

/**
 * Fondo completo de una de las 8 expediciones -- 5 capas (cielo, siluetas lejanas, mesa de
 * campamento, objeto central de la expedición, luz de fogata), sección 2 de la especificación
 * de arte. El objeto central cambia según [expedicionId]; las otras 4 capas son compartidas.
 */
@Composable
fun FondoExpedicion(expedicionId: String, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        dibujarCielo()
        dibujarSiluetasLejanas()
        dibujarMesaDeCampamento()
        when (expedicionId) {
            "sombra_sol" -> dibujarVasosComparados()
            "peso_piedra" -> dibujarBalanzaConPiedras()
            "frio_puna" -> dibujarTermometroClavado()
            "semilla_crece" -> dibujarMacetasConBrotes()
            "eco_quebrada" -> dibujarCanonConEco()
            "lupa_insecto" -> dibujarLupaSobreHoja()
            "camino_corto" -> dibujarMapaConRutas()
            else -> dibujarInstrumentosAgrupados()
        }
        dibujarLuzDeFogata()
    }
}

private fun DrawScope.dibujarCielo() {
    drawRect(brush = Brush.verticalGradient(listOf(MarronNoche, AzulPizarra.copy(alpha = 0.55f), NaranjaFogata.copy(alpha = 0.18f))))
}

private fun DrawScope.dibujarSiluetasLejanas() {
    val a = size.width
    val h = size.height
    val siluetas = Path().apply {
        moveTo(0f, h * 0.42f)
        cubicTo(a * 0.15f, h * 0.3f, a * 0.25f, h * 0.4f, a * 0.35f, h * 0.35f)
        cubicTo(a * 0.5f, h * 0.28f, a * 0.6f, h * 0.4f, a * 0.75f, h * 0.33f)
        cubicTo(a * 0.85f, h * 0.29f, a * 0.95f, h * 0.38f, a, h * 0.35f)
        lineTo(a, h * 0.55f)
        lineTo(0f, h * 0.55f)
        close()
    }
    drawPath(siluetas, color = MarronNoche.copy(alpha = 0.55f))
}

private fun DrawScope.dibujarMesaDeCampamento() {
    val a = size.width
    val h = size.height
    val mesa = Path().apply {
        moveTo(a * 0.05f, h * 0.98f)
        lineTo(a * 0.1f, h * 0.55f)
        cubicTo(a * 0.1f, h * 0.52f, a * 0.9f, h * 0.52f, a * 0.9f, h * 0.55f)
        lineTo(a * 0.95f, h * 0.98f)
        close()
    }
    drawPath(mesa, brush = Brush.verticalGradient(listOf(KakiOscuro, MarronNoche.copy(alpha = 0.8f))))
    listOf(0.62f, 0.75f, 0.88f).forEach { py ->
        val veta = Path().apply {
            moveTo(a * 0.14f, h * py)
            cubicTo(a * 0.4f, h * (py - 0.015f), a * 0.6f, h * (py + 0.015f), a * 0.87f, h * py)
        }
        drawPath(veta, color = MarronNoche.copy(alpha = 0.4f), style = Stroke(width = a * 0.004f))
    }
}

private fun DrawScope.dibujarLuzDeFogata() {
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(NaranjaFogata.copy(alpha = 0.4f), Color.Transparent),
            center = Offset(size.width * 0.5f, size.height * 0.58f),
            radius = size.width * 0.45f,
        ),
        radius = size.width * 0.45f,
        center = Offset(size.width * 0.5f, size.height * 0.58f),
    )
}

private fun DrawScope.dibujarVasosComparados() {
    val a = size.width
    val h = size.height
    listOf(0.38f to true, 0.62f to false).forEach { (cx, sol) ->
        val vaso = Path().apply {
            moveTo(a * cx - a * 0.06f, h * 0.6f)
            lineTo(a * cx - a * 0.05f, h * 0.82f)
            cubicTo(a * cx - a * 0.05f, h * 0.87f, a * cx + a * 0.05f, h * 0.87f, a * cx + a * 0.05f, h * 0.82f)
            lineTo(a * cx + a * 0.06f, h * 0.6f)
            close()
        }
        drawPath(vaso, brush = Brush.linearGradient(listOf(Color(0xFF7FA8B8).copy(alpha = 0.5f), CremaMapa.copy(alpha = 0.3f))))
        drawPath(vaso, color = CremaMapa, style = Stroke(width = a * 0.006f))
        if (sol) {
            drawCircle(color = NaranjaFogata, radius = a * 0.025f, center = Offset(a * cx, h * 0.52f))
        } else {
            val nube = Path().apply {
                moveTo(a * cx - a * 0.03f, h * 0.52f)
                quadraticTo(a * cx, h * 0.49f, a * cx + a * 0.03f, h * 0.52f)
            }
            drawPath(nube, color = CremaMapa.copy(alpha = 0.7f), style = Stroke(width = a * 0.012f))
        }
    }
}

private fun DrawScope.dibujarBalanzaConPiedras() {
    val a = size.width
    val h = size.height
    drawLine(KakiClaro, Offset(a * 0.5f, h * 0.58f), Offset(a * 0.5f, h * 0.8f), strokeWidth = a * 0.012f)
    drawLine(KakiClaro, Offset(a * 0.32f, h * 0.66f), Offset(a * 0.68f, h * 0.66f), strokeWidth = a * 0.012f)
    listOf(0.32f, 0.68f).forEachIndexed { i, cx ->
        val platillo = Path().apply {
            moveTo(a * cx - a * 0.08f, h * 0.66f)
            quadraticTo(a * cx, h * 0.78f, a * cx + a * 0.08f, h * 0.66f)
        }
        drawPath(platillo, color = KakiClaro, style = Stroke(width = a * 0.008f))
        val radio = if (i == 0) a * 0.045f else a * 0.06f
        drawCircle(brush = Brush.radialGradient(listOf(Color(0xFFBDBDBD), MarronNoche.copy(alpha = 0.7f))), radius = radio, center = Offset(a * cx, h * 0.72f))
    }
}

private fun DrawScope.dibujarTermometroClavado() {
    val a = size.width
    val h = size.height
    val tubo = Path().apply {
        moveTo(a * 0.47f, h * 0.5f)
        cubicTo(a * 0.44f, h * 0.53f, a * 0.44f, h * 0.72f, a * 0.47f, h * 0.75f)
        lineTo(a * 0.53f, h * 0.75f)
        cubicTo(a * 0.56f, h * 0.72f, a * 0.56f, h * 0.53f, a * 0.53f, h * 0.5f)
        close()
    }
    drawPath(tubo, brush = Brush.verticalGradient(listOf(Color(0xFFE05B4A), Color(0xFF7FA8B8))))
    drawPath(tubo, color = CremaMapa, style = Stroke(width = a * 0.006f))
    drawCircle(brush = Brush.radialGradient(listOf(Color(0xFFE05B4A), MarronNoche)), radius = a * 0.05f, center = Offset(a * 0.5f, h * 0.83f))
    val tierra = Path().apply {
        moveTo(a * 0.4f, h * 0.88f)
        quadraticTo(a * 0.5f, h * 0.84f, a * 0.6f, h * 0.88f)
    }
    drawPath(tierra, color = MarronNoche, style = Stroke(width = a * 0.02f, cap = StrokeCap.Round))
}

private fun DrawScope.dibujarMacetasConBrotes() {
    val a = size.width
    val h = size.height
    listOf(Triple(0.32f, 0.06f, 0.6f), Triple(0.5f, 0.1f, 0.5f), Triple(0.68f, 0.03f, 0.68f)).forEach { (cx, altoBrote, py) ->
        val maceta = Path().apply {
            moveTo(a * cx - a * 0.06f, h * 0.7f)
            lineTo(a * cx - a * 0.045f, h * 0.85f)
            lineTo(a * cx + a * 0.045f, h * 0.85f)
            lineTo(a * cx + a * 0.06f, h * 0.7f)
            close()
        }
        drawPath(maceta, brush = Brush.linearGradient(listOf(KakiOscuro, MarronNoche.copy(alpha = 0.7f))))
        val brote = Path().apply {
            moveTo(a * cx, h * 0.7f)
            quadraticTo(a * cx - a * 0.03f, h * (0.7f - altoBrote - 0.05f), a * cx, h * py * 0.55f)
        }
        drawPath(brote, color = Color(0xFF6FA05A), style = Stroke(width = a * 0.01f, cap = StrokeCap.Round))
    }
}

private fun DrawScope.dibujarCanonConEco() {
    val a = size.width
    val h = size.height
    val paredIzq = Path().apply {
        moveTo(a * 0.15f, h * 0.55f)
        cubicTo(a * 0.08f, h * 0.68f, a * 0.12f, h * 0.85f, a * 0.28f, h * 0.95f)
    }
    val paredDer = Path().apply {
        moveTo(a * 0.85f, h * 0.55f)
        cubicTo(a * 0.92f, h * 0.68f, a * 0.88f, h * 0.85f, a * 0.72f, h * 0.95f)
    }
    drawPath(paredIzq, brush = Brush.linearGradient(listOf(KakiOscuro, MarronNoche)), style = Stroke(width = a * 0.05f))
    drawPath(paredDer, brush = Brush.linearGradient(listOf(KakiOscuro, MarronNoche)), style = Stroke(width = a * 0.05f))
    listOf(0.08f, 0.14f, 0.2f).forEach { radioExtra ->
        drawCircle(color = CremaMapa.copy(alpha = 0.25f), radius = a * (0.1f + radioExtra), center = Offset(a * 0.5f, h * 0.75f), style = Stroke(width = a * 0.004f))
    }
}

private fun DrawScope.dibujarLupaSobreHoja() {
    val a = size.width
    val h = size.height
    val hoja = Path().apply {
        moveTo(a * 0.3f, h * 0.85f)
        cubicTo(a * 0.3f, h * 0.7f, a * 0.7f, h * 0.7f, a * 0.7f, h * 0.85f)
        cubicTo(a * 0.5f, h * 0.9f, a * 0.5f, h * 0.9f, a * 0.3f, h * 0.85f)
        close()
    }
    drawPath(hoja, brush = Brush.radialGradient(listOf(Color(0xFF6FA05A), Color(0xFF3E6B32))))
    val centro = Offset(a * 0.5f, h * 0.68f)
    drawCircle(color = Color(0xFF7FA8B8).copy(alpha = 0.2f), radius = a * 0.13f, center = centro)
    drawCircle(color = MarronNoche, radius = a * 0.13f, center = centro, style = Stroke(width = a * 0.012f))
    drawLine(KakiOscuro, Offset(centro.x + a * 0.09f, centro.y + a * 0.09f), Offset(a * 0.68f, h * 0.9f), strokeWidth = a * 0.018f, cap = StrokeCap.Round)
    drawOval(color = MarronNoche, topLeft = Offset(a * 0.47f, h * 0.8f), size = Size(a * 0.06f, a * 0.03f))
}

private fun DrawScope.dibujarMapaConRutas() {
    val a = size.width
    val h = size.height
    val mapa = Path().apply {
        moveTo(a * 0.25f, h * 0.6f)
        lineTo(a * 0.75f, h * 0.6f)
        lineTo(a * 0.78f, h * 0.9f)
        lineTo(a * 0.22f, h * 0.9f)
        close()
    }
    drawPath(mapa, brush = Brush.linearGradient(listOf(CremaMapa, KakiClaro.copy(alpha = 0.6f))))
    drawPath(mapa, color = MarronNoche, style = Stroke(width = a * 0.006f))
    val rutaA = Path().apply {
        moveTo(a * 0.32f, h * 0.85f)
        quadraticTo(a * 0.5f, h * 0.65f, a * 0.7f, h * 0.85f)
    }
    val rutaB = Path().apply {
        moveTo(a * 0.32f, h * 0.85f)
        quadraticTo(a * 0.5f, h * 0.78f, a * 0.7f, h * 0.85f)
    }
    val puntitos = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(a * 0.015f, a * 0.012f))
    drawPath(rutaA, color = AzulPizarra, style = Stroke(width = a * 0.007f, pathEffect = puntitos))
    drawPath(rutaB, color = NaranjaFogata, style = Stroke(width = a * 0.007f, pathEffect = puntitos))
    drawCircle(color = MarronNoche, radius = a * 0.012f, center = Offset(a * 0.32f, h * 0.85f))
    drawCircle(color = MarronNoche, radius = a * 0.012f, center = Offset(a * 0.7f, h * 0.85f))
}

private fun DrawScope.dibujarInstrumentosAgrupados() {
    val a = size.width
    val h = size.height
    val posiciones = listOf(0.28f, 0.4f, 0.52f, 0.64f, 0.76f, 0.35f, 0.65f)
    posiciones.forEachIndexed { i, cx ->
        val py = if (i < 5) 0.75f else 0.65f
        drawCircle(
            brush = Brush.radialGradient(listOf(CremaMapa.copy(alpha = 0.5f), KakiOscuro.copy(alpha = 0.3f))),
            radius = a * 0.05f,
            center = Offset(a * cx, h * py),
        )
        drawCircle(color = MarronNoche.copy(alpha = 0.6f), radius = a * 0.05f, center = Offset(a * cx, h * py), style = Stroke(width = a * 0.003f))
    }
}
