package it.unibo.alessiociarrocchi.tesiahc.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.health.connect.client.records.BloodPressureRecord
import androidx.health.connect.client.units.Pressure
import it.unibo.alessiociarrocchi.tesiahc.R
import it.unibo.alessiociarrocchi.tesiahc.presentation.theme.HealthConnectTheme
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.UUID


/**
 * Creates a row to represent an [BloodPressureRecord]
 */
@Composable
fun BloodPressureRow(
  time: Instant,
  zoneOffset: ZoneOffset?,
  systolic: Pressure,
  diastolic: Pressure,
  uid: String,
  onDetailsClick: (String) -> Unit = {},
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
          onDetailsClick(uid)
        }
    ) {
      /*Text(
        color = MaterialTheme.colors.primary,
        text = uid,
        style = MaterialTheme.typography.caption
      )*/
      Text(
        color = MaterialTheme.colors.primary,
        text = it.unibo.alessiociarrocchi.tesiahc.localDateTimeToString(it.unibo.alessiociarrocchi.tesiahc.convertLongToDate(time, zoneOffset!!.totalSeconds)),
        style = MaterialTheme.typography.caption
      )
      Text(stringResource(id=R.string.bp_systolic) + ": " + systolic.inMillimetersOfMercury.toString() + " mmHg")
      Text(stringResource(id=R.string.bp_diastolic) + ": " + diastolic.inMillimetersOfMercury.toString() + " mmHg")
    }
  }
}