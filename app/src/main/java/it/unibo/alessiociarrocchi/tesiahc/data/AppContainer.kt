package it.unibo.alessiociarrocchi.tesiahc.data

import android.content.Context
import it.unibo.alessiociarrocchi.tesiahc.data.repository.HeartRateRepository
import it.unibo.alessiociarrocchi.tesiahc.data.repository.LocationRepository
import it.unibo.alessiociarrocchi.tesiahc.data.repository.BloodPressureRepository
import it.unibo.alessiociarrocchi.tesiahc.data.repository.SettingsRepository


/**
 * Dependency container for the app
 */

interface AppContainer {

    val settingsRepository: SettingsRepository
    val heartRateRepository: HeartRateRepository
    val locationRepository: LocationRepository
    val bloodPressureRepository: BloodPressureRepository

}

class AppContainerImpl(context: Context) : AppContainer {
    override val settingsRepository: SettingsRepository by lazy {
        SettingsRepository(LocalDatabase.getDatabase(context).settingsDao())
    }
    override val heartRateRepository: HeartRateRepository by lazy {
        HeartRateRepository(LocalDatabase.getDatabase(context).heartRateAggregateDao())
    }
    override val locationRepository: LocationRepository by lazy {
        LocationRepository(LocalDatabase.getDatabase(context).locationDao())
    }
    override val bloodPressureRepository: BloodPressureRepository by lazy {
        BloodPressureRepository(LocalDatabase.getDatabase(context).bloodPressureDao())
    }
}