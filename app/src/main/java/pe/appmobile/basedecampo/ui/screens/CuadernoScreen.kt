package pe.appmobile.basedecampo.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import pe.appmobile.basedecampo.R
import pe.appmobile.basedecampo.ui.art.IconoExpedicion
import pe.appmobile.basedecampo.ui.art.Insignia
import pe.appmobile.basedecampo.ui.viewmodel.CuadernoUiState

@Composable
fun CuadernoScreen(uiState: CuadernoUiState, onVolver: () -> Unit) {
    val volverCd = stringResource(R.string.cuaderno_cd_volver)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        IconButton(
            onClick = onVolver,
            modifier = Modifier.size(56.dp).semantics { contentDescription = volverCd },
        ) { Icon(Icons.Filled.ArrowBack, contentDescription = null) }

        Text(stringResource(R.string.cuaderno_titulo), style = MaterialTheme.typography.headlineLarge)

        if (!uiState.cargando) {
            Text(
                stringResource(R.string.cuaderno_seccion_expediciones),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = 16.dp),
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(top = 8.dp),
            ) {
                uiState.expediciones.forEach { expedicionConEstado ->
                    IconoExpedicion(
                        expedicionId = expedicionConEstado.expedicion.id,
                        sellado = expedicionConEstado.sellado,
                        modifier = Modifier.size(72.dp)
                            .semantics { contentDescription = expedicionConEstado.expedicion.nombre },
                    )
                }
            }

            Text(
                stringResource(R.string.cuaderno_seccion_insignias),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = 24.dp),
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(top = 8.dp),
            ) {
                uiState.insignias.forEach { insignia ->
                    val obtenida = insignia.fechaObtenida != null
                    val descripcion = if (obtenida) {
                        stringResource(R.string.cuaderno_insignia_obtenida, insignia.nombre)
                    } else {
                        stringResource(R.string.cuaderno_insignia_pendiente, insignia.nombre)
                    }
                    Insignia(
                        insigniaId = insignia.id,
                        obtenida = obtenida,
                        modifier = Modifier.size(64.dp).semantics { contentDescription = descripcion },
                    )
                }
            }
        }
    }
}
