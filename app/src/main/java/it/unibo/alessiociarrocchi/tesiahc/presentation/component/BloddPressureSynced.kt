package it.unibo.alessiociarrocchi.tesiahc.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import it.unibo.alessiociarrocchi.tesiahc.R

@Composable
fun BloodPressureSynced(
    synced: Int
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
            var bpSynced = "Misurazione da sincronizzare con firestore"
            if(synced == 1){
                img_bpSynced = R.drawable.synced
                bpSynced = "Misurazione sincronizzata con firestore"
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

    }
}