package it.unibo.alessiociarrocchi.tesiahc.presentation.screen.locationGps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import it.unibo.alessiociarrocchi.tesiahc.presentation.AppViewModelProvider
import it.unibo.alessiociarrocchi.tesiahc.presentation.RouteDestination
import it.unibo.alessiociarrocchi.tesiahc.presentation.component.LocationRow
import it.unibo.alessiociarrocchi.tesiahc.presentation.screen.HomeScreen
import it.unibo.alessiociarrocchi.tesiahc.presentation.screen.MyScaffold


object LocationScreen : RouteDestination {
    override val route: String
        get() = "location_screen"
    override val title: String
        get() = "Location"

}

@Composable
fun LocationScreen(
    navController: NavController? = null,
) {

    val viewModel: LocationViewModel = viewModel(
        factory =
        AppViewModelProvider.provideViewModel(LocalContext.current)
    )



    MyScaffold(
        navController = navController,
        showBackButton = true,
        title = LocationScreen.title,
        navigateTo = {
            navController?.navigate(HomeScreen.route) {
                popUpTo(HomeScreen.route) {
                    inclusive = true
                }
            }
        }
    ) {
        val locationPosition = viewModel.locList.collectAsState().value
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(locationPosition) { item ->
                LocationRow(
                    item.id,
                    item.time,
                    item.latitude,
                    item.longitude,
                    onLongClick = { id ->
                        viewModel.deleteItem(id)
                    },
                )
            }
        }
    }
}