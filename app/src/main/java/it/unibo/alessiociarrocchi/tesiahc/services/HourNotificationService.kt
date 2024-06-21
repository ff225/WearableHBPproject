package it.unibo.alessiociarrocchi.tesiahc.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
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

        /*val notification = NotificationCompat.Builder(this, "AHC_hour_notification")
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setShowWhen(true)
            .setWhen(System.currentTimeMillis())
            .setAutoCancel(true)
            .setContentTitle("Tesi Android Health Connect")
            .setContentText("E' un buon momento per effettuare la misurazione della pressione arteriosa?")

        startForeground(2, notification.build())*/
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