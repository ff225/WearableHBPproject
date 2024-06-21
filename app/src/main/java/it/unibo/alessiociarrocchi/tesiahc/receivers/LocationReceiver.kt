package it.unibo.alessiociarrocchi.tesiahc.receivers

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import it.unibo.alessiociarrocchi.tesiahc.services.LocationService

//private const val TAG = "LocationReceiver"

class LocationReceiver: BroadcastReceiver() {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    override fun onReceive(context: Context, intent: Intent) {
        val myIntent = Intent(context, LocationService::class.java)
        context.startForegroundService(myIntent)

        /*
        val myIntent = Intent(context, LocationService::class.java).apply {
            action = LocationService.ACTION_START
        }
        context.startForegroundService(myIntent)
        */
    }
}