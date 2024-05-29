package it.unibo.alessiociarrocchi.tesiahc.presentation.screen.locationGps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import it.unibo.alessiociarrocchi.tesiahc.data.db.MyLocationEntity
import it.unibo.alessiociarrocchi.tesiahc.presentation.component.LocationRow

@Composable
fun LocationScreen(
    locList: List<MyLocationEntity>,
    onLongClik: (Int) -> Unit = {},
){
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(locList) { item ->
            LocationRow(
                item.id,
                item.date,
                item.latitude,
                item.longitude,
                onLongClik = { uid ->
                    onLongClik(uid)
                },
            )
        }
    }
}