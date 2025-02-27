package it.unibo.alessiociarrocchi.tesiahc.data

import android.content.Context
import it.unibo.alessiociarrocchi.tesiahc.data.repository.HeartRateRepository
import it.unibo.alessiociarrocchi.tesiahc.data.repository.LocationRepository
import it.unibo.alessiociarrocchi.tesiahc.data.repository.MyBloodPressureRepository
import it.unibo.alessiociarrocchi.tesiahc.data.repository.SettingsRepository


/**
 * Dependency container for the app
 */

interface AppContainer {

    val mySettingsRepository: SettingsRepository
    val myHeartRateRepository: HeartRateRepository
    val myLocationRepository: LocationRepository
    val myBloodPressureRepository: MyBloodPressureRepository

}

class AppContainerImpl(context: Context) : AppContainer {
    override val mySettingsRepository: SettingsRepository by lazy {
        SettingsRepository(LocalDatabase.getDatabase(context).settingsDao())
    }
    override val myHeartRateRepository: HeartRateRepository by lazy {
        HeartRateRepository(LocalDatabase.getDatabase(context).heartRateAggregateDao())
    }
    override val myLocationRepository: LocationRepository by lazy {
        LocationRepository(LocalDatabase.getDatabase(context).locationDao())
    }
    override val myBloodPressureRepository: MyBloodPressureRepository by lazy {
        MyBloodPressureRepository(LocalDatabase.getDatabase(context).bloodPressureDao())
    }
}