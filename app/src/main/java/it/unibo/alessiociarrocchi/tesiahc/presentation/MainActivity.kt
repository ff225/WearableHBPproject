package it.unibo.alessiociarrocchi.tesiahc.presentation

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import it.unibo.alessiociarrocchi.tesiahc.data.MyLocationRepository
import it.unibo.alessiociarrocchi.tesiahc.receivers.HourNotificationReceiver
import java.util.Calendar
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
    /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      ActivityCompat.requestPermissions(
        this,
        arrayOf(
          android.Manifest.permission.POST_NOTIFICATIONS
        ),
        0
      )
    }*/

    val calendar: Calendar = Calendar.getInstance()
    val intent1 = Intent(this@MainActivity, HourNotificationReceiver::class.java)
    val pendingIntent =
      PendingIntent.getBroadcast(this, 0, intent1, PendingIntent.FLAG_MUTABLE)
    val am = this.getSystemService(ALARM_SERVICE) as AlarmManager
    am.setRepeating(
      AlarmManager.RTC_WAKEUP,
      calendar.getTimeInMillis(),
      (1000 * 60 * 60).toLong(),
      pendingIntent
    )

    val healthConnectManager = (application as BaseApplication).healthConnectManager

    val locationRepository = MyLocationRepository.getInstance(
        applicationContext, Executors.newSingleThreadExecutor()
      )

    setContent {
      HealthConnectApp(
        healthConnectManager = healthConnectManager,
        locationRepository = locationRepository,
        applicationContext = applicationContext)
    }

    /*if (ActivityCompat.checkSelfPermission(applicationContext, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
      && ActivityCompat.checkSelfPermission(applicationContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    )
    {
      val myIntent = Intent(applicationContext, LocationService::class.java)
      applicationContext.startForegroundService(myIntent)
    }*/

  }
}
