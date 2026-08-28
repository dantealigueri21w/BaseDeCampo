package pe.appmobile.basedecampo.ui.screens

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import pe.appmobile.basedecampo.data.AppDatabase
import pe.appmobile.basedecampo.data.repository.ExpedicionRepository
import pe.appmobile.basedecampo.ui.theme.BaseDeCampoTheme

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ParentalGateScreenTest {
    @get:Rule
    val compose = createComposeRule()

    private lateinit var db: AppDatabase
    private lateinit var repository: ExpedicionRepository

    @Before
    fun crearDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).allowMainThreadQueries().build()
        repository = ExpedicionRepository(db)
    }

    @After
    fun cerrarDb() {
        db.close()
    }

    @Test
    fun `el gate cerrado no revienta y muestra la instruccion de mantener presionado`() {
        compose.setContent {
            BaseDeCampoTheme {
                ParentalGateScreen(repository = repository)
            }
        }
        compose.onNodeWithText("Mantén presionado 3 segundos").assertExists()
    }
}
