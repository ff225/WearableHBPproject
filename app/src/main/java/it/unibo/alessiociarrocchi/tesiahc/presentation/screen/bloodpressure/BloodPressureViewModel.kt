package it.unibo.alessiociarrocchi.tesiahc.presentation.screen.bloodpressure

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.unibo.alessiociarrocchi.tesiahc.data.model.BloodPressureEntity
import it.unibo.alessiociarrocchi.tesiahc.data.repository.BloodPressureRepository
import it.unibo.alessiociarrocchi.tesiahc.data.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.TimeZone

class BloodPressureViewModel(
    bloodPressureRepository: BloodPressureRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private var _bpList: MutableStateFlow<List<BloodPressureEntity>> = MutableStateFlow(emptyList())
    val bpList = _bpList.asStateFlow()


    init {
        // work with timestamp instead of Date
        //val sdf = SimpleDateFormat("yyyy-MM-dd")
        //_currDate = sdf.format(Date())
        //Log.d("BloodPressureViewModel", "currDate: $_currDate")
        val date =
            Instant.now().atZone(TimeZone.getDefault().toZoneId()).truncatedTo(ChronoUnit.DAYS)
                .toEpochSecond()

        Log.d("BloodPressureViewModel", "date: $date")
        viewModelScope.launch {
            bloodPressureRepository.getItemsToday(
                Instant.now().atZone(TimeZone.getDefault().toZoneId()).truncatedTo(ChronoUnit.DAYS)
                    .toEpochSecond()
            ).let {
                _bpList.value = it
            }
        }
    }
}

/*
    private fun refreshList() {
        if (_currDateFilters.isNotEmpty()) {
            val dataInizio = _currDateFilters.substringBefore('|').trim()
            val dataFine = _currDateFilters.substringAfter('|').trim()
            if (dataFine.isNotEmpty()) {
                viewModelScope.launch {
                    _bpList.value = myBPRepository.getItemsByDates(dataInizio, dataFine)
                }
            } else {
                getFiltersOrToday()
            }
        } else {
            getFiltersOrToday()
        }
    }*/
/*
    private fun getFiltersOrToday() {

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
            _bpList.value = myBPRepository.getItemsToday(_currDate)
        }


    }
*/
/*
fun refreshWithFilters(dates: String) {
_currDateFilters = dates
//refreshList()
}
*/
