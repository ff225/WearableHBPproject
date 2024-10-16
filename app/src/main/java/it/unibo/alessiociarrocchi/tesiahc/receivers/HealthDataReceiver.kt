package it.unibo.alessiociarrocchi.tesiahc.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import it.unibo.alessiociarrocchi.tesiahc.services.HealthDataService

class HealthDataReceiver: BroadcastReceiver() {

    companion object {
        const val CHANNEL_ID = "AHC_sync"
        const val CHANNEL_NAME = "Tesi Health Connect: sincronizzazione"
        const val NOTIFICATION_ID = 2
        const val REQUESTCODE = 2
    }

    @Override
    override fun onReceive(context: Context, intent: Intent) {
        val myIntent = Intent(context, HealthDataService::class.java).apply {
            action = HealthDataService.ACTION_START
        }
        context.startForegroundService(myIntent)

    }
}