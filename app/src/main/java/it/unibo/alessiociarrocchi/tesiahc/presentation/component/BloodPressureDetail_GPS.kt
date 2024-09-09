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


@Composable
fun BloodPressureDetail_GPS(
  latitude: Double,
  longitude: Double
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 8.dp, vertical = 8.dp),
    verticalAlignment = Alignment.Top,
    horizontalArrangement = Arrangement.Start
  ) {
    Column() {
      if(latitude != 0.00 && longitude != 0.00){
        Text(stringResource(id = R.string.bp_latitudine) + ": " + latitude)
        Text(stringResource(id = R.string.bp_longitudine) + ": " + longitude)
      }
      else{
        Text("Nessun dato")
      }
    }
  }
}
