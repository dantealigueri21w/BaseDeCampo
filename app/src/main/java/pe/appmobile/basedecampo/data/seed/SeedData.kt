package pe.appmobile.basedecampo.data.seed

import pe.appmobile.basedecampo.data.entity.ExpedicionEntity
import pe.appmobile.basedecampo.data.entity.InsigniaEntity
import pe.appmobile.basedecampo.data.entity.InstrumentoEntity
import pe.appmobile.basedecampo.data.entity.PasoProcedimientoEntity

object SeedData {

    val instrumentos = listOf(
        InstrumentoEntity("vaso_graduado", "Vaso graduado", "VASO_GRADUADO", "VOLUMEN"),
        InstrumentoEntity("balanza", "Balanza", "BALANZA", "PESO"),
        InstrumentoEntity("termometro", "Termómetro", "TERMOMETRO", "TEMPERATURA"),
        InstrumentoEntity("regla", "Regla", "REGLA", "LONGITUD"),
        InstrumentoEntity("cronometro", "Cronómetro", "CRONOMETRO", "TIEMPO"),
        InstrumentoEntity("lupa", "Lupa", "LUPA", "DETALLE_VISUAL"),
        InstrumentoEntity("cinta_metrica", "Cinta métrica", "CINTA_METRICA", "DISTANCIA"),
    )

    val expediciones = listOf(
        ExpedicionEntity("sombra_sol", "La Sombra y el Sol", "¿El agua se evapora más rápido con sol o con sombra?", "VOLUMEN", "vaso_graduado", 3, 1),
        ExpedicionEntity("peso_piedra", "El Peso de la Piedra", "¿Qué tan pesada es cada piedra de la quebrada?", "PESO", "balanza", 3, 2),
        ExpedicionEntity("frio_puna", "El Frío de la Puna", "¿Cuánto baja la temperatura de noche a distintas alturas?", "TEMPERATURA", "termometro", 3, 3),
        ExpedicionEntity("semilla_crece", "La Semilla que Crece", "¿Qué tan rápido germina una semilla con distinta cantidad de agua?", "LONGITUD", "regla", 5, 4),
        ExpedicionEntity("eco_quebrada", "El Eco de la Quebrada", "¿El eco tarda más en un espacio angosto o uno abierto?", "TIEMPO", "cronometro", 3, 5),
        ExpedicionEntity("lupa_insecto", "La Lupa del Insecto", "¿Qué detalles reales tiene un insecto que a simple vista no se ven?", "DETALLE_VISUAL", "lupa", 2, 6),
        ExpedicionEntity("camino_corto", "El Camino más Corto", "¿Qué ruta entre dos puntos del campamento es más corta?", "DISTANCIA", "cinta_metrica", 3, 7),
        ExpedicionEntity("expedicion_final", "La Expedición Final", "¿Cómo cambian distancia y tiempo del mismo recorrido bajo distintas condiciones?", "DISTANCIA", "cinta_metrica", 4, 8),
    )

    val pasos = listOf(
        // La Sombra y el Sol (5 pasos)
        PasoProcedimientoEntity(pasoId = "preparar_vasos", expedicionId = "sombra_sol", descripcion = "Llenar dos vasos graduados con la misma cantidad de agua", orden = 1, debeIrAntesDeCsv = "colocar_sol,colocar_sombra"),
        PasoProcedimientoEntity(pasoId = "colocar_sol", expedicionId = "sombra_sol", descripcion = "Dejar un vaso bajo el sol directo", orden = 2, debeIrAntesDeCsv = "esperar_evaporacion"),
        PasoProcedimientoEntity(pasoId = "colocar_sombra", expedicionId = "sombra_sol", descripcion = "Dejar el otro vaso a la sombra", orden = 3, debeIrAntesDeCsv = "esperar_evaporacion"),
        PasoProcedimientoEntity(pasoId = "esperar_evaporacion", expedicionId = "sombra_sol", descripcion = "Esperar el mismo tiempo en los dos lugares", orden = 4, debeIrAntesDeCsv = "medir_evaporacion"),
        PasoProcedimientoEntity(pasoId = "medir_evaporacion", expedicionId = "sombra_sol", descripcion = "Medir cuánta agua quedó en cada vaso", orden = 5, debeIrAntesDeCsv = ""),

        // El Peso de la Piedra (5 pasos)
        PasoProcedimientoEntity(pasoId = "ajustar_balanza", expedicionId = "peso_piedra", descripcion = "Poner la balanza en cero antes de pesar", orden = 1, debeIrAntesDeCsv = "pesar_a,pesar_b"),
        PasoProcedimientoEntity(pasoId = "pesar_a", expedicionId = "peso_piedra", descripcion = "Pesar la piedra A", orden = 2, debeIrAntesDeCsv = "comparar_pesos"),
        PasoProcedimientoEntity(pasoId = "pesar_b", expedicionId = "peso_piedra", descripcion = "Pesar la piedra B", orden = 3, debeIrAntesDeCsv = "comparar_pesos"),
        PasoProcedimientoEntity(pasoId = "comparar_pesos", expedicionId = "peso_piedra", descripcion = "Comparar los dos pesos", orden = 4, debeIrAntesDeCsv = "anotar_peso"),
        PasoProcedimientoEntity(pasoId = "anotar_peso", expedicionId = "peso_piedra", descripcion = "Anotar cuál piedra pesa más", orden = 5, debeIrAntesDeCsv = ""),

        // El Frío de la Puna (4 pasos)
        PasoProcedimientoEntity(pasoId = "preparar_termometro", expedicionId = "frio_puna", descripcion = "Revisar que el termómetro marque bien antes de salir", orden = 1, debeIrAntesDeCsv = "colocar"),
        PasoProcedimientoEntity(pasoId = "colocar", expedicionId = "frio_puna", descripcion = "Colocar el termómetro en el punto de medición", orden = 2, debeIrAntesDeCsv = "esperar_estabilizar"),
        PasoProcedimientoEntity(pasoId = "esperar_estabilizar", expedicionId = "frio_puna", descripcion = "Esperar a que el termómetro se estabilice", orden = 3, debeIrAntesDeCsv = "leer"),
        PasoProcedimientoEntity(pasoId = "leer", expedicionId = "frio_puna", descripcion = "Leer la temperatura marcada", orden = 4, debeIrAntesDeCsv = ""),

        // La Semilla que Crece (4 pasos)
        PasoProcedimientoEntity(pasoId = "sembrar", expedicionId = "semilla_crece", descripcion = "Sembrar semillas con distinta cantidad de agua", orden = 1, debeIrAntesDeCsv = "esperar_dias"),
        PasoProcedimientoEntity(pasoId = "esperar_dias", expedicionId = "semilla_crece", descripcion = "Esperar varios días a que germinen", orden = 2, debeIrAntesDeCsv = "medir_brote"),
        PasoProcedimientoEntity(pasoId = "medir_brote", expedicionId = "semilla_crece", descripcion = "Medir la altura del brote con la regla", orden = 3, debeIrAntesDeCsv = "anotar_altura"),
        PasoProcedimientoEntity(pasoId = "anotar_altura", expedicionId = "semilla_crece", descripcion = "Anotar la altura medida", orden = 4, debeIrAntesDeCsv = ""),

        // El Eco de la Quebrada (4 pasos)
        PasoProcedimientoEntity(pasoId = "elegir_espacio", expedicionId = "eco_quebrada", descripcion = "Elegir un espacio angosto y uno abierto para comparar", orden = 1, debeIrAntesDeCsv = "gritar"),
        PasoProcedimientoEntity(pasoId = "gritar", expedicionId = "eco_quebrada", descripcion = "Hacer un sonido y empezar a contar", orden = 2, debeIrAntesDeCsv = "medir_eco"),
        PasoProcedimientoEntity(pasoId = "medir_eco", expedicionId = "eco_quebrada", descripcion = "Medir con el cronómetro cuánto tarda en volver el eco", orden = 3, debeIrAntesDeCsv = "anotar_eco"),
        PasoProcedimientoEntity(pasoId = "anotar_eco", expedicionId = "eco_quebrada", descripcion = "Anotar el tiempo del eco en cada espacio", orden = 4, debeIrAntesDeCsv = ""),

        // La Lupa del Insecto (4 pasos)
        PasoProcedimientoEntity(pasoId = "encontrar_insecto", expedicionId = "lupa_insecto", descripcion = "Encontrar un insecto sin tocarlo", orden = 1, debeIrAntesDeCsv = "observar_lupa"),
        PasoProcedimientoEntity(pasoId = "observar_lupa", expedicionId = "lupa_insecto", descripcion = "Observarlo de cerca con la lupa", orden = 2, debeIrAntesDeCsv = "dibujar_detalle"),
        PasoProcedimientoEntity(pasoId = "dibujar_detalle", expedicionId = "lupa_insecto", descripcion = "Dibujar los detalles que se ven con la lupa", orden = 3, debeIrAntesDeCsv = "comparar_ojo"),
        PasoProcedimientoEntity(pasoId = "comparar_ojo", expedicionId = "lupa_insecto", descripcion = "Comparar con lo que se ve a simple vista", orden = 4, debeIrAntesDeCsv = ""),

        // El Camino más Corto (5 pasos)
        PasoProcedimientoEntity(pasoId = "marcar_rutas", expedicionId = "camino_corto", descripcion = "Marcar dos rutas posibles entre los mismos dos puntos", orden = 1, debeIrAntesDeCsv = "medir_ruta_a,medir_ruta_b"),
        PasoProcedimientoEntity(pasoId = "medir_ruta_a", expedicionId = "camino_corto", descripcion = "Medir la primera ruta con la cinta métrica", orden = 2, debeIrAntesDeCsv = "comparar_rutas"),
        PasoProcedimientoEntity(pasoId = "medir_ruta_b", expedicionId = "camino_corto", descripcion = "Medir la segunda ruta con la cinta métrica", orden = 3, debeIrAntesDeCsv = "comparar_rutas"),
        PasoProcedimientoEntity(pasoId = "comparar_rutas", expedicionId = "camino_corto", descripcion = "Comparar las dos distancias medidas", orden = 4, debeIrAntesDeCsv = "anotar_ruta"),
        PasoProcedimientoEntity(pasoId = "anotar_ruta", expedicionId = "camino_corto", descripcion = "Anotar cuál ruta es más corta", orden = 5, debeIrAntesDeCsv = ""),

        // La Expedición Final (5 pasos, combina temperatura y distancia)
        PasoProcedimientoEntity(pasoId = "revisar_termometro_final", expedicionId = "expedicion_final", descripcion = "Revisar la temperatura del punto de partida", orden = 1, debeIrAntesDeCsv = "medir_distancia_final"),
        PasoProcedimientoEntity(pasoId = "medir_distancia_final", expedicionId = "expedicion_final", descripcion = "Medir la distancia del recorrido con la cinta métrica", orden = 2, debeIrAntesDeCsv = "cronometrar_final"),
        PasoProcedimientoEntity(pasoId = "cronometrar_final", expedicionId = "expedicion_final", descripcion = "Cronometrar cuánto toma recorrerla", orden = 3, debeIrAntesDeCsv = "comparar_condiciones"),
        PasoProcedimientoEntity(pasoId = "comparar_condiciones", expedicionId = "expedicion_final", descripcion = "Comparar el resultado bajo dos condiciones distintas", orden = 4, debeIrAntesDeCsv = "anotar_final"),
        PasoProcedimientoEntity(pasoId = "anotar_final", expedicionId = "expedicion_final", descripcion = "Anotar la conclusión final de la expedición", orden = 5, debeIrAntesDeCsv = ""),
    )

    val insignias = listOf(
        InsigniaEntity("primer_plan", "Primer Plan", "Sellar el primer plan de expedición", null),
        InsigniaEntity("instrumento_correcto", "Instrumento Correcto", "Elegir el instrumento correcto para la variable, 5 veces seguidas", null),
        InsigniaEntity("secuencia_perfecta", "Secuencia Perfecta", "Armar una secuencia de pasos sin reordenar ninguno tras el primer intento", null),
        InsigniaEntity("repeticion_sabia", "Repetición Sabia", "Elegir el número de repeticiones correcto (ni de más ni de menos) 5 veces", null),
        InsigniaEntity("base_completa", "Base Completa", "Sellar los 8 planes de expedición", null),
        InsigniaEntity("planificador_veloz", "Planificador Veloz", "Sellar un plan válido al primer intento, 3 veces", null),
        InsigniaEntity("cuaderno_lleno", "Cuaderno Lleno", "8 planes guardados con todas sus piezas completas", null),
        InsigniaEntity("ruta_dificil", "Ruta Difícil", "Sellar un plan con 3 variables a controlar a la vez", null),
        InsigniaEntity("sin_atajos", "Sin Atajos", "Completar un plan sin que Tuco tuviera que señalar ningún error", null),
        InsigniaEntity("mentor_de_tuco", "Mentor de Tuco", "Ayudar a Tuco a esperar (no soltar la expedición) 10 veces", null),
        InsigniaEntity("racha_de_campamento", "Racha de Campamento", "5 días seguidos con al menos un plan avanzado", null),
    )
}
