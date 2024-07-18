package it.unibo.alessiociarrocchi.tesiahc.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import it.unibo.alessiociarrocchi.tesiahc.funcs.hasLocationPermission
import it.unibo.alessiociarrocchi.tesiahc.funcs.startLocationBackgroungService
//import it.unibo.alessiociarrocchi.tesiahc.data.MyLocationRepository
import it.unibo.alessiociarrocchi.tesiahc.startHealthDataSync
import it.unibo.alessiociarrocchi.tesiahc.startHealthReminder
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class MainActivity : ComponentActivity()  {

  companion object {
    var SERVIZIO_HEALTHDATA : Int = 0
    var SERVIZIO_HEALTHREM : Int = 0
    var SERVIZIO_GPS : Int = 0
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    initMyApp()
  }

  override fun onResume() {
    super.onResume()
    initMyApp()
  }

  fun initMyApp(){
    startHealthReminder(this)

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

    if (this.hasLocationPermission()){
      startLocationBackgroungService(this)
    }

    setContent {
      HealthConnectApp(
        healthConnectManager = healthConnectManager,
        //locationRepository = locationRepository,
        applicationContext = this)
    }
  }



}
