package it.unibo.alessiociarrocchi.tesiahc.presentation.screen

import android.annotation.SuppressLint
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import it.unibo.alessiociarrocchi.tesiahc.R
import it.unibo.alessiociarrocchi.tesiahc.presentation.component.InstalledMessage
import it.unibo.alessiociarrocchi.tesiahc.presentation.component.NotInstalledMessage
import it.unibo.alessiociarrocchi.tesiahc.presentation.component.NotSupportedMessage
import it.unibo.alessiociarrocchi.tesiahc.presentation.theme.HealthConnectTheme
import it.unibo.alessiociarrocchi.tesiahc.services.LocationService
import it.unibo.alessiociarrocchi.tesiahc.showInfoSnackbar

/**
 * Welcome screen shown when the app is first launched.
 */

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WelcomeScreen(
  healthConnectAvailability: it.unibo.alessiociarrocchi.tesiahc.data.HealthConnectAvailability,
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
    Image(
      modifier = Modifier.fillMaxWidth(0.5f),
      painter = painterResource(id = R.drawable.ic_health_connect_logo),
      contentDescription = stringResource(id = R.string.health_connect_logo)
    )
    Spacer(modifier = Modifier.height(32.dp))
    Text(
      text = stringResource(id = R.string.welcome_message),
      color = MaterialTheme.colors.onBackground,
      fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(24.dp))
    when (healthConnectAvailability) {
      it.unibo.alessiociarrocchi.tesiahc.data.HealthConnectAvailability.INSTALLED -> InstalledMessage()
      it.unibo.alessiociarrocchi.tesiahc.data.HealthConnectAvailability.NOT_INSTALLED -> NotInstalledMessage()
      it.unibo.alessiociarrocchi.tesiahc.data.HealthConnectAvailability.NOT_SUPPORTED -> NotSupportedMessage()
    }
    Spacer(modifier = Modifier.height(32.dp))
    Text(
      text = "Servizio localizzazione GPS ogni 15 min.",
      color = MaterialTheme.colors.onBackground,
      fontWeight = FontWeight.Bold
    )
    Text(
      text = "(concedere manualmente permessi notifiche)",
      color = MaterialTheme.colors.onBackground
    )
    Spacer(modifier = Modifier.height(12.dp))
    Button(
      onClick = {

        if (ActivityCompat.checkSelfPermission(applicationContext, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
          if (ActivityCompat.checkSelfPermission(applicationContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.checkSelfPermission(applicationContext, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED){
              val myIntent = Intent(applicationContext, LocationService::class.java).apply {
                action = LocationService.ACTION_START
              }
              applicationContext.startForegroundService(myIntent)
              showInfoSnackbar(scaffoldState, scope, "Servizio avviato correttamente")
            }
            else{
              notificationPermission.launchPermissionRequest()
            }
          }
          else{
            coarseLocationPermission.launchPermissionRequest();
          }
        }
        else{
          fineLocationPermission.launchPermissionRequest();
        }

      }) {
      Text(text = "Avvia")
    }
    Spacer(modifier = Modifier.height(10.dp))
    Button(
      colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
      onClick = {
        val myIntent = Intent(applicationContext, LocationService::class.java).apply {
          action = LocationService.ACTION_STOP
        }
        applicationContext.stopService(myIntent)
        showInfoSnackbar(scaffoldState, scope, "Servizio fermato correttamente")
      }) {
      Text(text = "Ferma")
    }
  }
}
