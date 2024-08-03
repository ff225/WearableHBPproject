package it.unibo.alessiociarrocchi.tesiahc.presentation.screen.bloodpressure

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import it.unibo.alessiociarrocchi.tesiahc.data.MyBloodPressureRepository
import it.unibo.alessiociarrocchi.tesiahc.data.db.MyBloodPressureEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

class BloodPressureViewModel(private val myBPRepository: MyBloodPressureRepository) :
ViewModel() {

    private val _bpList = MutableStateFlow(emptyList<MyBloodPressureEntity>())
    val bpList = _bpList.asStateFlow()
    private var _currDate = ""
    private var _currDateFilters = ""

    fun initialLoad() {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        _currDate = sdf.format(Date())

        refreshList()
    }

    private fun refreshList(){
        if (_currDateFilters.isNotEmpty()){
            val dataInizio = _currDateFilters.substringBefore('|').trim()
            val dataFine = _currDateFilters.substringAfter('|').trim()
            if(dataFine.isNotEmpty()){
                viewModelScope.launch {
                    //_bpList.value = myBPRepository.getItemsByDates(dataInizio, dataFine)
                    _bpList.value = myBPRepository.getItemsByDates(dataInizio, dataFine)
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
            _bpList.value = myBPRepository.getItemsToday(_currDate)
        }
    }

    fun refreshWithFilters(dates:String){
        _currDateFilters = dates
        refreshList()
    }

}

class BloodPressureViewModelFactory(
    private val myBPRepository: MyBloodPressureRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BloodPressureViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BloodPressureViewModel(
                myBPRepository = myBPRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
