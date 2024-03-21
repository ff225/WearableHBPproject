package it.unibo.alessiociarrocchi.tesiahc.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import it.unibo.alessiociarrocchi.tesiahc.R
import it.unibo.alessiociarrocchi.tesiahc.data.HeartRateAggregateData
import it.unibo.alessiociarrocchi.tesiahc.formatInstantToDateTime

/**
 * Displays content of heart rate measurements
 */
@Composable
fun BloodPressureDetail_HeartRate(
  hrAggregate: HeartRateAggregateData,
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 8.dp, vertical = 8.dp),
    verticalAlignment = Alignment.Top,
    horizontalArrangement = Arrangement.Start
  ) {
    Column() {
      Text(
        color = MaterialTheme.colors.primaryVariant,
        text = stringResource(id = R.string.bp_detail_hr_title),
        style = MaterialTheme.typography.h6
      )
      Text(stringResource(id = R.string.bp_detail_hr_start) + ": " + formatInstantToDateTime(hrAggregate.hrStart))
      Text(stringResource(id = R.string.bp_detail_hr_end) + ": " + formatInstantToDateTime(hrAggregate.hrEnd))
      Text(stringResource(id = R.string.bp_detail_hr_bpm_avg) + ": " + hrAggregate.hrAVG.toString() + " bpm")
      Text(stringResource(id = R.string.bp_detail_hr_bpm_max) + ": " + hrAggregate.hrMAX.toString() + " bpm")
      Text(stringResource(id = R.string.bp_detail_hr_bpm_min) + ": " + hrAggregate.hrMIN.toString() + " bpm")
      Text(stringResource(id = R.string.bp_detail_hr_meas_count) + ": " + hrAggregate.hrMC.toString())
    }
  }
}
