package pe.appmobile.basedecampo.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pe.appmobile.basedecampo.R
import pe.appmobile.basedecampo.data.entity.InstrumentoEntity
import pe.appmobile.basedecampo.domain.model.PasoProcedimiento
import pe.appmobile.basedecampo.ui.art.FondoExpedicion
import pe.appmobile.basedecampo.ui.art.IlustracionInstrumento
import pe.appmobile.basedecampo.ui.art.IlustracionTuco
import pe.appmobile.basedecampo.ui.art.PoseTuco
import pe.appmobile.basedecampo.ui.components.FichaArrastrable
import pe.appmobile.basedecampo.ui.components.ZonaSoltar
import pe.appmobile.basedecampo.ui.theme.CremaMapa
import pe.appmobile.basedecampo.ui.theme.MarronNoche
import pe.appmobile.basedecampo.ui.theme.NaranjaFogata
import pe.appmobile.basedecampo.ui.viewmodel.ExpedicionUiState

@Composable
fun ExpedicionScreen(
    uiState: ExpedicionUiState,
    onElegirInstrumento: (String) -> Unit,
    onMoverPaso: (String, Int) -> Unit,
    onCambiarRepeticiones: (Int) -> Unit,
    onSellarPlan: () -> Unit,
) {
    val expedicion = uiState.expedicion ?: return
    Box(modifier = Modifier.fillMaxSize()) {
    FondoExpedicion(expedicionId = expedicion.id, modifier = Modifier.fillMaxSize())
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        Text(
            expedicion.nombre,
            style = MaterialTheme.typography.headlineLarge,
            color = CremaMapa,
        )
        Text(
            expedicion.pregunta,
            style = MaterialTheme.typography.bodyLarge,
            color = CremaMapa,
        )
        Spacer(Modifier.height(16.dp))

        Text(stringResource(R.string.expedicion_instrumentos_titulo), style = MaterialTheme.typography.titleLarge, color = CremaMapa)
        SelectorInstrumento(
            instrumentos = uiState.instrumentos,
            instrumentoElegidoId = uiState.instrumentoElegidoId,
            onElegir = onElegirInstrumento,
        )
        Spacer(Modifier.height(16.dp))

        Text(stringResource(R.string.expedicion_secuencia_titulo), style = MaterialTheme.typography.titleLarge, color = CremaMapa)
        SecuenciaDePasos(
            pasos = expedicion.pasos,
            ordenActual = uiState.ordenPasos,
            onMoverPaso = onMoverPaso,
        )
        Spacer(Modifier.height(16.dp))

        Text(stringResource(R.string.expedicion_repeticiones_titulo), style = MaterialTheme.typography.titleLarge, color = CremaMapa)
        ContadorRepeticiones(
            valor = uiState.repeticionesElegidas,
            onCambiar = onCambiarRepeticiones,
        )
        Spacer(Modifier.height(24.dp))

        Button(onClick = onSellarPlan, modifier = Modifier.fillMaxWidth().height(56.dp)) {
            Text(stringResource(R.string.expedicion_sellar_plan))
        }

        uiState.resultado?.let { resultado ->
            Spacer(Modifier.height(16.dp))
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    IlustracionTuco(
                        pose = if (resultado.esValido) PoseTuco.CELEBRANDO else PoseTuco.CONFUNDIDO,
                        modifier = Modifier.size(120.dp),
                    )
                    Text(
                        text = if (resultado.esValido) {
                            stringResource(R.string.expedicion_resultado_valido)
                        } else {
                            stringResource(R.string.expedicion_resultado_invalido, resultado.mensajeError.orEmpty())
                        },
                        color = if (resultado.esValido) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
    }
}

@Composable
private fun SelectorInstrumento(
    instrumentos: List<InstrumentoEntity>,
    instrumentoElegidoId: String?,
    onElegir: (String) -> Unit,
) {
    var posicionSlot by remember { mutableStateOf(Rect.Zero) }

    Column {
        ZonaSoltar(
            modifier = Modifier
                .size(140.dp)
                .background(CremaMapa.copy(alpha = 0.12f), RoundedCornerShape(16.dp))
                .border(2.dp, CremaMapa.copy(alpha = 0.6f), RoundedCornerShape(16.dp)),
            onPosicionConocida = { posicionSlot = it },
        ) {
            if (instrumentoElegidoId == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        stringResource(R.string.expedicion_mesa_vacia),
                        style = MaterialTheme.typography.bodyMedium,
                        color = CremaMapa.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                    )
                }
            } else {
                IlustracionInstrumento(instrumentoId = instrumentoElegidoId, modifier = Modifier.fillMaxSize())
            }
        }
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            instrumentos.filter { it.id != instrumentoElegidoId }.forEachIndexed { indice, instrumento ->
                val pistaActiva = indice == 0 && instrumentoElegidoId == null
                FichaArrastrable(
                    zonaDestino = posicionSlot,
                    onSoltadaEnZona = { onElegir(instrumento.id) },
                    modifier = Modifier.size(100.dp)
                        .offset(y = if (pistaActiva) pistaNudgeDp() else 0.dp)
                        .semantics { contentDescription = "Arrastra ${instrumento.nombre} a la mesa" },
                ) {
                    IlustracionInstrumento(instrumentoId = instrumento.id, modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}

/**
 * Pequeño rebote animado (±6dp, vaivén continuo) para invitar a arrastrar sin usar texto -- ver
 * hallazgo del 28/08/2026: la mecánica solo se explicaba con encabezados de texto, sin ninguna
 * señal visual. Quien llama decide el eje aplicando el resultado a `offset(x=...)` o
 * `offset(y=...)`.
 */
@Composable
private fun pistaNudgeDp(): Dp {
    val transicion = rememberInfiniteTransition(label = "pistaArrastre")
    val valor by transicion.animateFloat(
        initialValue = -6f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(650, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "pistaArrastreValor",
    )
    return valor.dp
}

/**
 * Reordena arrastrando: la ficha que se mueve calcula, con el desplazamiento horizontal
 * acumulado dividido entre el ancho de una ficha, cuántas posiciones debe saltar -- técnica de
 * índices, no de coordenadas absolutas, así que no depende de calcular ventanas globales.
 */
@Composable
private fun SecuenciaDePasos(
    pasos: List<PasoProcedimiento>,
    ordenActual: List<String>,
    onMoverPaso: (String, Int) -> Unit,
) {
    val anchoFicha = 140.dp
    val density = LocalDensity.current
    val anchoFichaPx = with(density) { anchoFicha.toPx() }
    val haptics = LocalHapticFeedback.current
    var yaArrastroAlgunPaso by remember { mutableStateOf(false) }

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        ordenActual.forEachIndexed { indice, pasoId ->
            val paso = pasos.first { it.id == pasoId }
            var offsetX by remember(pasoId) { mutableStateOf(0f) }
            val pistaActiva = indice == 0 && !yaArrastroAlgunPaso
            Box(
                modifier = Modifier
                    .width(anchoFicha)
                    .height(140.dp)
                    .offset(x = if (pistaActiva) pistaNudgeDp() else 0.dp)
                    .offset { IntOffset(offsetX.toInt(), 0) }
                    .semantics { contentDescription = "Paso ${indice + 1}: ${paso.descripcion}" }
                    .pointerInput(pasoId, ordenActual.size) {
                        detectHorizontalDragGestures(
                            onHorizontalDrag = { change, dragAmount ->
                                change.consume()
                                yaArrastroAlgunPaso = true
                                offsetX += dragAmount
                                val salto = (offsetX / anchoFichaPx).toInt()
                                if (salto != 0) {
                                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                    onMoverPaso(pasoId, indice + salto)
                                    offsetX -= salto * anchoFichaPx
                                }
                            },
                            onDragEnd = { offsetX = 0f },
                            onDragCancel = { offsetX = 0f },
                        )
                    },
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    MarcadorPaso(numero = indice + 1)
                    Spacer(Modifier.height(4.dp))
                    Text(paso.descripcion, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

/** Marcador de paso al estilo "hito de sendero" -- número sobre un círculo de Acento, para que
 * cada ficha de paso no se vea como texto plano y combine con las fichas ilustradas de arriba. */
@Composable
private fun MarcadorPaso(numero: Int, modifier: Modifier = Modifier) {
    Box(modifier = modifier.size(28.dp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(color = NaranjaFogata)
        }
        Text("$numero", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = MarronNoche)
    }
}

@Composable
private fun ContadorRepeticiones(valor: Int, onCambiar: (Int) -> Unit) {
    val quitarCd = stringResource(R.string.expedicion_cd_quitar_repeticion)
    val agregarCd = stringResource(R.string.expedicion_cd_agregar_repeticion)
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        IconButton(
            onClick = { onCambiar((valor - 1).coerceAtLeast(0)) },
            modifier = Modifier.size(120.dp).semantics { contentDescription = quitarCd },
        ) { Text("−", fontSize = 40.sp) }
        Text("$valor", fontSize = 32.sp, textAlign = TextAlign.Center, modifier = Modifier.widthIn(min = 64.dp))
        IconButton(
            onClick = { onCambiar(valor + 1) },
            modifier = Modifier.size(120.dp).semantics { contentDescription = agregarCd },
        ) { Text("+", fontSize = 40.sp) }
    }
}
