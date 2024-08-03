package it.unibo.alessiociarrocchi.tesiahc.presentation.component

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
import androidx.compose.ui.unit.dp
import it.unibo.alessiociarrocchi.tesiahc.R
import it.unibo.alessiociarrocchi.tesiahc.convertToLocalDateViaMilisecond
import java.util.Date


@Composable
fun BloodPressureDetail(
  id: Int,
  uid: String,
  systolic: Double,
  diastolic: Double,
  time: Date,
  zoneOffset: Int,
  latitudine: Double,
  longitudine: Double
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
        text = "Id: " + id,
        style = MaterialTheme.typography.caption
      )
      Text(
        color = MaterialTheme.colors.primary,
        text = "Health Connect id: " + uid,
        style = MaterialTheme.typography.caption
      )
      Text(
        color = MaterialTheme.colors.secondary,
        text = convertToLocalDateViaMilisecond(time, zoneOffset).toString(),
        style = MaterialTheme.typography.h6
      )
      Text(stringResource(id = R.string.bp_systolic) + ": " + systolic.toString() + " mmHg")
      Text(stringResource(id = R.string.bp_diastolic) + ": " + diastolic.toString() + " mmHg")
      //Text(stringResource(id = R.string.bp_posizione_misurazione) + ": " + stringArrayResource(id=R.array.bp_posizioni_misurazione)[measurementLocation])
      //Text(stringResource(id = R.string.bp_posizione_corpo) + ": " + stringArrayResource(id=R.array.bp_posizioni_corpo)[bodyPosition])
      Text(stringResource(id = R.string.bp_latitudine) + ": " + latitudine)
      Text(stringResource(id = R.string.bp_longitudine) + ": " + longitudine)
    }
  }
}