package it.unibo.alessiociarrocchi.tesiahc.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import it.unibo.alessiociarrocchi.tesiahc.R
import it.unibo.alessiociarrocchi.tesiahc.convertToLocalDateViaMillisecond
import it.unibo.alessiociarrocchi.tesiahc.data.model.BloodPressureEntity

@Composable
fun BloodPressureDetail(
    bpDetail: BloodPressureEntity,
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
                color = MaterialTheme.colors.primary,
                text = "Id: " + bpDetail.id,
                style = MaterialTheme.typography.caption
            )
            Text(
                color = MaterialTheme.colors.primary,
                text = "Health Connect id: " + bpDetail.uid,
                style = MaterialTheme.typography.caption
            )
            //BloodPressureSynced(bpDetail.synced, bpDetail.id, onReloadPage, applicationContext, scaffoldState, scope)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                color = MaterialTheme.colors.secondary,
                text = convertToLocalDateViaMillisecond(bpDetail.time, bpDetail.timezone).toString()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(stringResource(id = R.string.bp_systolic) + ": " + bpDetail.systolic.toString() + " mmHg")
            Text(stringResource(id = R.string.bp_diastolic) + ": " + bpDetail.diastolic.toString() + " mmHg")
            Text(
                stringResource(id = R.string.bp_posizione_misurazione) + ": " + stringArrayResource(
                    id = R.array.bp_posizioni_misurazione
                )[bpDetail.measurementLocation]
            )
            Text(stringResource(id = R.string.bp_posizione_corpo) + ": " + stringArrayResource(id = R.array.bp_posizioni_corpo)[bpDetail.bodyPosition])
        }
    }
}