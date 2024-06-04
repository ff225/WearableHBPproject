package it.unibo.alessiociarrocchi.tesiahc.presentation

import android.annotation.SuppressLint
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import it.unibo.alessiociarrocchi.tesiahc.R
import it.unibo.alessiociarrocchi.tesiahc.data.MyLocationRepository
import it.unibo.alessiociarrocchi.tesiahc.presentation.navigation.Drawer
import it.unibo.alessiociarrocchi.tesiahc.presentation.navigation.HealthConnectNavigation
import it.unibo.alessiociarrocchi.tesiahc.presentation.navigation.Screen
import it.unibo.alessiociarrocchi.tesiahc.presentation.theme.HealthConnectTheme
import kotlinx.coroutines.launch

const val TAG = "Tesi Android Health Connect"

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HealthConnectApp(
  healthConnectManager: it.unibo.alessiociarrocchi.tesiahc.data.MyHealthConnectManager,
  locationRepository: MyLocationRepository,
  applicationContext: android.content.Context
) {
  HealthConnectTheme {
    val scaffoldState = rememberScaffoldState()
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val availability by healthConnectManager.availability

    Scaffold(
      scaffoldState = scaffoldState,
      topBar = {
        TopAppBar(
          title = {
            val titleId = when (currentRoute) {
              Screen.ReadBP.route -> Screen.ReadBP.titleId
              else -> R.string.app_name
            }
            Text(stringResource(titleId))
          },
          navigationIcon = {
            IconButton(
              onClick = {
                if (availability == it.unibo.alessiociarrocchi.tesiahc.data.HealthConnectAvailability.INSTALLED) {
                  scope.launch {
                    scaffoldState.drawerState.open()
                  }
                }
              }
            ) {
              Icon(
                imageVector = Icons.Rounded.Menu,
                stringResource(id = R.string.menu)
              )
            }
          }
        )
      },
      drawerContent = {
        if (availability == it.unibo.alessiociarrocchi.tesiahc.data.HealthConnectAvailability.INSTALLED) {
          Drawer(
            scope = scope,
            scaffoldState = scaffoldState,
            navController = navController
          )
        }
      },
      snackbarHost = {
        SnackbarHost(it) { data -> Snackbar(snackbarData = data) }
      }
    ) {
      HealthConnectNavigation(
        navController = navController,
        scaffoldState = scaffoldState,
        applicationContext = applicationContext,
        healthConnectManager = healthConnectManager,
        myLocationRepository = locationRepository
      )
    }
  }
}
