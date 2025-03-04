package it.unibo.alessiociarrocchi.tesiahc.presentation.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.health.connect.client.records.BloodPressureRecord
import it.unibo.alessiociarrocchi.tesiahc.R
import it.unibo.alessiociarrocchi.tesiahc.convertToLocalDateViaMillisecond
import it.unibo.alessiociarrocchi.tesiahc.fromHex


/**
 * Creates a row to represent an [BloodPressureRecord]
 */
@Composable
fun BloodPressureRow(
    time: Long,
    zoneOffset: Int,
    systolic: Double,
    diastolic: Double,
    idBloodPressure: Int,
    onDetailsClick: (Int) -> Unit = {},

    ) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onDetailsClick(idBloodPressure)
                }
        ) {
            Spacer(modifier = Modifier.height(2.dp))
            Canvas(modifier = Modifier.fillMaxWidth()) {
                drawLine(
                    color = Color.fromHex("#d0d0d0"),
                    start = Offset(x = 0f, y = 0f),
                    end = Offset(x = size.width, y = size.height),
                    strokeWidth = 5f
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                color = MaterialTheme.colors.primary,
                text = convertToLocalDateViaMillisecond(time, zoneOffset).toString(),
                style = MaterialTheme.typography.caption
            )
            Text(stringResource(id = R.string.bp_systolic) + ": " + systolic.toString() + " mmHg")
            Text(stringResource(id = R.string.bp_diastolic) + ": " + diastolic.toString() + " mmHg")

            //BloodPressureSynced(synced, myBP_id, onReloadPage, applicationContext, scaffoldState, scope)
        }
    }
}