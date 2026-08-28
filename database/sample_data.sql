-- Datos semilla reales, transcritos de app/src/main/java/pe/appmobile/basedecampo/data/seed/SeedData.kt
-- Se insertan una sola vez, la primera vez que la app corre en un dispositivo sin datos
-- (ExpedicionRepository.sembrarSiEsPrimerLanzamiento).

INSERT INTO instrumento (id, nombre, tipo, mideVariable) VALUES
('vaso_graduado', 'Vaso graduado', 'VASO_GRADUADO', 'VOLUMEN'),
('balanza', 'Balanza', 'BALANZA', 'PESO'),
('termometro', 'Termómetro', 'TERMOMETRO', 'TEMPERATURA'),
('regla', 'Regla', 'REGLA', 'LONGITUD'),
('cronometro', 'Cronómetro', 'CRONOMETRO', 'TIEMPO'),
('lupa', 'Lupa', 'LUPA', 'DETALLE_VISUAL'),
('cinta_metrica', 'Cinta métrica', 'CINTA_METRICA', 'DISTANCIA');

INSERT INTO expedicion (id, nombre, pregunta, variableAMedir, instrumentoCorrectoId, repeticionesMinimas, orden) VALUES
('sombra_sol', 'La Sombra y el Sol', '¿El agua se evapora más rápido con sol o con sombra?', 'VOLUMEN', 'vaso_graduado', 3, 1),
('peso_piedra', 'El Peso de la Piedra', '¿Qué tan pesada es cada piedra de la quebrada?', 'PESO', 'balanza', 3, 2),
('frio_puna', 'El Frío de la Puna', '¿Cuánto baja la temperatura de noche a distintas alturas?', 'TEMPERATURA', 'termometro', 3, 3),
('semilla_crece', 'La Semilla que Crece', '¿Qué tan rápido germina una semilla con distinta cantidad de agua?', 'LONGITUD', 'regla', 5, 4),
('eco_quebrada', 'El Eco de la Quebrada', '¿El eco tarda más en un espacio angosto o uno abierto?', 'TIEMPO', 'cronometro', 3, 5),
('lupa_insecto', 'La Lupa del Insecto', '¿Qué detalles reales tiene un insecto que a simple vista no se ven?', 'DETALLE_VISUAL', 'lupa', 2, 6),
('camino_corto', 'El Camino más Corto', '¿Qué ruta entre dos puntos del campamento es más corta?', 'DISTANCIA', 'cinta_metrica', 3, 7),
('expedicion_final', 'La Expedición Final', '¿Cómo cambian distancia y tiempo del mismo recorrido bajo distintas condiciones?', 'DISTANCIA', 'cinta_metrica', 4, 8);

-- paso_procedimiento: pasoDbId es autogenerado, se omite; debeIrAntesDeCsv en blanco significa "sin pasos que deban ir despues"
INSERT INTO paso_procedimiento (pasoId, expedicionId, descripcion, orden, debeIrAntesDeCsv) VALUES
-- La Sombra y el Sol (5 pasos)
('preparar_vasos', 'sombra_sol', 'Llenar dos vasos graduados con la misma cantidad de agua', 1, 'colocar_sol,colocar_sombra'),
('colocar_sol', 'sombra_sol', 'Dejar un vaso bajo el sol directo', 2, 'esperar_evaporacion'),
('colocar_sombra', 'sombra_sol', 'Dejar el otro vaso a la sombra', 3, 'esperar_evaporacion'),
('esperar_evaporacion', 'sombra_sol', 'Esperar el mismo tiempo en los dos lugares', 4, 'medir_evaporacion'),
('medir_evaporacion', 'sombra_sol', 'Medir cuánta agua quedó en cada vaso', 5, ''),
-- El Peso de la Piedra (5 pasos)
('ajustar_balanza', 'peso_piedra', 'Poner la balanza en cero antes de pesar', 1, 'pesar_a,pesar_b'),
('pesar_a', 'peso_piedra', 'Pesar la piedra A', 2, 'comparar_pesos'),
('pesar_b', 'peso_piedra', 'Pesar la piedra B', 3, 'comparar_pesos'),
('comparar_pesos', 'peso_piedra', 'Comparar los dos pesos', 4, 'anotar_peso'),
('anotar_peso', 'peso_piedra', 'Anotar cuál piedra pesa más', 5, ''),
-- El Frío de la Puna (4 pasos)
('preparar_termometro', 'frio_puna', 'Revisar que el termómetro marque bien antes de salir', 1, 'colocar'),
('colocar', 'frio_puna', 'Colocar el termómetro en el punto de medición', 2, 'esperar_estabilizar'),
('esperar_estabilizar', 'frio_puna', 'Esperar a que el termómetro se estabilice', 3, 'leer'),
('leer', 'frio_puna', 'Leer la temperatura marcada', 4, ''),
-- La Semilla que Crece (4 pasos)
('sembrar', 'semilla_crece', 'Sembrar semillas con distinta cantidad de agua', 1, 'esperar_dias'),
('esperar_dias', 'semilla_crece', 'Esperar varios días a que germinen', 2, 'medir_brote'),
('medir_brote', 'semilla_crece', 'Medir la altura del brote con la regla', 3, 'anotar_altura'),
('anotar_altura', 'semilla_crece', 'Anotar la altura medida', 4, ''),
-- El Eco de la Quebrada (4 pasos)
('elegir_espacio', 'eco_quebrada', 'Elegir un espacio angosto y uno abierto para comparar', 1, 'gritar'),
('gritar', 'eco_quebrada', 'Hacer un sonido y empezar a contar', 2, 'medir_eco'),
('medir_eco', 'eco_quebrada', 'Medir con el cronómetro cuánto tarda en volver el eco', 3, 'anotar_eco'),
('anotar_eco', 'eco_quebrada', 'Anotar el tiempo del eco en cada espacio', 4, ''),
-- La Lupa del Insecto (4 pasos)
('encontrar_insecto', 'lupa_insecto', 'Encontrar un insecto sin tocarlo', 1, 'observar_lupa'),
('observar_lupa', 'lupa_insecto', 'Observarlo de cerca con la lupa', 2, 'dibujar_detalle'),
('dibujar_detalle', 'lupa_insecto', 'Dibujar los detalles que se ven con la lupa', 3, 'comparar_ojo'),
('comparar_ojo', 'lupa_insecto', 'Comparar con lo que se ve a simple vista', 4, ''),
-- El Camino más Corto (5 pasos)
('marcar_rutas', 'camino_corto', 'Marcar dos rutas posibles entre los mismos dos puntos', 1, 'medir_ruta_a,medir_ruta_b'),
('medir_ruta_a', 'camino_corto', 'Medir la primera ruta con la cinta métrica', 2, 'comparar_rutas'),
('medir_ruta_b', 'camino_corto', 'Medir la segunda ruta con la cinta métrica', 3, 'comparar_rutas'),
('comparar_rutas', 'camino_corto', 'Comparar las dos distancias medidas', 4, 'anotar_ruta'),
('anotar_ruta', 'camino_corto', 'Anotar cuál ruta es más corta', 5, ''),
-- La Expedición Final (5 pasos, combina temperatura y distancia)
('revisar_termometro_final', 'expedicion_final', 'Revisar la temperatura del punto de partida', 1, 'medir_distancia_final'),
('medir_distancia_final', 'expedicion_final', 'Medir la distancia del recorrido con la cinta métrica', 2, 'cronometrar_final'),
('cronometrar_final', 'expedicion_final', 'Cronometrar cuánto toma recorrerla', 3, 'comparar_condiciones'),
('comparar_condiciones', 'expedicion_final', 'Comparar el resultado bajo dos condiciones distintas', 4, 'anotar_final'),
('anotar_final', 'expedicion_final', 'Anotar la conclusión final de la expedición', 5, '');

INSERT INTO insignia (id, nombre, descripcion, fechaObtenida) VALUES
('primer_plan', 'Primer Plan', 'Sellar el primer plan de expedición', NULL),
('instrumento_correcto', 'Instrumento Correcto', 'Elegir el instrumento correcto para la variable, 5 veces seguidas', NULL),
('secuencia_perfecta', 'Secuencia Perfecta', 'Armar una secuencia de pasos sin reordenar ninguno tras el primer intento', NULL),
('repeticion_sabia', 'Repetición Sabia', 'Elegir el número de repeticiones correcto (ni de más ni de menos) 5 veces', NULL),
('base_completa', 'Base Completa', 'Sellar los 8 planes de expedición', NULL),
('planificador_veloz', 'Planificador Veloz', 'Sellar un plan válido al primer intento, 3 veces', NULL),
('cuaderno_lleno', 'Cuaderno Lleno', '8 planes guardados con todas sus piezas completas', NULL),
('ruta_dificil', 'Ruta Difícil', 'Sellar un plan con 3 variables a controlar a la vez', NULL),
('sin_atajos', 'Sin Atajos', 'Completar un plan sin que Tuco tuviera que señalar ningún error', NULL),
('mentor_de_tuco', 'Mentor de Tuco', 'Ayudar a Tuco a esperar (no soltar la expedición) 10 veces', NULL),
('racha_de_campamento', 'Racha de Campamento', '5 días seguidos con al menos un plan avanzado', NULL);
