package it.unibo.alessiociarrocchi.tesiahc.presentation

import android.content.Context
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import it.unibo.alessiociarrocchi.tesiahc.data.AppContainerImpl
import it.unibo.alessiociarrocchi.tesiahc.presentation.screen.bloodpressure.BloodPressureViewModel
import it.unibo.alessiociarrocchi.tesiahc.presentation.screen.bloodpressuredetail.BloodPressureDetailViewModel
import it.unibo.alessiociarrocchi.tesiahc.presentation.screen.locationGps.LocationViewModel

object AppViewModelProvider {

    fun provideViewModel(context: Context) = viewModelFactory {
        initializer {
            BloodPressureViewModel(
                settingsRepository = AppContainerImpl(context).settingsRepository,
                bloodPressureRepository = AppContainerImpl(context).bloodPressureRepository
            )
        }
        initializer {
            BloodPressureDetailViewModel(
                this.createSavedStateHandle(),
                bloodPressureRepository = AppContainerImpl(context).bloodPressureRepository,
                heartRateRepository = AppContainerImpl(context).heartRateRepository
            )
        }

        initializer {
            LocationViewModel(
                locationRepository = AppContainerImpl(context).locationRepository,
                settingsRepository = AppContainerImpl(context).settingsRepository
            )
        }
    }
}