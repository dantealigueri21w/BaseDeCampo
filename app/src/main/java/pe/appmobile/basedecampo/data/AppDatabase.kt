package pe.appmobile.basedecampo.data

import androidx.room.Database
import androidx.room.RoomDatabase
import pe.appmobile.basedecampo.data.dao.ExpedicionDao
import pe.appmobile.basedecampo.data.dao.InsigniaDao
import pe.appmobile.basedecampo.data.dao.InstrumentoDao
import pe.appmobile.basedecampo.data.dao.PasoProcedimientoDao
import pe.appmobile.basedecampo.data.dao.PerfilDao
import pe.appmobile.basedecampo.data.dao.PlanSelladoDao
import pe.appmobile.basedecampo.data.dao.RachaDao
import pe.appmobile.basedecampo.data.dao.RepasoPendienteDao
import pe.appmobile.basedecampo.data.entity.ExpedicionEntity
import pe.appmobile.basedecampo.data.entity.InsigniaEntity
import pe.appmobile.basedecampo.data.entity.InstrumentoEntity
import pe.appmobile.basedecampo.data.entity.PasoProcedimientoEntity
import pe.appmobile.basedecampo.data.entity.PerfilEntity
import pe.appmobile.basedecampo.data.entity.PlanSelladoEntity
import pe.appmobile.basedecampo.data.entity.RachaEntity
import pe.appmobile.basedecampo.data.entity.RepasoPendienteEntity

@Database(
    entities = [
        PerfilEntity::class,
        ExpedicionEntity::class,
        InstrumentoEntity::class,
        PasoProcedimientoEntity::class,
        PlanSelladoEntity::class,
        InsigniaEntity::class,
        RachaEntity::class,
        RepasoPendienteEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun perfilDao(): PerfilDao
    abstract fun expedicionDao(): ExpedicionDao
    abstract fun instrumentoDao(): InstrumentoDao
    abstract fun pasoProcedimientoDao(): PasoProcedimientoDao
    abstract fun planSelladoDao(): PlanSelladoDao
    abstract fun insigniaDao(): InsigniaDao
    abstract fun rachaDao(): RachaDao
    abstract fun repasoPendienteDao(): RepasoPendienteDao
}
