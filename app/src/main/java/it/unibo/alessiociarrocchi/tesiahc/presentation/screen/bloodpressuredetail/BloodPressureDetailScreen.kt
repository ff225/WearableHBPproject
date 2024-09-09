
package it.unibo.alessiociarrocchi.tesiahc.presentation.screen.bloodpressuredetail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import it.unibo.alessiociarrocchi.tesiahc.R
import it.unibo.alessiociarrocchi.tesiahc.data.db.MyBloodPressureEntity
import it.unibo.alessiociarrocchi.tesiahc.data.db.MyHeartRateAggregateEntity
import it.unibo.alessiociarrocchi.tesiahc.data.updateBloodPressureDesc
import it.unibo.alessiociarrocchi.tesiahc.presentation.component.BloodPressureDetail
import it.unibo.alessiociarrocchi.tesiahc.presentation.component.BloodPressureDetail_GPS
import it.unibo.alessiociarrocchi.tesiahc.presentation.component.BloodPressureDetail_HeartRate
import it.unibo.alessiociarrocchi.tesiahc.sendSingleHealthData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BloodPressureDetailScreen(
  myBP: MyBloodPressureEntity?,
  hrAggregate: MyHeartRateAggregateEntity?,
  onGoBack: () -> Unit = {},
  onReloadPage: () -> Unit = {},
  applicationContext: android.content.Context,
  scaffoldState : ScaffoldState,
  scope: CoroutineScope
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .fillMaxHeight()
      .padding(horizontal = 10.dp, vertical = 16.dp)
      .verticalScroll(rememberScrollState())
  ){
    Column(){

      // bottone indietro
      Row(
        modifier = Modifier
          .combinedClickable(
            onClick = {
              onGoBack()
            }
          ),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
      ){
        Image(
          painter = painterResource(id = R.drawable.arrow_back),
          contentDescription = "Indietro",
          contentScale = ContentScale.Fit,
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
          text = "Torna indietro",
        )
      }
      Spacer(modifier = Modifier.height(8.dp))


      if(myBP != null) {
        // dati pressione
        Text(
          color = MaterialTheme.colors.primaryVariant,
          text = stringResource(id = R.string.bp_detail_title),
          style = MaterialTheme.typography.h5
        )
        BloodPressureDetail(
          myBP,
          onReloadPage,
          applicationContext,
          scaffoldState,
          scope
        )

        // dati localizzazione gps
        Text(
          color = MaterialTheme.colors.primaryVariant,
          text = "Localizzazione GPS",
          style = MaterialTheme.typography.h6
        )
        BloodPressureDetail_GPS(
          myBP.latitude,
          myBP.longitude
        )

        // dati cuore
        Text(
          color = MaterialTheme.colors.primaryVariant,
          text = stringResource(id = R.string.bp_detail_hr_title),
          style = MaterialTheme.typography.h6
        )
        BloodPressureDetail_HeartRate(
          hrAggregate
        )

        // campo descrizione
        var textDesc by remember { mutableStateOf(myBP!!.description) }
        val focusManager = LocalFocusManager.current
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
          modifier = Modifier.fillMaxWidth(),
          maxLines = 5,
          value = textDesc,
          onValueChange = { newText ->
            textDesc = newText
          },
          label = { Text(text = "Descrizione misurazione") },
          placeholder = { Text(text = "Descrivi brevemente dove è stata svolta la misurazione e cosa si stava facendo poco prima") },
          keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
          keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
          modifier = Modifier
            .fillMaxWidth(),
          horizontalArrangement = Arrangement.Center
        ) {
          Button(
            onClick = {
              runBlocking {
                launch {
                  updateBloodPressureDesc(
                    myBP.id,
                    textDesc,
                    applicationContext,
                    scaffoldState,
                    scope
                  )
                }
              }

              runBlocking {
                launch {
                  sendSingleHealthData(myBP.id, applicationContext, scaffoldState, scope, false)
                }
              }

              onReloadPage()
            }) {
            Text(text = "Salva info")
          }
        }
      }
    }
  }
}