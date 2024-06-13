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
    /*private lateinit var notification : NotificationCompat.Builder
    private lateinit var notificationManager : NotificationManager*/

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
       /* notification = NotificationCompat.Builder(this, "AHC_location")
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setShowWhen(true)
            .setAutoCancel(true)
            .setContentTitle("Tesi Android Health Connect")
            .setContentText("Servizio localizzazione GPS avviato")

        notificationManager = getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager*/

        locationClient.getLocationUpdates(900000L) //15 minuti
            .catch { e -> e.printStackTrace() }
            .onEach { location ->
                //Log.d(TAG, location.latitude.toString() + " " + location.longitude.toString())

                if(location != null){
                    val loc_latitude = location.latitude
                    val loc_longitude = location.longitude
                    val loc_time = location.time;

                    val lastLocation = MyLocationRepository.getInstance(applicationContext, Executors.newSingleThreadExecutor())
                        .getLastLocation()

                    if (lastLocation == null){
                        SaveLocation(loc_latitude, loc_longitude, loc_time)
                    }
                    else{
                        if(lastLocation.mydate == null){
                            SaveLocation(loc_latitude, loc_longitude, loc_time)
                        }
                        else{
                            val last_time = lastLocation.mydate.time
                            val last_time_conf =last_time + (15 * 60 * 1000) // aggiungo 15 minuti
                            if(last_time_conf < loc_time){
                                SaveLocation(loc_latitude, loc_longitude, loc_time)
                            }
                        }
                    }
                }

            }
            .launchIn(serviceScope)

        //startForeground(1, notification.build())
    }

    private fun SaveLocation(loc_latitude: Double, loc_longitude: Double, loc_time: Long){
        val mylocation = MyLocationEntity(
            latitude = loc_latitude,
            longitude = loc_longitude,
            mydate = Date(loc_time)
        )

        MyLocationRepository.getInstance(applicationContext, Executors.newSingleThreadExecutor())
            .addLocation(mylocation)

       /* val updatedNotification = notification
            .setContentText("Location: ${loc_latitude.toString()}, ${loc_longitude.toString()}")
            .setWhen(System.currentTimeMillis())

        notificationManager.notify(1, updatedNotification.build())*/
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