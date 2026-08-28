package pe.appmobile.basedecampo.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pe.appmobile.basedecampo.data.entity.ExpedicionEntity
import pe.appmobile.basedecampo.data.entity.InsigniaEntity
import pe.appmobile.basedecampo.data.entity.InstrumentoEntity
import pe.appmobile.basedecampo.data.entity.PasoProcedimientoEntity
import pe.appmobile.basedecampo.data.entity.PerfilEntity
import pe.appmobile.basedecampo.data.entity.PlanSelladoEntity
import pe.appmobile.basedecampo.data.entity.RachaEntity
import pe.appmobile.basedecampo.data.entity.RepasoPendienteEntity

@Dao
interface PerfilDao {
    @Query("SELECT * FROM perfil WHERE id = 1")
    suspend fun obtener(): PerfilEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardar(perfil: PerfilEntity)
}

@Dao
interface ExpedicionDao {
    @Query("SELECT * FROM expedicion ORDER BY orden")
    suspend fun obtenerTodas(): List<ExpedicionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodas(expediciones: List<ExpedicionEntity>)

    @Query("DELETE FROM expedicion WHERE id = :id")
    suspend fun eliminar(id: String)
}

@Dao
interface InstrumentoDao {
    @Query("SELECT * FROM instrumento")
    suspend fun obtenerTodos(): List<InstrumentoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodos(instrumentos: List<InstrumentoEntity>)
}

@Dao
interface PasoProcedimientoDao {
    @Query("SELECT * FROM paso_procedimiento WHERE expedicionId = :expedicionId ORDER BY orden")
    suspend fun obtenerPorExpedicion(expedicionId: String): List<PasoProcedimientoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodos(pasos: List<PasoProcedimientoEntity>)
}

@Dao
interface PlanSelladoDao {
    @Insert
    suspend fun insertar(plan: PlanSelladoEntity): Long

    @Query("SELECT * FROM plan_sellado ORDER BY fechaSellado")
    suspend fun obtenerTodos(): List<PlanSelladoEntity>
}

@Dao
interface InsigniaDao {
    @Query("SELECT * FROM insignia")
    suspend fun obtenerTodas(): List<InsigniaEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodas(insignias: List<InsigniaEntity>)

    @Query("UPDATE insignia SET fechaObtenida = :fecha WHERE id = :insigniaId")
    suspend fun marcarObtenida(insigniaId: String, fecha: Long)

    @Query("SELECT id FROM insignia WHERE fechaObtenida IS NOT NULL")
    suspend fun obtenerIdsGanadas(): List<String>
}

@Dao
interface RachaDao {
    @Query("SELECT * FROM racha WHERE id = 1")
    suspend fun obtener(): RachaEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardar(racha: RachaEntity)
}

@Dao
interface RepasoPendienteDao {
    @Query("SELECT * FROM repaso_pendiente WHERE proximaRevision <= :hoy")
    suspend fun obtenerPendientesParaHoy(hoy: Long): List<RepasoPendienteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardar(item: RepasoPendienteEntity)

    @Query("SELECT * FROM repaso_pendiente WHERE itemId = :itemId")
    suspend fun obtenerPorId(itemId: String): RepasoPendienteEntity?
}
