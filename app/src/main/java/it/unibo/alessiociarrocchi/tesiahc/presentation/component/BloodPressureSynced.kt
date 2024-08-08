package it.unibo.alessiociarrocchi.tesiahc.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import it.unibo.alessiociarrocchi.tesiahc.R
import it.unibo.alessiociarrocchi.tesiahc.sendSingleHealthData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@Composable
fun BloodPressureSynced(
    synced: Int,
    myBP_id: Int,
    onReloadPage: () -> Unit = {},
    applicationContext: android.content.Context,
    scaffoldState : ScaffoldState,
    scope: CoroutineScope
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ){
        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ){

            var img_bpSynced = R.drawable.not_synced
            var bpSynced = "Misurazione da sincronizzare"
            if(synced == 1){
                img_bpSynced = R.drawable.synced
                bpSynced = "Misurazione sincronizzata"
            }


            Image(
                painter = painterResource(id = img_bpSynced),
                contentDescription = bpSynced,
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = bpSynced,
                style = MaterialTheme.typography.caption
            )
        }

        if (synced == 0 && myBP_id > 0){
            var reloadPage by remember { mutableStateOf(false) }

            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Button(
                    onClick = {
                        runBlocking {
                            launch {
                                reloadPage = sendSingleHealthData(myBP_id, applicationContext, scaffoldState, scope, true)
                            }
                        }
                        if(reloadPage){
                            onReloadPage()
                        }
                    }) {
                    Text(text = "Forza invio")
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
        }

    }
}