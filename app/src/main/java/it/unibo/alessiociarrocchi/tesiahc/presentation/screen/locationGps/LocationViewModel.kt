package it.unibo.alessiociarrocchi.tesiahc.presentation.screen.locationGps
/*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import it.unibo.alessiociarrocchi.tesiahc.data.MyLocationRepository
import it.unibo.alessiociarrocchi.tesiahc.data.db.MyLocationEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date

class LocationViewModel(private val myLocationRepository: MyLocationRepository) : ViewModel() {

    //val periodStart = Instant.now().minus(30, ChronoUnit.DAYS)
    //val periodEnd = Instant.now()
    //val locList = locationRepository.getLocations()
    private val _locList = MutableStateFlow(emptyList<MyLocationEntity>())
    val locList = _locList.asStateFlow()
    private var _currDate = ""
    private var _currDateFilters = ""

    fun initialLoad() {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        _currDate = sdf.format(Date())

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
        if (_currDateFilters.isNotEmpty()){
            val dataInizio = _currDateFilters.substringBefore('|').trim()
            val dataFine = _currDateFilters.substringAfter('|').trim()
            if(dataFine.isNotEmpty()){
                viewModelScope.launch {
                    _locList.value = myLocationRepository.getLocationsDates(dataInizio, dataFine)
                }
            }
            else{
                refreshToday()
            }
        }
        else{
            refreshToday()
        }
    }

    private fun refreshToday(){
        viewModelScope.launch {
            _locList.value = myLocationRepository.getLocationsToday(_currDate)
        }
    }

    fun refreshWithFilters(dates:String){
        _currDateFilters = dates
        refreshList()
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
*/