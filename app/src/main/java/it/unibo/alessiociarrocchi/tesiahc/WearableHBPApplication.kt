package it.unibo.alessiociarrocchi.tesiahc

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import it.unibo.alessiociarrocchi.tesiahc.data.AppContainerImpl
import it.unibo.alessiociarrocchi.tesiahc.receivers.HealthDataReceiver
import it.unibo.alessiociarrocchi.tesiahc.receivers.HourNotificationReceiver
import it.unibo.alessiociarrocchi.tesiahc.receivers.LocationReceiver

class WearableHBPApplication : Application() {

    lateinit var appContainer: AppContainerImpl

    override fun onCreate() {
        super.onCreate()


        val channel = NotificationChannel(
            HealthDataReceiver.CHANNEL_ID,
            HealthDataReceiver.CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )


        // consiglio di effettuare la misurazione
        val channel1 = NotificationChannel(
            HourNotificationReceiver.CHANNEL_ID,
            HourNotificationReceiver.CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )


        // localizzazione GPS
        val channel2 = NotificationChannel(
            LocationReceiver.CHANNEL_ID,
            LocationReceiver.CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )

        val notificationManager = getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager
        notificationManager.createNotificationChannels(listOf(channel, channel1, channel2))


        appContainer = AppContainerImpl(this)
    }
}