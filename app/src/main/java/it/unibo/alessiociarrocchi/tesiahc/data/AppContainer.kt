package it.unibo.alessiociarrocchi.tesiahc.data

import android.content.Context
import it.unibo.alessiociarrocchi.tesiahc.data.repository.BloodOxygenRepository
import it.unibo.alessiociarrocchi.tesiahc.data.repository.HeartRateRepository
import it.unibo.alessiociarrocchi.tesiahc.data.repository.LocationRepository
import it.unibo.alessiociarrocchi.tesiahc.data.repository.BloodPressureRepository
import it.unibo.alessiociarrocchi.tesiahc.data.repository.ExerciseRepository
import it.unibo.alessiociarrocchi.tesiahc.data.repository.SettingsRepository
import it.unibo.alessiociarrocchi.tesiahc.data.repository.StepsRepository
import it.unibo.alessiociarrocchi.tesiahc.presentation.screen.locationGps.LocationScreen


/**
 * Dependency container for the app
 */

interface AppContainer {

    val settingsRepository: SettingsRepository
    val heartRateRepository: HeartRateRepository
    val locationRepository: LocationRepository
    val bloodPressureRepository: BloodPressureRepository
    val stepsRepository: StepsRepository
    val exerciseRepository: ExerciseRepository
    val boRepository: BloodOxygenRepository
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

    override val stepsRepository: StepsRepository by lazy {
        StepsRepository(LocalDatabase.getDatabase(context).stepsDao())
    }

    override val exerciseRepository: ExerciseRepository by lazy {
        ExerciseRepository(LocalDatabase.getDatabase(context).exerciseDao())
    }

    override val boRepository: BloodOxygenRepository by lazy {
        BloodOxygenRepository(LocalDatabase.getDatabase(context).boDao())
    }
}