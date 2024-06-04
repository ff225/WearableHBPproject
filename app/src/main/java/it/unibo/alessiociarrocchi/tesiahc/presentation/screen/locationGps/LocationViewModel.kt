package it.unibo.alessiociarrocchi.tesiahc.presentation.screen.locationGps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import it.unibo.alessiociarrocchi.tesiahc.data.MyLocationRepository
import it.unibo.alessiociarrocchi.tesiahc.data.db.MyLocationEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LocationViewModel(private val myLocationRepository: MyLocationRepository) : ViewModel() {

    //val periodStart = Instant.now().minus(30, ChronoUnit.DAYS)
    //val periodEnd = Instant.now()
    //val locList = locationRepository.getLocations()
    private val _locList = MutableStateFlow(emptyList<MyLocationEntity>())
    val locList = _locList.asStateFlow()

    fun initialLoad() {
        refreshList()
    }

    fun deleteLocationAndRefresh(uid:Int){
        deleteLocation(uid)
        refreshList()
    }

    private fun deleteLocation(uid:Int){
        myLocationRepository.deleteLocation(uid)
    }

    private fun refreshList(){
        viewModelScope.launch {
            _locList.value = myLocationRepository.getLocations()
        }
    }

}

class LocationViewModelFactory(
    private val myLocationRepository: MyLocationRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LocationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LocationViewModel(
                myLocationRepository = myLocationRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
