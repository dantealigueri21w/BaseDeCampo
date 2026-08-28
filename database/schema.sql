-- Esquema real de Room, exportado desde app/schemas/pe.appmobile.basedecampo.data.AppDatabase/1.json
-- Base de Campo -- version de esquema 1

CREATE TABLE IF NOT EXISTS `perfil` (`id` INTEGER NOT NULL, `alias` TEXT NOT NULL, `avatarId` INTEGER NOT NULL, PRIMARY KEY(`id`));

CREATE TABLE IF NOT EXISTS `expedicion` (`id` TEXT NOT NULL, `nombre` TEXT NOT NULL, `pregunta` TEXT NOT NULL, `variableAMedir` TEXT NOT NULL, `instrumentoCorrectoId` TEXT NOT NULL, `repeticionesMinimas` INTEGER NOT NULL, `orden` INTEGER NOT NULL, PRIMARY KEY(`id`));

CREATE TABLE IF NOT EXISTS `instrumento` (`id` TEXT NOT NULL, `nombre` TEXT NOT NULL, `tipo` TEXT NOT NULL, `mideVariable` TEXT NOT NULL, PRIMARY KEY(`id`));

CREATE TABLE IF NOT EXISTS `paso_procedimiento` (`pasoDbId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `pasoId` TEXT NOT NULL, `expedicionId` TEXT NOT NULL, `descripcion` TEXT NOT NULL, `orden` INTEGER NOT NULL, `debeIrAntesDeCsv` TEXT NOT NULL, FOREIGN KEY(`expedicionId`) REFERENCES `expedicion`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE );
CREATE INDEX IF NOT EXISTS `index_paso_procedimiento_expedicionId` ON `paso_procedimiento` (`expedicionId`);

CREATE TABLE IF NOT EXISTS `plan_sellado` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `expedicionId` TEXT NOT NULL, `instrumentoElegidoId` TEXT NOT NULL, `ordenPasosCsv` TEXT NOT NULL, `repeticionesElegidas` INTEGER NOT NULL, `fechaSellado` INTEGER NOT NULL, `alPrimerIntento` INTEGER NOT NULL, FOREIGN KEY(`expedicionId`) REFERENCES `expedicion`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE );
CREATE INDEX IF NOT EXISTS `index_plan_sellado_expedicionId` ON `plan_sellado` (`expedicionId`);

CREATE TABLE IF NOT EXISTS `insignia` (`id` TEXT NOT NULL, `nombre` TEXT NOT NULL, `descripcion` TEXT NOT NULL, `fechaObtenida` INTEGER, PRIMARY KEY(`id`));

CREATE TABLE IF NOT EXISTS `racha` (`id` INTEGER NOT NULL, `diasConsecutivos` INTEGER NOT NULL, `ultimaFechaActividad` INTEGER NOT NULL, PRIMARY KEY(`id`));

CREATE TABLE IF NOT EXISTS `repaso_pendiente` (`itemId` TEXT NOT NULL, `fechaUltimoFallo` INTEGER NOT NULL, `intervaloDias` INTEGER NOT NULL, `proximaRevision` INTEGER NOT NULL, PRIMARY KEY(`itemId`));
