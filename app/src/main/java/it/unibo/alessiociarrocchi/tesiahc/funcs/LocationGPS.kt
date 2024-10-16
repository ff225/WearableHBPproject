package it.unibo.alessiociarrocchi.tesiahc.funcs

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import it.unibo.alessiociarrocchi.tesiahc.interfaces.LocationClient
import it.unibo.alessiociarrocchi.tesiahc.presentation.MainActivity
import it.unibo.alessiociarrocchi.tesiahc.services.LocationService
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

fun Context.hasLocationPermission(): Boolean {
    return ContextCompat.checkSelfPermission(
        this, Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
}

fun startLocationBackgroungService(context: Context){
    if (MainActivity.SERVIZIO_GPS == 0){
        val myIntent = Intent(context, LocationService::class.java).apply {
            action = LocationService.ACTION_START
        }
        context.startForegroundService(myIntent)
    }
}

class DefaultLocationClient(
    private val context: Context,
    private val client: FusedLocationProviderClient
) : LocationClient {

    @SuppressLint("MissingPermission")
    @Suppress("DEPRECATION")
    override fun getLocationUpdates(interval: Long): Flow<Location> {
        return callbackFlow {
            if (!context.hasLocationPermission()) {
                throw LocationClient.LocationException("Missing location permission")
            }

            val locationManager = context.getSystemService(
                Context.LOCATION_SERVICE
            ) as LocationManager
            val isGpsEnabled = locationManager.isProviderEnabled(
                LocationManager.GPS_PROVIDER
            )
            val isNetworkEnabled = locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
            )

            if (!isGpsEnabled && !isNetworkEnabled)
                throw LocationClient.LocationException("GPS is disabled")

            val request = LocationRequest.create()
                .setInterval(interval)
                .setFastestInterval(interval)

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    super.onLocationResult(result)
                    result.locations.lastOrNull()?.let { location ->
                        launch { send(location) }
                    }
                }
            }

            client.requestLocationUpdates(
                request, locationCallback, Looper.getMainLooper()
            )

            awaitClose {
                client.removeLocationUpdates(locationCallback)
            }
        }
    }

}

/*
private lateinit var locationCallback: LocationCallback

@RequiresApi(Build.VERSION_CODES.S)
fun getCurrentLocation(
    context: Context,
    onLocationChangeListener: OnLocationChangeListener
) {
    var fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    var locationRequest = LocationRequest().apply {
        // Sets the desired interval for
        // active location updates.
        // This interval is inexact.
        interval = TimeUnit.SECONDS.toMillis(60)

        // Sets the fastest rate for active location updates.
        // This interval is exact, and your application will never
        // receive updates more frequently than this value
        fastestInterval = TimeUnit.SECONDS.toMillis(30)

        // Sets the maximum time when batched location
        // updates are delivered. Updates may be
        // delivered sooner than this interval
        maxWaitTime = TimeUnit.MINUTES.toMillis(2)

        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }


    locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult)

            val mylocRes = locationResult
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)

            onLocationChangeListener.onComplete(getLatLng(mylocRes))

        }
    }

    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        return
    }
    fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
}

private fun getLatLng(location: LocationResult?): MyLocationTracker {
    if (location != null){
        if (location.lastLocation != null){
            return MyLocationTracker(latitude = location.lastLocation!!.latitude, longitude = location.lastLocation!!.longitude, altitude = location.lastLocation!!.altitude)
        }
    }
    return MyLocationTracker(0.0, 0.0, 0.0)
}

interface OnLocationChangeListener {
    fun onComplete(location: MyLocationTracker?)
}

 */