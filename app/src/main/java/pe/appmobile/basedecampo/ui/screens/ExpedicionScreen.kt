package pe.appmobile.basedecampo.ui.screens

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
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
            modifier = Modifier.size(140.dp),
            onPosicionConocida = { posicionSlot = it },
        ) {
            instrumentoElegidoId?.let { id ->
                IlustracionInstrumento(instrumentoId = id, modifier = Modifier.fillMaxSize())
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            instrumentos.filter { it.id != instrumentoElegidoId }.forEach { instrumento ->
                FichaArrastrable(
                    zonaDestino = posicionSlot,
                    onSoltadaEnZona = { onElegir(instrumento.id) },
                    modifier = Modifier.size(100.dp)
                        .semantics { contentDescription = "Arrastra ${instrumento.nombre} a la mesa" },
                ) {
                    IlustracionInstrumento(instrumentoId = instrumento.id, modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
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
    val anchoFicha = 96.dp
    val density = LocalDensity.current
    val anchoFichaPx = with(density) { anchoFicha.toPx() }

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        ordenActual.forEachIndexed { indice, pasoId ->
            val paso = pasos.first { it.id == pasoId }
            var offsetX by remember(pasoId) { mutableStateOf(0f) }
            Box(
                modifier = Modifier
                    .widthIn(min = 96.dp)
                    .height(120.dp)
                    .offset { IntOffset(offsetX.toInt(), 0) }
                    .semantics { contentDescription = "Paso ${indice + 1}: ${paso.descripcion}" }
                    .pointerInput(pasoId, ordenActual.size) {
                        detectDragGestures(
                            onDrag = { change, dragAmount ->
                                change.consume()
                                offsetX += dragAmount.x
                                val salto = (offsetX / anchoFichaPx).toInt()
                                if (salto != 0) {
                                    onMoverPaso(pasoId, indice + salto)
                                    offsetX -= salto * anchoFichaPx
                                }
                            },
                            onDragEnd = { offsetX = 0f },
                            onDragCancel = { offsetX = 0f },
                        )
                    },
            ) {
                Text("${indice + 1}. ${paso.descripcion}", modifier = Modifier.padding(8.dp))
            }
        }
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
