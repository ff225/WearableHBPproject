package it.unibo.alessiociarrocchi.tesiahc.presentation.screen.bloodpressuredetail

import android.os.RemoteException
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.BloodPressureRecord
import androidx.health.connect.client.records.HeartRateRecord
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import it.unibo.alessiociarrocchi.tesiahc.data.HeartRateAggregateData
import java.io.IOException
import java.util.UUID
import kotlinx.coroutines.launch
import java.time.temporal.ChronoUnit

class BloodPressureDetailViewModel(
  private val uid: String,
  private val healthConnectManager: it.unibo.alessiociarrocchi.tesiahc.data.HealthConnectManager,
) : ViewModel() {
  val permissions = setOf(
    HealthPermission.getReadPermission(BloodPressureRecord::class),
    HealthPermission.getReadPermission(HeartRateRecord::class)
  )

  var permissionsGranted = mutableStateOf(false)
    private set

  var bpDetail: MutableState<BloodPressureRecord?> = mutableStateOf(null)
    private set

  var hrAggregate: MutableState<HeartRateAggregateData?> = mutableStateOf(null)
    private set

  //var hrList: MutableState<List<HeartRateRecord>> = mutableStateOf(listOf())
  //  private set

  var uiState: UiState by mutableStateOf(UiState.Uninitialized)
    private set

  val permissionsLauncher = healthConnectManager.requestPermissionsActivityContract()

  fun initialLoad() {
    readDetailsData()
  }

  private fun readDetailsData() {
    viewModelScope.launch {
      tryWithPermissionsCheck {
        bpDetail.value = healthConnectManager.readSpecificBloodPressure(uid)

        val hrStart = bpDetail.value!!.time.minus(30, ChronoUnit.MINUTES)
        val hrEnd = bpDetail.value!!.time
        //hrList.value = healthConnectManager.readHeartRateRecordList(periodStart, periodEnd).reversed()
        val hrAVG = healthConnectManager.aggregateBPM_AVG(hrStart, hrEnd)
        val hrMIN = healthConnectManager.aggregateBPM_Min(hrStart, hrEnd)
        val hrMAX = healthConnectManager.aggregateBPM_Max(hrStart, hrEnd)
        val hrMC = healthConnectManager.aggregateMeasurements_Count(hrStart, hrEnd)
        hrAggregate.value = HeartRateAggregateData(
          hrStart, hrEnd, hrAVG, hrMIN, hrMAX, hrMC
        )
      }
    }
  }

  /**
   * Provides permission check and error handling for Health Connect suspend function calls.
   *
   * Permissions are checked prior to execution of [block], and if all permissions aren't granted
   * the [block] won't be executed, and [permissionsGranted] will be set to false, which will
   * result in the UI showing the permissions button.
   *
   * Where an error is caught, of the type Health Connect is known to throw, [uiState] is set to
   * [UiState.Error], which results in the snackbar being used to show the error message.
   */
  private suspend fun tryWithPermissionsCheck(block: suspend () -> Unit) {
    permissionsGranted.value = healthConnectManager.hasAllPermissions(permissions)
    uiState = try {
      if (permissionsGranted.value) {
        block()
      }
      UiState.Done
    } catch (remoteException: RemoteException) {
      UiState.Error(remoteException)
    } catch (securityException: SecurityException) {
      UiState.Error(securityException)
    } catch (ioException: IOException) {
      UiState.Error(ioException)
    } catch (illegalStateException: IllegalStateException) {
      UiState.Error(illegalStateException)
    }
  }

  sealed class UiState {
    object Uninitialized : UiState()
    object Done : UiState()

    // A random UUID is used in each Error object to allow errors to be uniquely identified,
    // and recomposition won't result in multiple snackbars.
    data class Error(val exception: Throwable, val uuid: UUID = UUID.randomUUID()) : UiState()
  }
}

class BloodPressureDetailViewModelFactory(
  private val uid: String,
  private val healthConnectManager: it.unibo.alessiociarrocchi.tesiahc.data.HealthConnectManager,
) : ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(BloodPressureDetailViewModel::class.java)) {
      @Suppress("UNCHECKED_CAST")
      return BloodPressureDetailViewModel(
        uid = uid,
        healthConnectManager = healthConnectManager
      ) as T
    }
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}
