package it.unibo.alessiociarrocchi.tesiahc.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import it.unibo.alessiociarrocchi.tesiahc.data.MyBloodPressureRepository
import it.unibo.alessiociarrocchi.tesiahc.data.MyHeartRateRepository
import it.unibo.alessiociarrocchi.tesiahc.data.MyLocationRepository
import it.unibo.alessiociarrocchi.tesiahc.data.MySettingsRepository
import it.unibo.alessiociarrocchi.tesiahc.funcs.hasLocationPermission
import it.unibo.alessiociarrocchi.tesiahc.funcs.startLocationBackgroungService
//import it.unibo.alessiociarrocchi.tesiahc.data.MyLocationRepository
import it.unibo.alessiociarrocchi.tesiahc.startHealthDataSync
import it.unibo.alessiociarrocchi.tesiahc.startHealthReminder
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors


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
    val mycontext = this;

    // avvio serivzio geolocalizzazione
    if (this.hasLocationPermission()){
      startLocationBackgroungService(this)
    }

    val healthConnectManager = (application as BaseApplication).healthConnectManager

    // avvio notifiche reminder per effettuare le misurazioni
    runBlocking {
      launch {
        if(healthConnectManager.isInstalled()){
          startHealthReminder(mycontext)
        }
      }
    }

    // controllo se i permessi (di lettura) dei dati health sono stati concessi
    var hoPermsHealth : Boolean = false
    runBlocking {
      launch {
        hoPermsHealth = healthConnectManager.hasAllPermissions(healthConnectManager.permissions)
      }
    }
    // avvio del servizio di sincronizzazione
    if(hoPermsHealth){
      startHealthDataSync(mycontext)
    }

    val settRepository = MySettingsRepository.getInstance(
      applicationContext, Executors.newSingleThreadExecutor()
    )

    val locationRepository = MyLocationRepository.getInstance(
      applicationContext, Executors.newSingleThreadExecutor()
    )

    val bpRepository = MyBloodPressureRepository.getInstance(
      applicationContext, Executors.newSingleThreadExecutor()
    )

    val hrRepository = MyHeartRateRepository.getInstance(
      applicationContext, Executors.newSingleThreadExecutor()
    )

    setContent {
      HealthConnectApp(
        healthConnectManager = healthConnectManager,
        myLocationRepository = locationRepository,
        myBPRepository = bpRepository,
        myHRRepository = hrRepository,
        mySettRepository = settRepository,
        applicationContext = mycontext)
    }
  }



}
