
package it.unibo.alessiociarrocchi.tesiahc.presentation.screen.bloodpressuredetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.health.connect.client.records.BloodPressureRecord
import it.unibo.alessiociarrocchi.tesiahc.R
import it.unibo.alessiociarrocchi.tesiahc.data.db.MyHeartRateAggregateEntity
import java.util.UUID
import it.unibo.alessiociarrocchi.tesiahc.presentation.component.BloodPressureDetail
import it.unibo.alessiociarrocchi.tesiahc.presentation.component.BloodPressureDetail_HeartRate

/**
 * Shows a details of a given [BloodPressureRecord]
 */
@Composable
fun BloodPressureDetailScreen(
  permissions: Set<String>,
  permissionsGranted: Boolean,
  myBP: BloodPressureRecord?,
  hrAggregate: MyHeartRateAggregateEntity?,
  uiState: BloodPressureDetailViewModel.UiState,
  onError: (Throwable?) -> Unit = {},
  onPermissionsResult: () -> Unit = {},
  onPermissionsLaunch: (Set<String>) -> Unit = {},
) {

  // Remember the last error ID, such that it is possible to avoid re-launching the error
  // notification for the same error when the screen is recomposed, or configuration changes etc.
  val errorId = rememberSaveable { mutableStateOf(UUID.randomUUID()) }

  LaunchedEffect(uiState) {
    // If the initial data load has not taken place, attempt to load the data.
    if (uiState is BloodPressureDetailViewModel.UiState.Uninitialized) {
      onPermissionsResult()
    }

    // The [BloodPressureDetailViewModel.UiState] provides details of whether the last action
    // was a success or resulted in an error. Where an error occurred, for example in reading
    // and writing to Health Connect, the user is notified, and where the error is one that can
    // be recovered from, an attempt to do so is made.
    if (uiState is BloodPressureDetailViewModel.UiState.Error &&
      errorId.value != uiState.uuid
    ) {
      onError(uiState.exception)
      errorId.value = uiState.uuid
    }
  }

  if (uiState != BloodPressureDetailViewModel.UiState.Uninitialized) {
    Column(
      modifier = Modifier.fillMaxSize(),
      verticalArrangement = Arrangement.Top,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      if (!permissionsGranted) {
        Button(
          onClick = { onPermissionsLaunch(permissions) }
        ) {
          Text(text = stringResource(R.string.permissions_button_label))
        }
      } else {
        BloodPressureDetail(
          myBP!!.metadata.id,
          myBP.systolic,
          myBP.diastolic,
          myBP.time,
          myBP.zoneOffset,
          //myBP.bodyPosition,
          //myBP.measurementLocation,
        )
        BloodPressureDetail_HeartRate(
          hrAggregate!!
        )
      }
    }
  }
}