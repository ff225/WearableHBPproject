
package it.unibo.alessiociarrocchi.tesiahc.presentation.screen.bloodpressuredetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import it.unibo.alessiociarrocchi.tesiahc.R
import it.unibo.alessiociarrocchi.tesiahc.data.db.MyBloodPressureEntity
import it.unibo.alessiociarrocchi.tesiahc.data.db.MyHeartRateAggregateEntity
import it.unibo.alessiociarrocchi.tesiahc.presentation.component.BloodPressureDetail
import it.unibo.alessiociarrocchi.tesiahc.presentation.component.BloodPressureDetail_HeartRate
import it.unibo.alessiociarrocchi.tesiahc.showInfoSnackbar
import it.unibo.alessiociarrocchi.tesiahc.updateBloodPressureDesc
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@Composable
fun BloodPressureDetailScreen(
  myBP: MyBloodPressureEntity?,
  hrAggregate: MyHeartRateAggregateEntity?,
  applicationContext: android.content.Context,
  scaffoldState : ScaffoldState,
) {
  var textDesc by rememberSaveable { mutableStateOf("") }
  val scope = rememberCoroutineScope()

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .fillMaxHeight()
      .padding(horizontal = 8.dp, vertical = 8.dp)
  ){
    Column(){
      Text(
        color = MaterialTheme.colors.primaryVariant,
        text = stringResource(id = R.string.bp_detail_title),
        style = MaterialTheme.typography.h5
      )
      if(myBP != null){
        textDesc = myBP.description
        BloodPressureDetail(
          myBP
        )
      }

      Text(
        color = MaterialTheme.colors.primaryVariant,
        text = stringResource(id = R.string.bp_detail_hr_title),
        style = MaterialTheme.typography.h6
      )
      BloodPressureDetail_HeartRate(
        hrAggregate
      )

      // campo descrizione
      if (myBP != null){
        Spacer(modifier = Modifier.height(24.dp))
        TextField(
          modifier = Modifier.fillMaxWidth(),
          value = textDesc,
          onValueChange = {
            textDesc = it
          },
          placeholder = { Text(text = "Descrivi dove ha svolto la misurazione") },
          label = { Text("Descrizione misurazione") },
          maxLines = 5
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
          modifier = Modifier
            .fillMaxWidth(),
          horizontalArrangement = Arrangement.Center
        ){
          Button(
            onClick = {
              runBlocking {
                launch {
                  updateBloodPressureDesc(myBP.id, textDesc, applicationContext)

                }
              }
              showInfoSnackbar(scaffoldState, scope, "Info salvate correttamente")

            }) {
            Text(text = "Salva descrizione")
          }
        }
      }

    }
  }
}