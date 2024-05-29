package it.unibo.alessiociarrocchi.tesiahc.presentation

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import it.unibo.alessiociarrocchi.tesiahc.data.MyLocationRepository
import it.unibo.alessiociarrocchi.tesiahc.services.LocationService
import java.util.concurrent.Executors

class MainActivity : ComponentActivity()  {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    ActivityCompat.requestPermissions(
      this,
      arrayOf(
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_FINE_LOCATION
      ),
      0
    )
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      ActivityCompat.requestPermissions(
        this,
        arrayOf(
          android.Manifest.permission.POST_NOTIFICATIONS
        ),
        0
      )
    }

    val healthConnectManager = (application as BaseApplication).healthConnectManager

    val locationRepository = MyLocationRepository.getInstance(
        applicationContext, Executors.newSingleThreadExecutor()
      )

    setContent {
      HealthConnectApp(
        healthConnectManager = healthConnectManager,
        locationRepository = locationRepository)
    }

    if (ActivityCompat.checkSelfPermission(applicationContext, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
      && ActivityCompat.checkSelfPermission(applicationContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    )
    {
      val myIntent = Intent(applicationContext, LocationService::class.java)
      applicationContext.startForegroundService(myIntent)
    }

  }
}
