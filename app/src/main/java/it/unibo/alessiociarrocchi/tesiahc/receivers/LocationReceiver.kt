package it.unibo.alessiociarrocchi.tesiahc.receivers

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import it.unibo.alessiociarrocchi.tesiahc.funcs.startLocationBackgroungService
import it.unibo.alessiociarrocchi.tesiahc.services.LocationService

//private const val TAG = "LocationReceiver"

class LocationReceiver: BroadcastReceiver() {

    companion object {
        const val CHANNEL_ID = "AHC_location"
        const val CHANNEL_NAME = "Tesi Health Connect: gps"
        const val NOTIFICATION_ID = 3
        const val REQUESTCODE = 3
    }

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    override fun onReceive(context: Context, intent: Intent) {
        startLocationBackgroungService(context)

        /*
        val myIntent = Intent(context, LocationService::class.java).apply {
            action = LocationService.ACTION_START
        }
        context.startForegroundService(myIntent)
        */
    }
}