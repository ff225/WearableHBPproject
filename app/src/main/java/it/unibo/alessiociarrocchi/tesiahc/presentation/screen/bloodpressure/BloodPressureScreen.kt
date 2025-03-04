package it.unibo.alessiociarrocchi.tesiahc.presentation.screen.bloodpressure

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import it.unibo.alessiociarrocchi.tesiahc.presentation.AppViewModelProvider
import it.unibo.alessiociarrocchi.tesiahc.presentation.RouteDestination
import it.unibo.alessiociarrocchi.tesiahc.presentation.component.BloodPressureRow
import it.unibo.alessiociarrocchi.tesiahc.presentation.screen.HomeScreen
import it.unibo.alessiociarrocchi.tesiahc.presentation.screen.MyScaffold
import it.unibo.alessiociarrocchi.tesiahc.presentation.screen.bloodpressuredetail.BloodPressureDetailScreen


object BloodPressureScreen : RouteDestination {
    override val route: String
        get() = "blood_pressure_screen"
    override val title: String
        get() = "Blood Pressure"
}

@Composable
fun BloodPressureScreen(
    navController: NavController? = null
) {

    val viewModel: BloodPressureViewModel = viewModel(
        factory = AppViewModelProvider.provideViewModel(
            LocalContext.current
        )
    )
    MyScaffold(
        showBackButton = true,
        navController = navController,
        navigateTo = {
            navController?.navigate(HomeScreen.route) {
                popUpTo(HomeScreen.route) {
                    inclusive = true
                }
            }
        },
        title = BloodPressureScreen.title,

        ) {
        Log.d(
            "BloodPressureScreen",
            "viewModel.bpList.value: ${viewModel.bpList.collectAsState().value}"
        )
        val values = viewModel.bpList.collectAsState().value
        LazyColumn(
            Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            items(values) { item ->
                BloodPressureRow(
                    item.time,
                    item.timezone,
                    item.systolic,
                    item.diastolic,
                    item.id,
                    onDetailsClick = { id ->
                        navController?.navigate(
                            BloodPressureDetailScreen.routeWithArgs.replace(
                                "{${BloodPressureDetailScreen.itemIdArg}}",
                                id.toString()
                            )
                        )
                    },
                )
            }
        }
    }

}

