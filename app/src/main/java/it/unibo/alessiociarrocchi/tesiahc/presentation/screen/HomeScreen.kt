package it.unibo.alessiociarrocchi.tesiahc.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest
import it.unibo.alessiociarrocchi.tesiahc.presentation.RouteDestination
import it.unibo.alessiociarrocchi.tesiahc.worker.GetDataFromHC
import it.unibo.alessiociarrocchi.tesiahc.worker.GetLatestLocation
import it.unibo.alessiociarrocchi.tesiahc.worker.SendDataToFirebase
import java.util.concurrent.TimeUnit

object HomeScreen : RouteDestination {
    override val route: String = "home_screen"
    override val title: String = "Home"
}

@Composable
fun HomeScreen(navController: NavController? = null) {
    val context = LocalContext.current

    val periodicGetDataFromHC: PeriodicWorkRequest =
        PeriodicWorkRequest.Builder(GetDataFromHC::class.java, 30, TimeUnit.MINUTES)
            .addTag("PeriodicGetDataFromHC")
            .build()

    val periodicGetCurrentLocationRequest: PeriodicWorkRequest =
        PeriodicWorkRequest.Builder(GetLatestLocation::class.java, 15, TimeUnit.MINUTES)
            .addTag("PeriodicGetCurrentLocation")
            .build()

    val periodicSendDataToFirebase: PeriodicWorkRequest =
        PeriodicWorkRequest.Builder(SendDataToFirebase::class.java, 1, TimeUnit.HOURS)
            .addTag("PeriodicSendDataToFirebase")
            .build()

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "getDataFromHC",
        ExistingPeriodicWorkPolicy.KEEP,
        periodicGetDataFromHC
    )

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "getCurrentLocation",
        ExistingPeriodicWorkPolicy.KEEP,
        periodicGetCurrentLocationRequest
    )

    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
        "sendDataToFirebase",
        ExistingPeriodicWorkPolicy.KEEP,
        periodicSendDataToFirebase
    )


    MyScaffold(
        title = HomeScreen.title,
        navController = navController
    ) {

        Column(
            Modifier
                .padding(it)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(onClick = {

                val getDataFromHC: WorkRequest =
                    OneTimeWorkRequest.Builder(GetDataFromHC::class.java)
                        .addTag("getDataFromHC")
                        .build()

                val getCurrentLocationRequest: WorkRequest =
                    OneTimeWorkRequest.Builder(GetLatestLocation::class.java)
                        .addTag("getCurrentLocation")
                        .build()

                val sendDataToFirebase: WorkRequest =
                    OneTimeWorkRequest.Builder(SendDataToFirebase::class.java)
                        .addTag("sendDataToFirebase")
                        .build()

                WorkManager.getInstance(context).enqueue(
                    listOf(getDataFromHC, getCurrentLocationRequest, sendDataToFirebase)
                )

            }) {
                Text("Get data from HealthConnect")
            }
        }
    }
}