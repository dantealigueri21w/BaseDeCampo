package pe.appmobile.basedecampo.domain.engine

import pe.appmobile.basedecampo.domain.model.PasoProcedimiento
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MotorSecuenciaTest {

    @Test
    fun `secuencia que respeta todas las precedencias es valida`() {
        val pasos = listOf(
            PasoProcedimiento(id = "preparar", descripcion = "Preparar el instrumento", debeIrAntesDe = setOf("medir")),
            PasoProcedimiento(id = "medir", descripcion = "Tomar la medición", debeIrAntesDe = setOf("anotar")),
            PasoProcedimiento(id = "anotar", descripcion = "Anotar el resultado")
        )
        val orden = listOf("preparar", "medir", "anotar")
        assertTrue(MotorSecuencia.esSecuenciaValida(pasos, orden))
    }

    @Test
    fun `secuencia que viola una precedencia es invalida`() {
        val pasos = listOf(
            PasoProcedimiento(id = "preparar", descripcion = "Preparar", debeIrAntesDe = setOf("medir")),
            PasoProcedimiento(id = "medir", descripcion = "Medir")
        )
        val orden = listOf("medir", "preparar") // invertido: viola la precedencia
        assertFalse(MotorSecuencia.esSecuenciaValida(pasos, orden))
    }

    @Test
    fun `dos ordenes distintos son validos si ninguno viola una precedencia real`() {
        // "El Peso de la Piedra": pesar piedra A y piedra B no tiene un orden obligatorio
        // entre si, solo deben pesarse ambas antes de comparar.
        val pasos = listOf(
            PasoProcedimiento(id = "pesar_a", descripcion = "Pesar piedra A", debeIrAntesDe = setOf("comparar")),
            PasoProcedimiento(id = "pesar_b", descripcion = "Pesar piedra B", debeIrAntesDe = setOf("comparar")),
            PasoProcedimiento(id = "comparar", descripcion = "Comparar los pesos")
        )
        val ordenUno = listOf("pesar_a", "pesar_b", "comparar")
        val ordenDos = listOf("pesar_b", "pesar_a", "comparar")
        assertTrue(MotorSecuencia.esSecuenciaValida(pasos, ordenUno))
        assertTrue(MotorSecuencia.esSecuenciaValida(pasos, ordenDos))
    }

    @Test
    fun `secuencia con un paso faltante es invalida`() {
        val pasos = listOf(
            PasoProcedimiento(id = "a", descripcion = "Paso A"),
            PasoProcedimiento(id = "b", descripcion = "Paso B")
        )
        assertFalse(MotorSecuencia.esSecuenciaValida(pasos, listOf("a")))
    }

    @Test
    fun `secuencia con un paso repetido es invalida`() {
        val pasos = listOf(
            PasoProcedimiento(id = "a", descripcion = "Paso A"),
            PasoProcedimiento(id = "b", descripcion = "Paso B")
        )
        assertFalse(MotorSecuencia.esSecuenciaValida(pasos, listOf("a", "a")))
    }

    @Test
    fun `secuencia con un paso desconocido que no pertenece a la expedicion es invalida`() {
        val pasos = listOf(
            PasoProcedimiento(id = "a", descripcion = "Paso A"),
            PasoProcedimiento(id = "b", descripcion = "Paso B")
        )
        assertFalse(MotorSecuencia.esSecuenciaValida(pasos, listOf("a", "x")))
    }
}
