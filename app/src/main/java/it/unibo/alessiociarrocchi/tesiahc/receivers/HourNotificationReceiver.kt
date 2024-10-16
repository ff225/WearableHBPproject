package it.unibo.alessiociarrocchi.tesiahc.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import it.unibo.alessiociarrocchi.tesiahc.services.HourNotificationService

class HourNotificationReceiver: BroadcastReceiver() {

    companion object {
        const val CHANNEL_ID = "AHC_hour_notification"
        const val CHANNEL_NAME = "Tesi Health Connect: memo misurazione"
        const val NOTIFICATION_ID = 1
        const val REQUESTCODE = 1
    }

    @Override
    override fun onReceive(context: Context, intent: Intent) {
        val myIntent = Intent(context, HourNotificationService::class.java).apply {
            action = HourNotificationService.ACTION_START
        }
        context.startForegroundService(myIntent)

    }
}