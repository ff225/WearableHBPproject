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

        /*val myIntent = Intent(context, LocationService::class.java).apply {
            action = LocationService.ACTION_START
        }
        context.startForegroundService(myIntent)*/

        /*if (intent.action == ACTION_PROCESS_UPDATES) {

            // Checks for location availability changes.
            LocationAvailability.extractLocationAvailability(intent)?.let { locationAvailability ->
                if (!locationAvailability.isLocationAvailable) {
                    Log.d(TAG, "Location services are no longer available!")
                }
            }

            LocationResult.extractResult(intent)?.let { locationResult ->
                val locations = locationResult.locations.map { location ->
                    Log.d(TAG, location.latitude.toString() + " " + location.longitude.toString())
                }
                if (locations.isNotEmpty()) {

                }
            }
        }*/

    }


    /*companion object {
        const val ACTION_PROCESS_UPDATES =
            "it.unibo.alessiociarrocchi.tesiahc.action." +
                    "PROCESS_UPDATES"
    }*/

}