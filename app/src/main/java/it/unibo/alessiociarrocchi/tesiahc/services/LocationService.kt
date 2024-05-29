package it.unibo.alessiociarrocchi.tesiahc.services

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.LocationServices
import it.unibo.alessiociarrocchi.tesiahc.R
import it.unibo.alessiociarrocchi.tesiahc.data.MyLocationRepository
import it.unibo.alessiociarrocchi.tesiahc.data.db.MyLocationEntity
import it.unibo.alessiociarrocchi.tesiahc.interfaces.LocationClient
import it.unibo.alessiociarrocchi.tesiahc.presentation.DefaultLocationClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.Date
import java.util.concurrent.Executors

private const val TAG = "LocationService"

class LocationService : Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: LocationClient

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        locationClient = DefaultLocationClient(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )

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
        val notification = NotificationCompat.Builder(this, "location")
            .setContentTitle("Tracking location ...")
            .setContentText("Location: null")
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setShowWhen(true)
            .setAutoCancel(true)

        val notificationManager = getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager

        locationClient.getLocationUpdates(900000L) //15 minuti
            .catch { e -> e.printStackTrace() }
            .onEach { location ->
                //Log.d(TAG, location.latitude.toString() + " " + location.longitude.toString())

                val loc_latitude = location.latitude
                val loc_longitude = location.longitude
                val loc_time = location.time;

                //TODO salvare solo se l'ultima posizione è oltre 15 minuti
                val mylocation = MyLocationEntity(
                    latitude = loc_latitude,
                    longitude = loc_longitude,
                    date = Date(loc_time)
                )
                MyLocationRepository.getInstance(applicationContext, Executors.newSingleThreadExecutor())
                    .addLocation(mylocation)

                val updatedNotification = notification
                    .setContentText("Location: ${loc_latitude.toString()}, ${loc_longitude.toString()}")
                    .setWhen(System.currentTimeMillis())

                notificationManager.notify(1, updatedNotification.build())
            }
            .launchIn(serviceScope)

        startForeground(1, notification.build())
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