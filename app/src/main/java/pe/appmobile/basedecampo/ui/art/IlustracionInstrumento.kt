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
private val MarronNoche = Color(0xFF2A2521)
private val NaranjaFogata = Color(0xFFD97B3F)
private val CremaMapa = Color(0xFFF5F1E8)
private val AguaVaso = Color(0xFF7FA8B8)

/**
 * Ficha de instrumento para la mesa de la expedición -- ilustración propia con degradado,
 * curvas y sombra de contacto (vara de calidad de la especificación de arte, sección 0),
 * distinta del pictograma más plano de IconoExpedicion.
 */
@Composable
fun IlustracionInstrumento(instrumentoId: String, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        dibujarSombraDeContacto()
        when (instrumentoId) {
            "vaso_graduado" -> dibujarVasoGraduado()
            "balanza" -> dibujarBalanza()
            "termometro" -> dibujarTermometro()
            "regla" -> dibujarRegla()
            "cronometro" -> dibujarCronometro()
            "lupa" -> dibujarLupa()
            "cinta_metrica" -> dibujarCintaMetrica()
        }
    }
}

private fun DrawScope.dibujarSombraDeContacto() {
    drawOval(
        color = MarronNoche.copy(alpha = 0.22f),
        topLeft = Offset(size.width * 0.2f, size.height * 0.88f),
        size = Size(size.width * 0.6f, size.height * 0.1f),
    )
}

private fun DrawScope.dibujarVasoGraduado() {
    val a = size.width
    val h = size.height
    val cuerpo = Path().apply {
        moveTo(a * 0.35f, h * 0.15f)
        lineTo(a * 0.3f, h * 0.75f)
        cubicTo(a * 0.3f, h * 0.85f, a * 0.7f, h * 0.85f, a * 0.7f, h * 0.75f)
        lineTo(a * 0.65f, h * 0.15f)
        close()
    }
    drawPath(cuerpo, brush = Brush.linearGradient(listOf(AguaVaso.copy(alpha = 0.35f), AguaVaso.copy(alpha = 0.65f))))
    val contorno = Path().apply {
        moveTo(a * 0.35f, h * 0.15f)
        lineTo(a * 0.3f, h * 0.75f)
        cubicTo(a * 0.3f, h * 0.85f, a * 0.7f, h * 0.85f, a * 0.7f, h * 0.75f)
        lineTo(a * 0.65f, h * 0.15f)
    }
    drawPath(contorno, color = AzulPizarra, style = Stroke(width = a * 0.02f))
    listOf(0.35f, 0.5f, 0.65f).forEach { py ->
        drawLine(AzulPizarra, Offset(a * 0.32f, h * py), Offset(a * 0.42f, h * py), strokeWidth = a * 0.015f)
    }
}

private fun DrawScope.dibujarBalanza() {
    val a = size.width
    val h = size.height
    drawLine(KakiOscuro, Offset(a * 0.5f, h * 0.15f), Offset(a * 0.5f, h * 0.55f), strokeWidth = a * 0.02f, cap = StrokeCap.Round)
    drawLine(KakiOscuro, Offset(a * 0.25f, h * 0.25f), Offset(a * 0.75f, h * 0.25f), strokeWidth = a * 0.02f, cap = StrokeCap.Round)
    listOf(0.25f, 0.75f).forEachIndexed { i, cx ->
        val platillo = Path().apply {
            moveTo(a * cx - a * 0.12f, h * 0.25f)
            quadraticTo(a * cx, h * 0.42f, a * cx + a * 0.12f, h * 0.25f)
        }
        drawPath(platillo, brush = Brush.linearGradient(listOf(KakiOscuro, MarronNoche.copy(alpha = 0.8f))), style = Stroke(width = a * 0.015f))
        val radioPiedra = if (i == 0) a * 0.07f else a * 0.09f
        drawCircle(
            brush = Brush.radialGradient(listOf(Color(0xFFBDBDBD), MarronNoche.copy(alpha = 0.7f))),
            radius = radioPiedra,
            center = Offset(a * cx, h * 0.5f),
        )
    }
}

private fun DrawScope.dibujarTermometro() {
    val a = size.width
    val h = size.height
    val tubo = Path().apply {
        moveTo(a * 0.45f, h * 0.12f)
        cubicTo(a * 0.4f, h * 0.15f, a * 0.4f, h * 0.55f, a * 0.45f, h * 0.6f)
        lineTo(a * 0.55f, h * 0.6f)
        cubicTo(a * 0.6f, h * 0.55f, a * 0.6f, h * 0.15f, a * 0.55f, h * 0.12f)
        close()
    }
    drawPath(tubo, brush = Brush.verticalGradient(listOf(CremaMapa, KakiOscuro.copy(alpha = 0.3f))))
    drawPath(tubo, color = AzulPizarra, style = Stroke(width = a * 0.015f))
    drawCircle(
        brush = Brush.radialGradient(listOf(Color(0xFFE05B4A), MarronNoche)),
        radius = a * 0.13f,
        center = Offset(a * 0.5f, h * 0.72f),
    )
    drawLine(Color(0xFFE05B4A), Offset(a * 0.5f, h * 0.28f), Offset(a * 0.5f, h * 0.62f), strokeWidth = a * 0.03f)
}

private fun DrawScope.dibujarRegla() {
    val a = size.width
    val h = size.height
    val cuerpo = Path().apply {
        moveTo(a * 0.15f, h * 0.4f)
        lineTo(a * 0.85f, h * 0.4f)
        lineTo(a * 0.85f, h * 0.6f)
        lineTo(a * 0.15f, h * 0.6f)
        close()
    }
    drawPath(cuerpo, brush = Brush.linearGradient(listOf(Color(0xFFE8D9B5), KakiOscuro.copy(alpha = 0.6f))))
    drawPath(cuerpo, color = MarronNoche, style = Stroke(width = a * 0.012f))
    for (i in 1..8) {
        val px = a * 0.15f + (a * 0.7f) * (i / 8f)
        val altoMarca = if (i % 2 == 0) h * 0.1f else h * 0.06f
        drawLine(MarronNoche, Offset(px, h * 0.4f), Offset(px, h * 0.4f + altoMarca), strokeWidth = a * 0.008f)
    }
}

private fun DrawScope.dibujarCronometro() {
    val a = size.width
    val h = size.height
    val centro = Offset(a * 0.5f, h * 0.55f)
    val radio = a * 0.3f
    drawCircle(brush = Brush.radialGradient(listOf(CremaMapa, KakiOscuro.copy(alpha = 0.5f)), center = centro, radius = radio), radius = radio, center = centro)
    drawCircle(color = AzulPizarra, radius = radio, center = centro, style = Stroke(width = a * 0.02f))
    drawLine(NaranjaFogata, centro, Offset(centro.x, centro.y - radio * 0.7f), strokeWidth = a * 0.02f, cap = StrokeCap.Round)
    drawLine(NaranjaFogata, centro, Offset(centro.x + radio * 0.4f, centro.y), strokeWidth = a * 0.015f, cap = StrokeCap.Round)
    val corona = Path().apply {
        moveTo(a * 0.44f, h * 0.22f)
        lineTo(a * 0.56f, h * 0.22f)
        lineTo(a * 0.53f, h * 0.28f)
        lineTo(a * 0.47f, h * 0.28f)
        close()
    }
    drawPath(corona, color = MarronNoche)
}

private fun DrawScope.dibujarLupa() {
    val a = size.width
    val h = size.height
    val centro = Offset(a * 0.42f, h * 0.4f)
    val radio = a * 0.24f
    drawCircle(brush = Brush.radialGradient(listOf(AguaVaso.copy(alpha = 0.25f), Color.Transparent), center = centro, radius = radio), radius = radio, center = centro)
    drawCircle(color = MarronNoche, radius = radio, center = centro, style = Stroke(width = a * 0.025f))
    val mango = Path().apply {
        moveTo(centro.x + radio * 0.7f, centro.y + radio * 0.7f)
        cubicTo(a * 0.68f, h * 0.68f, a * 0.75f, h * 0.78f, a * 0.82f, h * 0.85f)
    }
    drawPath(mango, color = KakiOscuro, style = Stroke(width = a * 0.035f, cap = StrokeCap.Round))
}

private fun DrawScope.dibujarCintaMetrica() {
    val a = size.width
    val h = size.height
    val centro = Offset(a * 0.5f, h * 0.5f)
    val radio = a * 0.3f
    drawCircle(brush = Brush.radialGradient(listOf(NaranjaFogata.copy(alpha = 0.85f), KakiOscuro), center = centro, radius = radio), radius = radio, center = centro)
    drawCircle(color = MarronNoche, radius = radio, center = centro, style = Stroke(width = a * 0.015f))
    val cinta = Path().apply {
        moveTo(centro.x + radio * 0.9f, centro.y)
        quadraticTo(a * 0.85f, h * 0.7f, a * 0.7f, h * 0.85f)
    }
    drawPath(cinta, color = CremaMapa, style = Stroke(width = a * 0.06f))
    drawLine(MarronNoche, Offset(a * 0.5f, h * 0.32f), Offset(a * 0.5f, h * 0.68f), strokeWidth = a * 0.01f)
}
