package it.unibo.alessiociarrocchi.tesiahc.presentation.screen.locationGps
/*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

import it.unibo.alessiociarrocchi.tesiahc.data.db.MyLocationEntity
import it.unibo.alessiociarrocchi.tesiahc.presentation.component.DateTimeRangeDialogPicker
import it.unibo.alessiociarrocchi.tesiahc.presentation.component.LocationRow
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
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
*/