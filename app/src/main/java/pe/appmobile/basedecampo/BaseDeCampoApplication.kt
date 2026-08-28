package pe.appmobile.basedecampo

import android.app.Application
import androidx.room.Room
import pe.appmobile.basedecampo.data.AppDatabase
import pe.appmobile.basedecampo.data.repository.ExpedicionRepository

class BaseDeCampoApplication : Application() {
    lateinit var database: AppDatabase
        private set
    lateinit var repository: ExpedicionRepository
        private set

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(this, AppDatabase::class.java, "basedecampo.db").build()
        repository = ExpedicionRepository(database)
    }
}
