package it.unibo.alessiociarrocchi.tesiahc.presentation.navigation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import it.unibo.alessiociarrocchi.tesiahc.data.MyLocationRepository
//import androidx.lifecycle.compose.collectAsStateWithLifecycle

//import it.unibo.alessiociarrocchi.tesiahc.data.MyLocationRepository
import it.unibo.alessiociarrocchi.tesiahc.showExceptionSnackbar
import it.unibo.alessiociarrocchi.tesiahc.presentation.screen.WelcomeScreen
import it.unibo.alessiociarrocchi.tesiahc.presentation.screen.bloodpressure.BloodPressureScreen
import it.unibo.alessiociarrocchi.tesiahc.presentation.screen.bloodpressure.BloodPressureViewModel
import it.unibo.alessiociarrocchi.tesiahc.presentation.screen.bloodpressure.BloodPressureViewModelFactory
import it.unibo.alessiociarrocchi.tesiahc.presentation.screen.bloodpressuredetail.BloodPressureDetailScreen
import it.unibo.alessiociarrocchi.tesiahc.presentation.screen.bloodpressuredetail.BloodPressureDetailViewModel
import it.unibo.alessiociarrocchi.tesiahc.presentation.screen.bloodpressuredetail.BloodPressureDetailViewModelFactory
import it.unibo.alessiociarrocchi.tesiahc.presentation.screen.locationGps.LocationScreen
import it.unibo.alessiociarrocchi.tesiahc.presentation.screen.locationGps.LocationViewModel
import it.unibo.alessiociarrocchi.tesiahc.presentation.screen.locationGps.LocationViewModelFactory
//import it.unibo.alessiociarrocchi.tesiahc.presentation.screen.locationGps.LocationScreen
//import it.unibo.alessiociarrocchi.tesiahc.presentation.screen.locationGps.LocationViewModel
//import it.unibo.alessiociarrocchi.tesiahc.presentation.screen.locationGps.LocationViewModelFactory
import it.unibo.alessiociarrocchi.tesiahc.showInfoSnackbar

/**
 * Provides the navigation in the app.
 */
@Composable
fun HealthConnectNavigation(
  navController: NavHostController,
  scaffoldState: ScaffoldState,
  applicationContext: android.content.Context,
  healthConnectManager: it.unibo.alessiociarrocchi.tesiahc.data.MyHealthConnectManager,
  myLocationRepository : MyLocationRepository
) {
  val scope = rememberCoroutineScope()
  NavHost(navController = navController, startDestination = Screen.WelcomeScreen.route) {

    val availabilityHealthConnect by healthConnectManager.availability

    // pagina di benvenuto
    composable(Screen.WelcomeScreen.route) {
      WelcomeScreen(
        healthConnectAvailability = availabilityHealthConnect,
        healthConnectManager = healthConnectManager,
        onResumeAvailabilityCheck = {
          healthConnectManager.checkAvailability()
        },
        applicationContext = applicationContext,
        scaffoldState = scaffoldState,
      )
    }

    /*composable(
      route = Screen.PrivacyPolicy.route,
      deepLinks = listOf(
        navDeepLink {
          action = "androidx.health.ACTION_SHOW_PERMISSIONS_RATIONALE"
        }
      )
    ) {
      PrivacyPolicyScreen()
    }*/

    // elenco delle misurazioni pressione del sangue
    composable(Screen.ReadBP.route) {
      val viewModel: BloodPressureViewModel = viewModel(
        factory = BloodPressureViewModelFactory(
          healthConnectManager = healthConnectManager
        )
      )
      val permissionsGranted by viewModel.permissionsGranted
      val sessionsList by viewModel.bpList
      val permissions = healthConnectManager.permissions
      val onPermissionsResult = { viewModel.initialLoad() }
      val permissionsLauncher =
        rememberLauncherForActivityResult(viewModel.permissionsLauncher) {
          onPermissionsResult()
        }
      BloodPressureScreen(
        permissionsGranted = permissionsGranted,
        permissions = permissions,
        bpList = sessionsList,
        uiState = viewModel.uiState,
        onInsertClick = {

        },
        onDetailsClick = {
          uid -> navController.navigate(Screen.BloodPressureDetail.route + "/" + uid)
        },
        onError = { exception ->
          showExceptionSnackbar(scaffoldState, scope, exception)
        },
        onPermissionsResult = {
          viewModel.initialLoad()
        },
        onPermissionsLaunch = { values ->
          permissionsLauncher.launch(values)
        }
      )
    }

    // dettaglio misurazione pressione del sangue
    composable(Screen.BloodPressureDetail.route + "/{$UID_NAV_ARGUMENT}") {
      val uid = it.arguments?.getString(UID_NAV_ARGUMENT)!!
      val viewModel: BloodPressureDetailViewModel = viewModel(
        factory = BloodPressureDetailViewModelFactory(
          uid = uid,
          healthConnectManager = healthConnectManager
        )
      )
      val permissionsGranted by viewModel.permissionsGranted
      val permissions = healthConnectManager.permissions
      val onPermissionsResult = { viewModel.initialLoad() }
      val permissionsLauncher =
        rememberLauncherForActivityResult(viewModel.permissionsLauncher) {
          onPermissionsResult()
        }

      val myBP by viewModel.bpDetail
      val hrAggregate by viewModel.hrAggregate

      BloodPressureDetailScreen(
        permissions = permissions,
        permissionsGranted = permissionsGranted,
        myBP = myBP,
        hrAggregate = hrAggregate,
        uiState = viewModel.uiState,
        onError = { exception ->
          showExceptionSnackbar(scaffoldState, scope, exception)
        },
        onPermissionsResult = {
          viewModel.initialLoad()
        },
        onPermissionsLaunch = { values ->
          permissionsLauncher.launch(values)
        }
      )
    }

    // elenco misurazioni sonno
    composable(Screen.ReadSleep.route){

    }

    // elenco posizioni gps
    composable(Screen.ReadLocations.route){
      val viewModel: LocationViewModel = viewModel(
        factory = LocationViewModelFactory(myLocationRepository)
      )
      viewModel.initialLoad()
      val sessionsList by viewModel.locList.collectAsState()

      LocationScreen(
        locList = sessionsList,
        onLongClick = {
            uid -> viewModel.deleteLocationAndRefresh(uid)
            showInfoSnackbar(scaffoldState, scope, "Elemento eliminato correttamente")
        },
        onConfirmFilters = {
          dates -> viewModel.refreshWithFilters(dates)
          showInfoSnackbar(scaffoldState, scope, "Lista aggiornata correttamente")
        }
      )
    }

  }
}
