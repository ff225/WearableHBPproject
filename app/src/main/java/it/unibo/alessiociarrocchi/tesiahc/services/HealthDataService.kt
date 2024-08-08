package it.unibo.alessiociarrocchi.tesiahc.services

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import it.unibo.alessiociarrocchi.tesiahc.R
import it.unibo.alessiociarrocchi.tesiahc.receivers.HealthDataReceiver
import it.unibo.alessiociarrocchi.tesiahc.syncHealthData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class HealthDataService: Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()

        start()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun stop() {
        stopForeground(false)
        stopSelf()
    }

    private fun start(){
        val notification = NotificationCompat.Builder(this, HealthDataReceiver.CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setShowWhen(true)
            .setAutoCancel(true)
            .setContentTitle("HEALTH CONNECT")
            .setContentText("Sincronizzazione Health Connect in corso...")

        val notificationManager = getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager
        startForeground(HealthDataReceiver.NOTIFICATION_ID, notification.build())

        runBlocking {
            launch {
                syncHealthData(applicationContext)
            }
        }

        val updatedNotification = notification
            .setContentText("Sincronizzazione Health Connect conclusa!")
            .setWhen(System.currentTimeMillis())

        notificationManager.notify(HealthDataReceiver.NOTIFICATION_ID, updatedNotification.build())
        //notificationManager.cancel(HealthDataReceiver.NOTIFICATION_ID)
        notificationManager.cancelAll()
    }

}
