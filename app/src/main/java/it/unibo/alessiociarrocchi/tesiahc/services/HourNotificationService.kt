package it.unibo.alessiociarrocchi.tesiahc.services

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import it.unibo.alessiociarrocchi.tesiahc.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

class HourNotificationService : Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    //private lateinit var notification : NotificationCompat.Builder
    //private lateinit var notificationManager : NotificationManager

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        start()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        /*notification = NotificationCompat.Builder(this, "AHC_hour_notification")
            .setContentTitle("Tesi Android Health Connect")
            .setContentText("Ricordati di effettuare una misurazione della pressione arteriosa")
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setShowWhen(true)
            .setWhen(System.currentTimeMillis())
            .setAutoCancel(true)

        notificationManager = getSystemService(
            NOTIFICATION_SERVICE
        ) as NotificationManager

        startForeground(1, notification.build())*/
    }

    private fun stop() {
        stopForeground(true)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }

}