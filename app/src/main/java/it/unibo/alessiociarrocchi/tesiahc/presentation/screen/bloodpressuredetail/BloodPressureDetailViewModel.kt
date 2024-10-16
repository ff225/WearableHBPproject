package it.unibo.alessiociarrocchi.tesiahc.presentation.screen.bloodpressuredetail

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import it.unibo.alessiociarrocchi.tesiahc.data.MyBloodPressureRepository
import it.unibo.alessiociarrocchi.tesiahc.data.MyHeartRateRepository
import it.unibo.alessiociarrocchi.tesiahc.data.db.MyBloodPressureEntity
import it.unibo.alessiociarrocchi.tesiahc.data.db.MyHeartRateAggregateEntity
import kotlinx.coroutines.launch

class BloodPressureDetailViewModel(
  private val uid: String,
  private val myBPRepository: MyBloodPressureRepository,
  private val myHRRepository: MyHeartRateRepository
) : ViewModel() {

  var bpDetail: MutableState<MyBloodPressureEntity?> = mutableStateOf(null)
    private set

  var hrAggregate: MutableState<MyHeartRateAggregateEntity?> = mutableStateOf(null)
    private set

  fun initialLoad() {
    readDetailsData()
  }

  private fun readDetailsData() {
    viewModelScope.launch {

      bpDetail.value = myBPRepository.getItem(uid.toInt())
      if (bpDetail != null){
        if(bpDetail.value != null){
          hrAggregate.value = myHRRepository.getItemByExternalId(bpDetail.value!!.id)
          //app.value = myHRRepository.getItems()
        }
      }


    }
  }

}

class BloodPressureDetailViewModelFactory(
  private val uid: String,
  private val myBPRepository: MyBloodPressureRepository,
  private val myHRRepository: MyHeartRateRepository
) : ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(BloodPressureDetailViewModel::class.java)) {
      @Suppress("UNCHECKED_CAST")
      return BloodPressureDetailViewModel(
        uid = uid,
        myBPRepository = myBPRepository,
        myHRRepository = myHRRepository
      ) as T
    }
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}
