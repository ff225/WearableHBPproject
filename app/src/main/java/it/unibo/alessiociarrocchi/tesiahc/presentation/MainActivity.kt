package it.unibo.alessiociarrocchi.tesiahc.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
//import it.unibo.alessiociarrocchi.tesiahc.data.MyLocationRepository
import it.unibo.alessiociarrocchi.tesiahc.startHealthDataSync
import it.unibo.alessiociarrocchi.tesiahc.startHealthReminder
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors


class MainActivity : ComponentActivity()  {

  companion object {
    var SERVIZIO_HEALTHDATA : String = "0"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    startHealthReminder(this)

    val healthConnectManager = (application as BaseApplication).healthConnectManager
    /*val locationRepository = MyLocationRepository.getInstance(
      this, Executors.newSingleThreadExecutor()
    )*/

    var hoPermsHealth : Boolean = false

    runBlocking {
      launch {
        hoPermsHealth = healthConnectManager.hasAllPermissions(healthConnectManager.permissions)
      }
    }
    if(hoPermsHealth){
      startHealthDataSync(this)
    }

    setContent {
      HealthConnectApp(
        healthConnectManager = healthConnectManager,
        //locationRepository = locationRepository,
        applicationContext = this)
    }


  }

  override fun onResume() {
    super.onResume()

    val healthConnectManager = (application as BaseApplication).healthConnectManager

    var hoPermsHealth : Boolean = false

    runBlocking {
      launch {
        hoPermsHealth = healthConnectManager.hasAllPermissions(healthConnectManager.permissions)
      }
    }
    if(hoPermsHealth){
      startHealthDataSync(this)
    }

    setContent {
      HealthConnectApp(
        healthConnectManager = healthConnectManager,
        //locationRepository = locationRepository,
        applicationContext = this)
    }

  }

}
