package it.unibo.alessiociarrocchi.tesiahc

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.BloodPressureRecord
import androidx.health.connect.client.records.HeartRateRecord
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import it.unibo.alessiociarrocchi.tesiahc.data.AppContainerImpl
import it.unibo.alessiociarrocchi.tesiahc.worker.GetDataFromHC
import it.unibo.alessiociarrocchi.tesiahc.worker.GetLatestLocation
import it.unibo.alessiociarrocchi.tesiahc.worker.SendDataToIPFS
import java.util.concurrent.TimeUnit


interface InfoNotificationChannel {
    val id: String
    val name: String
    val importance: Int
}

object HealthDataReceiver : InfoNotificationChannel {
    override val id: String = "HealthDataReceiver"
    override val name: String = "Health Data Receiver"
    override val importance: Int = NotificationManager.IMPORTANCE_LOW
}

object HourNotificationReceiver : InfoNotificationChannel {
    override val name: String = "Hour Notification Receiver"
    override val id: String = "HourNotificationReceiver"
    override val importance: Int = NotificationManager.IMPORTANCE_HIGH
}

object LocationChannel : InfoNotificationChannel {
    override val id: String = "LocationChannel"
    override val name: String = "Location Channel"
    override val importance: Int = NotificationManager.IMPORTANCE_LOW
}

class WearableHBPApplication : Application() {

    lateinit var appContainer: AppContainerImpl

    override fun onCreate() {
        super.onCreate()


        val channel = NotificationChannel(
            HealthDataReceiver.id, HealthDataReceiver.name, HealthDataReceiver.importance
        )


        // consiglio di effettuare la misurazione
        val channel1 = NotificationChannel(
            HourNotificationReceiver.id,
            HourNotificationReceiver.name,
            HourNotificationReceiver.importance
        )


        // localizzazione GPS
        val channel2 = NotificationChannel(
            LocationChannel.id, LocationChannel.name, LocationChannel.importance
        )

        val notificationManager = getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager
        notificationManager.createNotificationChannels(listOf(channel, channel1, channel2))


        appContainer = AppContainerImpl(this)

        if (arePermissionsGranted(this)) setupPeriodicWork(
            workManager = WorkManager.getInstance(
                this
            )
        )
    }
}

fun setupPeriodicWork(workManager: WorkManager) {
    val periodicGetDataFromHC: PeriodicWorkRequest =
        PeriodicWorkRequest.Builder(GetDataFromHC::class.java, 30, TimeUnit.MINUTES)
            .addTag("PeriodicGetDataFromHC").build()

    val periodicGetCurrentLocationRequest: PeriodicWorkRequest =
        PeriodicWorkRequest.Builder(GetLatestLocation::class.java, 15, TimeUnit.MINUTES)
            .addTag("PeriodicGetCurrentLocation").build()

    val periodicSendDataToFirebase: PeriodicWorkRequest =
        PeriodicWorkRequest.Builder(SendDataToIPFS::class.java, 30, TimeUnit.MINUTES)
            .addTag("PeriodicSendDataToIPFS").build()

    workManager.enqueueUniquePeriodicWork(
        "getDataFromHC", ExistingPeriodicWorkPolicy.KEEP, periodicGetDataFromHC
    )

    workManager.enqueueUniquePeriodicWork(
        "getCurrentLocation", ExistingPeriodicWorkPolicy.KEEP, periodicGetCurrentLocationRequest
    )

    /*
    workManager.enqueueUniquePeriodicWork(
        "sendDataToFirebase", ExistingPeriodicWorkPolicy.KEEP, periodicSendDataToFirebase
    )*/

    workManager.enqueueUniquePeriodicWork(
        "PeriodicSendDataToIPFS", ExistingPeriodicWorkPolicy.KEEP, periodicSendDataToFirebase
    )

}

@SuppressLint("RestrictedApi")
private fun arePermissionsGranted(context: Context): Boolean {
    val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            HealthPermission.getReadPermission(HeartRateRecord::class),
            HealthPermission.getReadPermission(BloodPressureRecord::class),
            HealthPermission.PERMISSION_READ_HEALTH_DATA_IN_BACKGROUND
        )
    } else {
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            HealthPermission.getReadPermission(HeartRateRecord::class),
            HealthPermission.getReadPermission(BloodPressureRecord::class),
            HealthPermission.PERMISSION_READ_HEALTH_DATA_IN_BACKGROUND
        )
    }

    return permissions.all {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }
}