package it.unibo.alessiociarrocchi.tesiahc.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.health.connect.client.units.Pressure
import it.unibo.alessiociarrocchi.tesiahc.R
import it.unibo.alessiociarrocchi.tesiahc.data.BodyPositionType
import it.unibo.alessiociarrocchi.tesiahc.data.MeasurementLocationType
import it.unibo.alessiociarrocchi.tesiahc.presentation.theme.HealthConnectTheme
import java.time.Instant
import java.time.ZoneOffset
import java.util.UUID

/**
 * Displays content of blood pressure measurement
 */
@Composable
fun BloodPressureDetail(
  uid: String,
  systolic: Pressure,
  diastolic: Pressure,
  time: Instant,
  zoneoffset: ZoneOffset?,
  //bodyPosition: Int,
  //measurementLocation: Int,
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
        text = stringResource(id = R.string.bp_detail_title),
        style = MaterialTheme.typography.h5
      )
      Text(
        color = MaterialTheme.colors.primary,
        text = uid,
        style = MaterialTheme.typography.caption
      )
      Text(
        color = MaterialTheme.colors.secondary,
        text = it.unibo.alessiociarrocchi.tesiahc.localDateTimeToString(it.unibo.alessiociarrocchi.tesiahc.convertLongToDate(time, zoneoffset!!.totalSeconds)),
        style = MaterialTheme.typography.caption
      )
      Text(stringResource(id = R.string.bp_systolic) + ": " + systolic.inMillimetersOfMercury.toString() + " mmHg")
      Text(stringResource(id = R.string.bp_diastolic) + ": " + diastolic.inMillimetersOfMercury.toString() + " mmHg")
      //Text(stringResource(id = R.string.bp_posizione_misurazione) + ": " + stringArrayResource(id=R.array.bp_posizioni_misurazione)[measurementLocation])
      //Text(stringResource(id = R.string.bp_posizione_corpo) + ": " + stringArrayResource(id=R.array.bp_posizioni_corpo)[bodyPosition])
    }
  }
}

@Preview
@Composable
fun BloodPressureDetailPreview() {
  HealthConnectTheme {
    BloodPressureDetail(
      UUID.randomUUID().toString(),
      Pressure.millimetersOfMercury(120.0),
      Pressure.millimetersOfMercury(80.0),
      Instant.now(),
      null,
      //BodyPositionType.BODY_POSITION_SITTING_DOWN.ordinal,
      //MeasurementLocationType.MEASUREMENT_LOCATION_LEFT_WRIST.ordinal,
    )
  }
}