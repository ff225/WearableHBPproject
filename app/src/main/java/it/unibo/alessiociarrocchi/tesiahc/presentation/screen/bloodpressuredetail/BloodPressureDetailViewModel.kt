package it.unibo.alessiociarrocchi.tesiahc.presentation.screen.bloodpressuredetail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.unibo.alessiociarrocchi.tesiahc.data.model.BloodPressureEntity
import it.unibo.alessiociarrocchi.tesiahc.data.model.HeartRateAggregateEntity
import it.unibo.alessiociarrocchi.tesiahc.data.repository.BloodPressureRepository
import it.unibo.alessiociarrocchi.tesiahc.data.repository.HeartRateRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class BloodPressureDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val bloodPressureRepository: BloodPressureRepository,
    private val heartRateRepository: HeartRateRepository
) : ViewModel() {

    private val itemId: Int = checkNotNull(savedStateHandle[BloodPressureDetailScreen.itemIdArg])


    var bpDetail: MutableStateFlow<BloodPressureEntity?> = MutableStateFlow(null)
        private set

    var hrAggregate: MutableStateFlow<HeartRateAggregateEntity?> = MutableStateFlow(null)
        private set

    init {
        Log.d("BloodPressureDetailViewModel", "itemId: $itemId")

        viewModelScope.launch {
            bpDetail.value = bloodPressureRepository.getItem(itemId)
            hrAggregate.value = heartRateRepository.getItemByExternalId(bpDetail.value!!.uid)
            Log.d("BloodPressureDetailViewModel", "bpDetail.value: ${bpDetail.value}")
            Log.d("BloodPressureDetailViewModel", "hrAggregate.value: ${hrAggregate.value}")
        }
    }

    fun updateDescription(textDesc: String) {
        viewModelScope.launch {
            bpDetail.value?.let {
                val updated = it.copy(description = textDesc)
                bloodPressureRepository.updateItem(updated)
            }
        }
    }
}