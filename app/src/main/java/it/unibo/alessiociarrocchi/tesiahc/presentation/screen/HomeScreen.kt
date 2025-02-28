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
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest
import it.unibo.alessiociarrocchi.tesiahc.presentation.RouteDestination
import it.unibo.alessiociarrocchi.tesiahc.worker.GetDataFromHC

object HomeScreen : RouteDestination {
    override val route: String = "home_screen"
    override val title: String = "Home"
}

@Composable
fun HomeScreen(navController: NavController? = null) {

    val context = LocalContext.current
    MyScaffold(
        title = HomeScreen.title,
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

                WorkManager.getInstance(context).enqueue(
                    getDataFromHC
                )

            }) {
                Text("Get data from HealthConnect")
            }
        }
    }
}