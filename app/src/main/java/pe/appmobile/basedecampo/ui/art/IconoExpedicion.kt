package pe.appmobile.basedecampo.ui.art

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

private val AzulPizarra = Color(0xFF4A6670)
private val NaranjaFogata = Color(0xFFD97B3F)
private val MarronNoche = Color(0xFF2A2521)
private val CremaMapa = Color(0xFFF5F1E8)

/**
 * Pictograma de una de las 8 expediciones -- silueta de un solo trazo grueso, sin degradado
 * (sección 4 de la especificación de arte: a tamaño de icono el detalle se pierde igual).
 * El color del trazo marca el estado: Acento cuando el plan ya está sellado, Primario si no.
 */
@Composable
fun IconoExpedicion(expedicionId: String, sellado: Boolean, modifier: Modifier = Modifier) {
    val colorTrazo = if (sellado) NaranjaFogata else AzulPizarra
    Canvas(modifier = modifier) {
        val radio = size.minDimension / 2
        val centro = Offset(size.width / 2, size.height / 2)

        drawCircle(
            brush = Brush.radialGradient(listOf(CremaMapa, CremaMapa.copy(alpha = 0.9f)), center = centro, radius = radio),
            radius = radio,
            center = centro,
        )
        drawCircle(color = colorTrazo, radius = radio * 0.94f, center = centro, style = Stroke(width = radio * 0.08f))

        when (expedicionId) {
            "sombra_sol" -> dibujarVasos(colorTrazo)
            "peso_piedra" -> dibujarBalanza(colorTrazo)
            "frio_puna" -> dibujarTermometro(colorTrazo)
            "semilla_crece" -> dibujarMaceta(colorTrazo)
            "eco_quebrada" -> dibujarCanon(colorTrazo)
            "lupa_insecto" -> dibujarLupa(colorTrazo)
            "camino_corto" -> dibujarMapa(colorTrazo)
            else -> dibujarEstrellaFinal(colorTrazo)
        }
    }
}

private fun DrawScope.trazo(radio: Float) = Stroke(width = radio * 0.09f, cap = StrokeCap.Round)

private fun DrawScope.dibujarVasos(color: Color) {
    val a = size.width
    val h = size.height
    val r = size.minDimension / 2
    listOf(0.38f, 0.62f).forEach { cx ->
        val vaso = Path().apply {
            moveTo(a * cx - a * 0.09f, h * 0.3f)
            lineTo(a * cx - a * 0.07f, h * 0.68f)
            quadraticTo(a * cx, h * 0.74f, a * cx + a * 0.07f, h * 0.68f)
            lineTo(a * cx + a * 0.09f, h * 0.3f)
        }
        drawPath(vaso, color = color, style = trazo(r))
    }
}

private fun DrawScope.dibujarBalanza(color: Color) {
    val a = size.width
    val h = size.height
    val r = size.minDimension / 2
    val fiel = Path().apply {
        moveTo(a * 0.5f, h * 0.28f)
        lineTo(a * 0.5f, h * 0.7f)
        moveTo(a * 0.3f, h * 0.35f)
        lineTo(a * 0.7f, h * 0.35f)
    }
    drawPath(fiel, color = color, style = trazo(r))
    listOf(0.3f, 0.7f).forEach { cx ->
        val platillo = Path().apply {
            moveTo(a * cx - a * 0.12f, h * 0.35f)
            quadraticTo(a * cx, h * 0.5f, a * cx + a * 0.12f, h * 0.35f)
        }
        drawPath(platillo, color = color, style = trazo(r * 0.7f))
    }
}

private fun DrawScope.dibujarTermometro(color: Color) {
    val a = size.width
    val h = size.height
    val r = size.minDimension / 2
    val tubo = Path().apply {
        moveTo(a * 0.5f, h * 0.22f)
        lineTo(a * 0.5f, h * 0.62f)
    }
    drawPath(tubo, color = color, style = trazo(r))
    drawCircle(color = color, radius = r * 0.22f, center = Offset(a * 0.5f, h * 0.72f))
}

private fun DrawScope.dibujarMaceta(color: Color) {
    val a = size.width
    val h = size.height
    val r = size.minDimension / 2
    val maceta = Path().apply {
        moveTo(a * 0.35f, h * 0.55f)
        lineTo(a * 0.4f, h * 0.75f)
        lineTo(a * 0.6f, h * 0.75f)
        lineTo(a * 0.65f, h * 0.55f)
        close()
    }
    drawPath(maceta, color = color, style = trazo(r * 0.7f))
    val brote = Path().apply {
        moveTo(a * 0.5f, h * 0.55f)
        quadraticTo(a * 0.42f, h * 0.4f, a * 0.5f, h * 0.25f)
        moveTo(a * 0.5f, h * 0.4f)
        quadraticTo(a * 0.58f, h * 0.35f, a * 0.6f, h * 0.28f)
    }
    drawPath(brote, color = color, style = trazo(r * 0.6f))
}

private fun DrawScope.dibujarCanon(color: Color) {
    val a = size.width
    val h = size.height
    val r = size.minDimension / 2
    val paredIzquierda = Path().apply {
        moveTo(a * 0.3f, h * 0.25f)
        cubicTo(a * 0.2f, h * 0.4f, a * 0.25f, h * 0.6f, a * 0.4f, h * 0.75f)
    }
    val paredDerecha = Path().apply {
        moveTo(a * 0.7f, h * 0.25f)
        cubicTo(a * 0.8f, h * 0.4f, a * 0.75f, h * 0.6f, a * 0.6f, h * 0.75f)
    }
    drawPath(paredIzquierda, color = color, style = trazo(r))
    drawPath(paredDerecha, color = color, style = trazo(r))
}

private fun DrawScope.dibujarLupa(color: Color) {
    val a = size.width
    val h = size.height
    val r = size.minDimension / 2
    drawCircle(color = color, radius = r * 0.3f, center = Offset(a * 0.42f, h * 0.42f), style = trazo(r * 0.7f))
    val mango = Path().apply {
        moveTo(a * 0.58f, h * 0.58f)
        lineTo(a * 0.75f, h * 0.75f)
    }
    drawPath(mango, color = color, style = trazo(r))
}

private fun DrawScope.dibujarMapa(color: Color) {
    val a = size.width
    val h = size.height
    val r = size.minDimension / 2
    val ruta = Path().apply {
        moveTo(a * 0.28f, h * 0.65f)
        quadraticTo(a * 0.5f, h * 0.3f, a * 0.72f, h * 0.65f)
    }
    drawPath(
        ruta,
        color = color,
        style = Stroke(width = r * 0.09f, cap = StrokeCap.Round, pathEffect = PathEffect.dashPathEffect(floatArrayOf(r * 0.18f, r * 0.14f))),
    )
    drawCircle(color = color, radius = r * 0.08f, center = Offset(a * 0.28f, h * 0.65f))
    drawCircle(color = color, radius = r * 0.08f, center = Offset(a * 0.72f, h * 0.65f))
}

private fun DrawScope.dibujarEstrellaFinal(color: Color) {
    val a = size.width
    val h = size.height
    val r = size.minDimension / 2
    val estrella = Path().apply {
        val cx = a * 0.5f
        val cy = h * 0.5f
        moveTo(cx, cy - r * 0.4f)
        lineTo(cx + r * 0.12f, cy - r * 0.12f)
        lineTo(cx + r * 0.4f, cy - r * 0.08f)
        lineTo(cx + r * 0.18f, cy + r * 0.1f)
        lineTo(cx + r * 0.25f, cy + r * 0.4f)
        lineTo(cx, cy + r * 0.2f)
        lineTo(cx - r * 0.25f, cy + r * 0.4f)
        lineTo(cx - r * 0.18f, cy + r * 0.1f)
        lineTo(cx - r * 0.4f, cy - r * 0.08f)
        lineTo(cx - r * 0.12f, cy - r * 0.12f)
        close()
    }
    drawPath(estrella, color = color, style = trazo(r * 0.5f))
}
