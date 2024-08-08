package it.unibo.alessiociarrocchi.tesiahc.presentation.screen.bloodpressure

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.unibo.alessiociarrocchi.tesiahc.data.db.MyBloodPressureEntity
import it.unibo.alessiociarrocchi.tesiahc.presentation.component.BloodPressureRow
import it.unibo.alessiociarrocchi.tesiahc.presentation.component.DateTimeRangeDialogPicker
import kotlinx.coroutines.CoroutineScope
import java.util.Date


@Composable
fun BloodPressureScreen(
    locList: List<MyBloodPressureEntity>,
    onDetailsClick: (Int) -> Unit = {},
    onConfirmFilters: (String) -> Unit = {},
    onReloadPage: () -> Unit = {},
    applicationContext: android.content.Context,
    scaffoldState : ScaffoldState,
    scope: CoroutineScope
) {
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
            BloodPressureRow(
                item.time,
                item.timezone,
                item.systolic,
                item.diastolic,
                item.id,
                item.synced,
                onDetailsClick = { myid ->
                    onDetailsClick(myid)
                },
                onReloadPage,
                applicationContext,
                scaffoldState,
                scope
            )
        }
    }

}

