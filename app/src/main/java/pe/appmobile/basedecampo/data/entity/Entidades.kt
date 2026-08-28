package pe.appmobile.basedecampo.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "perfil")
data class PerfilEntity(
    @PrimaryKey val id: Int = 1,
    val alias: String,
    val avatarId: Int,
)

@Entity(tableName = "expedicion")
data class ExpedicionEntity(
    @PrimaryKey val id: String,
    val nombre: String,
    val pregunta: String,
    val variableAMedir: String,
    val instrumentoCorrectoId: String,
    val repeticionesMinimas: Int,
    val orden: Int,
)

@Entity(tableName = "instrumento")
data class InstrumentoEntity(
    @PrimaryKey val id: String,
    val nombre: String,
    val tipo: String,
    val mideVariable: String,
)

@Entity(
    tableName = "paso_procedimiento",
    foreignKeys = [
        ForeignKey(
            entity = ExpedicionEntity::class,
            parentColumns = ["id"],
            childColumns = ["expedicionId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("expedicionId")],
)
data class PasoProcedimientoEntity(
    @PrimaryKey(autoGenerate = true) val pasoDbId: Long = 0,
    val pasoId: String,
    val expedicionId: String,
    val descripcion: String,
    val orden: Int,
    val debeIrAntesDeCsv: String,
)

@Entity(
    tableName = "plan_sellado",
    foreignKeys = [
        ForeignKey(
            entity = ExpedicionEntity::class,
            parentColumns = ["id"],
            childColumns = ["expedicionId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("expedicionId")],
)
data class PlanSelladoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val expedicionId: String,
    val instrumentoElegidoId: String,
    val ordenPasosCsv: String,
    val repeticionesElegidas: Int,
    val fechaSellado: Long,
    val alPrimerIntento: Boolean,
)

@Entity(tableName = "insignia")
data class InsigniaEntity(
    @PrimaryKey val id: String,
    val nombre: String,
    val descripcion: String,
    val fechaObtenida: Long?,
)

@Entity(tableName = "racha")
data class RachaEntity(
    @PrimaryKey val id: Int = 1,
    val diasConsecutivos: Int,
    val ultimaFechaActividad: Long,
)

@Entity(tableName = "repaso_pendiente")
data class RepasoPendienteEntity(
    @PrimaryKey val itemId: String,
    val fechaUltimoFallo: Long,
    val intervaloDias: Int,
    val proximaRevision: Long,
)
