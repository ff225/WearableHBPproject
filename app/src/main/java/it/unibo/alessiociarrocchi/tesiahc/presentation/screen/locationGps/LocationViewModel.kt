package it.unibo.alessiociarrocchi.tesiahc.presentation.screen.locationGps

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.unibo.alessiociarrocchi.tesiahc.data.model.LocationEntity
import it.unibo.alessiociarrocchi.tesiahc.data.repository.LocationRepository
import it.unibo.alessiociarrocchi.tesiahc.data.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.temporal.ChronoUnit

class LocationViewModel(
    private val locationRepository: LocationRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _locList = MutableStateFlow(emptyList<LocationEntity>())
    val locList = _locList.asStateFlow()

    init {
        val date =
            Instant.now().truncatedTo(ChronoUnit.DAYS).toEpochMilli()

        Log.d("BloodPressureViewModel", "date: $date")
        viewModelScope.launch {
            locationRepository.getLocationsToday(date)
                .let {
                    _locList.value = it
                }
        }
    }

    fun deleteItem(id: Int) {
        viewModelScope.launch {
            locationRepository.deleteLocation(id)
            val date =
                Instant.now().truncatedTo(ChronoUnit.DAYS).toEpochMilli()
            locationRepository.getLocationsToday(date)
                .let {
                    _locList.value = it
                }
        }
    }

}
/*  fun initialLoad() {
      val sdf = SimpleDateFormat("yyyy-MM-dd")
      _currDate = sdf.format(Date())

      refreshList()
  }

  fun deleteLocationAndRefresh(uid:Int){
      deleteLocation(uid)
      refreshList()
  }

  private fun deleteLocation(uid:Int){
      //myLocationRepository.deleteLocation(uid)
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
              getFiltersOrToday()
          }
      }
      else{
          getFiltersOrToday()
      }
  }

  private fun getFiltersOrToday(){
      /*
      var dfi = mySettRepository.getItem(GetKey_FilterDataInzio())
      if (dfi != null){
          if(dfi.valore != ""){
              var myout : String = dfi.valore

              var dff = mySettRepository.getItem(GetKey_FilterDataFine())
              if (dff != null){
                  if(dff.valore != ""){
                      myout += "|" + dff.valore
                  }
              }

              refreshWithFilters(myout)
              return
          }
      }

      viewModelScope.launch {
          _locList.value = myLocationRepository.getLocationsToday(_currDate)
      }

       */
  }

  fun refreshWithFilters(dates:String){
      _currDateFilters = dates
      refreshList()
  }*/

