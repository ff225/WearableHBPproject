package it.unibo.alessiociarrocchi.tesiahc.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import it.unibo.alessiociarrocchi.tesiahc.R
import it.unibo.alessiociarrocchi.tesiahc.convertToLocalDateViaMillisecond
import it.unibo.alessiociarrocchi.tesiahc.data.model.HeartRateAggregateEntity


@Composable
fun BloodPressureDetail_HeartRate(
    hrAggregate: HeartRateAggregateEntity,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Start
    ) {
        Column {
            Text(
                stringResource(id = R.string.bp_detail_hr_start) + ": " + convertToLocalDateViaMillisecond(
                    hrAggregate.hrStart.time,
                    hrAggregate.timezone
                ).toString()
            )
            Text(
                stringResource(id = R.string.bp_detail_hr_end) + ": " + convertToLocalDateViaMillisecond(
                    hrAggregate.hrEnd.time,
                    hrAggregate.timezone
                ).toString()
            )
            Text(stringResource(id = R.string.bp_detail_hr_bpm_avg) + ": " + hrAggregate.hrAVG.toString() + " bpm")
            Text(stringResource(id = R.string.bp_detail_hr_bpm_max) + ": " + hrAggregate.hrMAX.toString() + " bpm")
            Text(stringResource(id = R.string.bp_detail_hr_bpm_min) + ": " + hrAggregate.hrMIN.toString() + " bpm")
            Text(stringResource(id = R.string.bp_detail_hr_meas_count) + ": " + hrAggregate.hrMC.toString())

        }
    }
}
