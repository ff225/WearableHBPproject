package it.unibo.alessiociarrocchi.tesiahc.presentation.screen

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.health.connect.client.HealthConnectClient
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import it.unibo.alessiociarrocchi.tesiahc.R
import it.unibo.alessiociarrocchi.tesiahc.funcs.startLocationBackgroungService
import it.unibo.alessiociarrocchi.tesiahc.presentation.MainActivity
import it.unibo.alessiociarrocchi.tesiahc.presentation.component.InstalledMessage
import it.unibo.alessiociarrocchi.tesiahc.presentation.component.NotInstalledMessage
import it.unibo.alessiociarrocchi.tesiahc.presentation.component.NotSupportedMessage
import it.unibo.alessiociarrocchi.tesiahc.showInfoSnackbar
import it.unibo.alessiociarrocchi.tesiahc.startHealthDataSync
import it.unibo.alessiociarrocchi.tesiahc.startHealthReminder
import it.unibo.alessiociarrocchi.tesiahc.syncHeathData
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import android.health.connect.HealthConnectManager as HCM

/**
 * Welcome screen shown when the app is first launched.
 */

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WelcomeScreen(
  healthConnectAvailability: it.unibo.alessiociarrocchi.tesiahc.data.HealthConnectAvailability,
  healthConnectManager: it.unibo.alessiociarrocchi.tesiahc.data.MyHealthConnectManager,
  onResumeAvailabilityCheck: () -> Unit,
  lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
  applicationContext: android.content.Context,
  scaffoldState : ScaffoldState,
) {

  val notificationPermission = rememberPermissionState(
    permission = android.Manifest.permission.POST_NOTIFICATIONS
  )

  val fineLocationPermission = rememberPermissionState(
    permission = android.Manifest.permission.ACCESS_FINE_LOCATION
  )
  val coarseLocationPermission = rememberPermissionState(
    permission = android.Manifest.permission.ACCESS_COARSE_LOCATION
  )

  val scope = rememberCoroutineScope()

  val currentOnAvailabilityCheck by rememberUpdatedState(onResumeAvailabilityCheck)

  // Add a listener to re-check whether Health Connect has been installed each time the Welcome
  // screen is resumed: This ensures that if the user has been redirected to the Play store and
  // followed the onboarding flow, then when the app is resumed, instead of showing the message
  // to ask the user to install Health Connect, the app recognises that Health Connect is now
  // available and shows the appropriate welcome.
  DisposableEffect(lifecycleOwner) {
    val observer = LifecycleEventObserver { _, event ->
      if (event == Lifecycle.Event.ON_RESUME) {
        currentOnAvailabilityCheck()
      }
    }

    // Add the observer to the lifecycle
    lifecycleOwner.lifecycle.addObserver(observer)

    // When the effect leaves the Composition, remove the observer
    onDispose {
      lifecycleOwner.lifecycle.removeObserver(observer)
    }
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(32.dp),
    verticalArrangement = Arrangement.Top,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    /*Image(
      modifier = Modifier.fillMaxWidth(0.5f),
      painter = painterResource(id = R.drawable.ic_health_connect_logo),
      contentDescription = stringResource(id = R.string.health_connect_logo)
    )*/
    Spacer(modifier = Modifier.height(32.dp))
    Text(
      text = stringResource(id = R.string.welcome_message),
      color = MaterialTheme.colors.onBackground,
      fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(12.dp))
    when (healthConnectAvailability) {
      it.unibo.alessiociarrocchi.tesiahc.data.HealthConnectAvailability.INSTALLED -> InstalledMessage()
      it.unibo.alessiociarrocchi.tesiahc.data.HealthConnectAvailability.NOT_INSTALLED -> NotInstalledMessage()
      it.unibo.alessiociarrocchi.tesiahc.data.HealthConnectAvailability.NOT_SUPPORTED -> NotSupportedMessage()
    }

    // se health connect risulta correttamente installato
    if(healthConnectAvailability == it.unibo.alessiociarrocchi.tesiahc.data.HealthConnectAvailability.INSTALLED){
      Spacer(modifier = Modifier.height(64.dp))

      var hoPermsHealth : Boolean = false

      runBlocking {
        launch {
          hoPermsHealth = healthConnectManager.hasAllPermissions(healthConnectManager.permissions)
        }
      }

      if(hoPermsHealth) {
        if (ActivityCompat.checkSelfPermission(applicationContext, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
          if (ActivityCompat.checkSelfPermission(applicationContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.checkSelfPermission(applicationContext, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED){
              Text(
                text = "Servizio di sincronizzazione dati Health Connect",
                color = MaterialTheme.colors.onBackground,
                fontWeight = FontWeight.Bold
              )
              Spacer(modifier = Modifier.height(8.dp))
              if(MainActivity.SERVIZIO_HEALTHDATA == 0 || MainActivity.SERVIZIO_GPS == 0 || MainActivity.SERVIZIO_HEALTHREM == 0) {
                startLocationBackgroungService(applicationContext)
                startHealthReminder(applicationContext)
                startHealthDataSync(applicationContext)
                showInfoSnackbar(scaffoldState, scope, "Servizio avviato correttamente")
              }
              else{
                Text(
                  text = "Il servizio è attivo correttamente",
                  color = MaterialTheme.colors.onBackground
                )
                Spacer(modifier = Modifier.height(32.dp))
                printBtnSync(applicationContext, scaffoldState)
              }
            }
            else{
              Button(
                onClick = {
                  notificationPermission.launchPermissionRequest()
                }
              ){
                Text("Concedi permessi notifiche")
              }

              // avviso
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = "in caso di problemi concedere manualmente tutti i permessi",
                color = MaterialTheme.colors.onBackground
              )
            }
          }
          else{
            Button(
              onClick = {
                coarseLocationPermission.launchPermissionRequest()
              }
            ){
              Text("Concedi permessi posizione GPS")
            }
          }
        }
        else{
          Button(
            onClick = {
              fineLocationPermission.launchPermissionRequest()
            }
          ){
            Text("Concedi permessi posizione GPS")
          }

          // avviso
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "in caso di problemi concedere manualmente tutti i permessi",
            color = MaterialTheme.colors.onBackground
          )
        }

      }
      else {
        Button(
          onClick ={
            /*val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
              // HCM is an import alias for HealthConnectManager from the Health Connect client
              Intent(HCM.ACTION_MANAGE_HEALTH_PERMISSIONS)
                .putExtra(
                  Intent.EXTRA_PACKAGE_NAME,
                  applicationContext.packageName
                )
            } else {
              Intent(
                HealthConnectClient.ACTION_HEALTH_CONNECT_SETTINGS
              )
            }*/

            val intent =Intent(
              HealthConnectClient.ACTION_HEALTH_CONNECT_SETTINGS
            )

            startActivity(applicationContext, intent, null)

            healthConnectManager.requestPermissionsActivityContract()
          }
        ){
          Text("Concedi permessi Health Connect")
        }

        // avviso
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "Se qualcosa va storto, concedere manualmente tutti i permessi richiesti.",
          color = MaterialTheme.colors.onBackground
        )
      }


    }

  }
}

@Composable
fun printBtnSync(applicationContext: android.content.Context,
                 scaffoldState : ScaffoldState, ){
  val scope = rememberCoroutineScope()
  Button(
    onClick = {

      runBlocking {
        launch {
          syncHeathData(applicationContext)
        }
      }

      showInfoSnackbar(scaffoldState, scope, "Lettura dati Health Connect conclusa con successo")
    }) {
    Text(text = "Lettura manuale Health Connect")
  }
}