package it.unibo.alessiociarrocchi.tesiahc.presentation

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import it.unibo.alessiociarrocchi.tesiahc.data.AppContainer
import it.unibo.alessiociarrocchi.tesiahc.data.AppDataContainer

class BaseApplication : Application() {
  val healthConnectManager by lazy {
      it.unibo.alessiociarrocchi.tesiahc.data.MyHealthConnectManager(this)
  }

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()

        val channel = NotificationChannel(
                "AHC_location",
                "LocationTesiAndroidHealthConnect",
                NotificationManager.IMPORTANCE_LOW
            )
        val notificationManager = getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        val channel1 = NotificationChannel(
                "AHC_hour_notification",
                "HourNotificationTesiAndroidHealthConnect",
                NotificationManager.IMPORTANCE_HIGH
            )
        val notificationManager1 = getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager
        notificationManager1.createNotificationChannel(channel1)

        container = AppDataContainer(this)
    }
}
