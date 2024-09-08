package it.unibo.alessiociarrocchi.tesiahc.presentation.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import it.unibo.alessiociarrocchi.tesiahc.data.MySettingsRepository
import it.unibo.alessiociarrocchi.tesiahc.data.db.GetKey_FilterDataFine
import it.unibo.alessiociarrocchi.tesiahc.data.db.GetKey_FilterDataInzio
import it.unibo.alessiociarrocchi.tesiahc.data.db.MySettingsEntity
import it.unibo.alessiociarrocchi.tesiahc.toDate
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.Executors

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun DateTimeRangeDialogPicker(
    dataInizio: Date = Date(),
    applicationContext: android.content.Context,
    onConfim: (String) -> Unit = {}
){
    val showDateRangePicker = remember { mutableStateOf(false) }
    val dateRangePickerState = rememberDateRangePickerState(yearRange = 2024..2024)
    val selectedDateRange = remember { mutableStateOf("") }

    //val sdfView = SimpleDateFormat("dd/MM/yyyy")
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    //selectedDateRange.value = sdfView.format(Date())

    if(showDateRangePicker.value){
        DatePickerDialog(
            onDismissRequest = {
                selectedDateRange.value =getRangeDates(dateRangePickerState, applicationContext)
                onConfim(selectedDateRange.value)
                showDateRangePicker.value = false
               },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedDateRange.value =getRangeDates(dateRangePickerState, applicationContext)
                        onConfim(selectedDateRange.value)
                        showDateRangePicker.value = false
                    }
                ) {
                    Text(text = "Conferma")
                }
            }) {

            DateRangePicker(state = dateRangePickerState, title = null, headline = {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Text(
                        text = dateRangePickerState.selectedStartDateMillis.toDate()
                            .ifEmpty { "Data di inizio" },
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = " - ",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = dateRangePickerState.selectedEndDateMillis.toDate()
                            .ifEmpty { "Data di fine" },
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            })
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .height(50.dp)
            .background(Color.Yellow),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){
        var dataView = sdf.format(dataInizio)

        val settRepository = MySettingsRepository.getInstance(
            applicationContext, Executors.newSingleThreadExecutor()
        )

        var dfi = settRepository.getItem(GetKey_FilterDataInzio())
        if (dfi != null){
            if(dfi.valore != ""){
                dataView = dfi.valore

                var dff = settRepository.getItem(GetKey_FilterDataFine())
                if (dff != null){
                    if(dff.valore != ""){
                        dataView += "|" + dff.valore
                    }
                }

            }
        }

        if(selectedDateRange.value != ""){
            dataView = selectedDateRange.value
        }

        Text(modifier = Modifier.combinedClickable(
            onClick = {
                showDateRangePicker.value=true
            },
            onLongClick = {

            }),
            text = dataView, style = MaterialTheme.typography.bodyLarge)
    }

}



@OptIn(ExperimentalMaterial3Api::class)
fun getRangeDates(dateRangePickerState : DateRangePickerState, applicationContext: android.content.Context) : String{
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    var stringDFI = sdf.format(Date())
    var stringDFF = ""
    try {
        stringDFI = sdf.format(dateRangePickerState.selectedStartDateMillis)
    }
    catch (e: Exception){

    }
    try {
        stringDFF = sdf.format(dateRangePickerState.selectedEndDateMillis)
    }
    catch (e: Exception){

    }

    onFilterCommand(applicationContext, stringDFI, stringDFF)

    return stringDFI + " | " +stringDFF
}

fun onFilterCommand(applicationContext: android.content.Context, dataI : String, dataF : String){
    val settRepository = MySettingsRepository.getInstance(
        applicationContext, Executors.newSingleThreadExecutor()
    )

    // data inizio
    val stringFDI = GetKey_FilterDataInzio()
    var doUpdate = false
    if(settRepository.getItem(stringFDI) != null){
        doUpdate = true
    }
    val myItemI = MySettingsEntity(
        chiave = stringFDI,
        valore = dataI,
    )
    if (doUpdate){
        settRepository.updateItem(myItemI)
    }
    else{
        settRepository.addItem(myItemI)
    }

    // data fine
    val stringFDF = GetKey_FilterDataFine()
    doUpdate = false
    if(settRepository.getItem(stringFDF) != null){
        doUpdate = true
    }
    val myItemF = MySettingsEntity(
        chiave = stringFDF,
        valore = dataF,
    )
    if (doUpdate){
        settRepository.updateItem(myItemF)
    }
    else{
        settRepository.addItem(myItemF)
    }

}

