package it.unibo.alessiociarrocchi.tesiahc

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import it.unibo.alessiociarrocchi.tesiahc.data.AppContainerImpl
import it.unibo.alessiociarrocchi.tesiahc.data.MyHealthConnectManager
import it.unibo.alessiociarrocchi.tesiahc.receivers.HealthDataReceiver
import it.unibo.alessiociarrocchi.tesiahc.receivers.HourNotificationReceiver
import it.unibo.alessiociarrocchi.tesiahc.receivers.LocationReceiver

class WearableHBPApplication : Application() {

    lateinit var appContainer: AppContainerImpl
    val healthConnectManager by lazy {
        MyHealthConnectManager(this)
    }

    override fun onCreate() {
        super.onCreate()

        // TODO move all notification here.
        val channel = NotificationChannel(
            HealthDataReceiver.CHANNEL_ID,
            HealthDataReceiver.CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        val notificationManager = getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        // consiglio di effettuare la misurazione
        val channel1 = NotificationChannel(
            HourNotificationReceiver.CHANNEL_ID,
            HourNotificationReceiver.CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager1 = getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager
        notificationManager1.createNotificationChannel(channel1)

        // localizzazione GPS
        val channel2 = NotificationChannel(
            LocationReceiver.CHANNEL_ID,
            LocationReceiver.CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        val notificationManager2 = getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager
        notificationManager2.createNotificationChannel(channel2)
        appContainer = AppContainerImpl(this)
    }
}