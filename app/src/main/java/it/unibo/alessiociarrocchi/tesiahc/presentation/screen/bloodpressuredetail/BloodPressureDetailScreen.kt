
package it.unibo.alessiociarrocchi.tesiahc.presentation.screen.bloodpressuredetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.unibo.alessiociarrocchi.tesiahc.data.db.MyBloodPressureEntity
import it.unibo.alessiociarrocchi.tesiahc.data.db.MyHeartRateAggregateEntity
import it.unibo.alessiociarrocchi.tesiahc.presentation.component.BloodPressureDetail
import it.unibo.alessiociarrocchi.tesiahc.presentation.component.BloodPressureDetail_HeartRate

@Composable
fun BloodPressureDetailScreen(
  myBP: MyBloodPressureEntity?,
  hrAggregate: MyHeartRateAggregateEntity?
) {

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .fillMaxHeight()
      .padding(horizontal = 8.dp, vertical = 8.dp)
  ){
    if(myBP != null){
      BloodPressureDetail(
        myBP!!.id,
        myBP.uid,
        myBP.systolic,
        myBP.diastolic,
        myBP.time,
        myBP.timezone,
        myBP.latitude,
        myBP.longitude
      )
    }

    BloodPressureDetail_HeartRate(
      hrAggregate
    )
  }

}