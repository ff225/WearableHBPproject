package it.unibo.alessiociarrocchi.tesiahc.data

import android.content.Context
import it.unibo.alessiociarrocchi.tesiahc.data.db.MyLocalDatabase

/**
 * App container for Dependency injection.
 */
interface AppContainer {
    val bpRepository: BloodPressureRepository
}

/**
 * [AppContainer] implementation that provides instance of [BloodPressureRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [BloodPressureRepository]
     */
    override val bpRepository: BloodPressureRepository by lazy {
        BloodPressureRepository(MyLocalDatabase.getDatabase(context).bpDao())
    }

}