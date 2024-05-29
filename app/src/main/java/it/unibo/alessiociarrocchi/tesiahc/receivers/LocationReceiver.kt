package it.unibo.alessiociarrocchi.tesiahc.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationResult
import it.unibo.alessiociarrocchi.tesiahc.services.LocationService

private const val TAG = "LocationReceiver"

class LocationReceiver: BroadcastReceiver() {

    @Override
    override fun onReceive(context: Context, intent: Intent) {

        val myIntent = Intent(context, LocationService::class.java)
        context.startForegroundService(myIntent)

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