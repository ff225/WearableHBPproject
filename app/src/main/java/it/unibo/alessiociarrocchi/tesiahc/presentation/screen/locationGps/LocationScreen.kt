package it.unibo.alessiociarrocchi.tesiahc.presentation.screen.locationGps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import it.unibo.alessiociarrocchi.tesiahc.data.db.MyLocationEntity
import it.unibo.alessiociarrocchi.tesiahc.presentation.component.DateTimeRangeDialogPicker
import it.unibo.alessiociarrocchi.tesiahc.presentation.component.LocationRow
import java.util.Date

@Composable
fun LocationScreen(
    locList: List<MyLocationEntity>,
    onLongClick: (Int) -> Unit = {},
    onConfirmFilters: (String) -> Unit = {},
){
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item(){
            DateTimeRangeDialogPicker(
                dataInizio = Date(),
                onConfim = { dates ->
                    onConfirmFilters(dates)
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
        items(locList) { item ->
            LocationRow(
                item.id,
                item.mydate,
                item.latitude,
                item.longitude,
                onLongClik = { uid ->
                    onLongClick(uid)
                },
            )
        }
    }
}
