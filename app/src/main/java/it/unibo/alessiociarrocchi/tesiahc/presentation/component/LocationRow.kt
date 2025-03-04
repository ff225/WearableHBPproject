package it.unibo.alessiociarrocchi.tesiahc.presentation.component

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.unibo.alessiociarrocchi.tesiahc.R
import it.unibo.alessiociarrocchi.tesiahc.convertToLocalDateViaMillisecond
import java.time.ZoneId
import java.util.TimeZone

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LocationRow(
    uid: Int,
    date: Long,
    latitude: Double,
    longitude: Double,
    onLongClick: (Int) -> Unit = {},
) {
    val showAlertMessageLongClick = remember { mutableStateOf(false) }

    if (showAlertMessageLongClick.value) {
        AlertDialog(
            onDismissRequest = {
                showAlertMessageLongClick.value = false
            },
            title = {
                Text(
                    text = "Eliminazione riga",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            },
            text = {
                Text(
                    "Vuoi cancellare questa riga?",
                    fontSize = 16.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        //Log.d("Delete dialog", "Eccomi con id:" + uid.toString())
                        onLongClick(uid)
                        showAlertMessageLongClick.value = false
                    }) {
                    Text("Sì", style = TextStyle(color = Color.Black))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showAlertMessageLongClick.value = false
                    }) {
                    Text("No", style = TextStyle(color = Color.Black))
                }
            },
        )
    }

    Row(
        modifier = Modifier
            .combinedClickable(
                onClick = {},
                onLongClick = {
                    showAlertMessageLongClick.value = true
                })
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Log.d("LocationRow", "${TimeZone.getTimeZone(ZoneId.systemDefault()).rawOffset}")
            Text(
                color = MaterialTheme.colors.primary,
                text = convertToLocalDateViaMillisecond(
                    date,
                    TimeZone.getTimeZone(ZoneId.systemDefault()).rawOffset / 1000
                ).toString(),
                style = MaterialTheme.typography.caption
            )
            Text(stringResource(id = R.string.latitudine) + ": " + latitude.toString())
            Text(stringResource(id = R.string.longitudine) + ": " + longitude.toString())
        }
    }

}

