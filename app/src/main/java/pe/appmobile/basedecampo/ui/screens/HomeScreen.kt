package pe.appmobile.basedecampo.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import pe.appmobile.basedecampo.R
import pe.appmobile.basedecampo.ui.art.IconoExpedicion
import pe.appmobile.basedecampo.ui.theme.MarronNoche
import pe.appmobile.basedecampo.ui.theme.NaranjaFogata
import pe.appmobile.basedecampo.ui.viewmodel.ExpedicionConEstado
import pe.appmobile.basedecampo.ui.viewmodel.HomeUiState

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onExpedicionClick: (String) -> Unit,
    onCuadernoClick: () -> Unit,
    onPerfilClick: () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        FondoMesaDeCampamento(modifier = Modifier.fillMaxSize())

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(
                text = stringResource(R.string.home_titulo),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Spacer(Modifier.height(24.dp))

            if (!uiState.cargando) {
                FlowRow(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    uiState.expediciones.forEach { expedicionConEstado ->
                        PosteDeExpedicion(
                            expedicionConEstado = expedicionConEstado,
                            onClick = { onExpedicionClick(expedicionConEstado.expedicion.id) },
                        )
                    }
                }
            } else {
                Spacer(Modifier.weight(1f))
            }

            val perfilCdTexto = stringResource(R.string.home_cd_perfil)
            val cuadernoCdTexto = stringResource(R.string.home_cd_cuaderno)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                IconButton(
                    onClick = onPerfilClick,
                    modifier = Modifier.size(56.dp)
                        .semantics { contentDescription = perfilCdTexto },
                ) { Icon(Icons.Filled.Person, contentDescription = null) }
                IconButton(
                    onClick = onCuadernoClick,
                    modifier = Modifier.size(56.dp)
                        .semantics { contentDescription = cuadernoCdTexto },
                ) { Icon(Icons.Filled.Menu, contentDescription = null) }
            }
        }
    }
}

/**
 * Fondo propio del Home, 3 capas -- no está en arte/66-...-CANVAS.md porque esa especificación
 * cubre los fondos POR EXPEDICIÓN (sección 2 de ese documento) y las 11 insignias/iconos
 * (secciones 3 y 4), no el fondo del propio mapa. Mismo tratamiento de "luz de fogata" que las
 * 8 escenas, para que el Home se sienta parte del mismo mundo (vara de calidad, sección 4.0).
 */
@Composable
private fun FondoMesaDeCampamento(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        // Capa 1: cielo nocturno con degradado
        drawRect(brush = Brush.verticalGradient(listOf(MarronNoche, MarronNoche.copy(alpha = 0.85f))))
        // Capa 2: silueta de montañas lejanas
        val montañas = Path().apply {
            moveTo(0f, size.height * 0.55f)
            cubicTo(size.width * 0.2f, size.height * 0.35f, size.width * 0.35f, size.height * 0.5f, size.width * 0.5f, size.height * 0.4f)
            cubicTo(size.width * 0.7f, size.height * 0.3f, size.width * 0.85f, size.height * 0.5f, size.width, size.height * 0.42f)
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }
        drawPath(montañas, color = Color(0xFF1A1613))
        // Capa 3: luz de fogata en una esquina, unifica el ambiente con las 8 escenas
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(NaranjaFogata.copy(alpha = 0.35f), Color.Transparent),
                center = Offset(size.width * 0.15f, size.height * 0.85f),
                radius = size.width * 0.4f,
            ),
            radius = size.width * 0.4f,
            center = Offset(size.width * 0.15f, size.height * 0.85f),
        )
    }
}

@Composable
private fun PosteDeExpedicion(
    expedicionConEstado: ExpedicionConEstado,
    onClick: () -> Unit,
) {
    val descripcionEstado = if (expedicionConEstado.sellado) {
        stringResource(R.string.home_estado_sellado)
    } else {
        stringResource(R.string.home_estado_sin_plan)
    }
    Box(
        modifier = Modifier
            .size(120.dp)
            .clickable(onClick = onClick)
            .semantics {
                contentDescription = "${expedicionConEstado.expedicion.nombre}, $descripcionEstado"
            },
        contentAlignment = Alignment.Center,
    ) {
        IconoExpedicion(
            expedicionId = expedicionConEstado.expedicion.id,
            sellado = expedicionConEstado.sellado,
            modifier = Modifier.fillMaxSize(),
        )
    }
}
