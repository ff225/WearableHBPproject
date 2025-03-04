package it.unibo.alessiociarrocchi.tesiahc.worker

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import it.unibo.alessiociarrocchi.tesiahc.WearableHBPApplication
import it.unibo.alessiociarrocchi.tesiahc.data.model.LocationEntity
import kotlinx.coroutines.suspendCancellableCoroutine
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class GetLatestLocation(ctx: Context, params: WorkerParameters) : CoroutineWorker(ctx, params) {

    private val latest = LocationServices.getFusedLocationProviderClient(ctx)
    private val context = ctx
    private val locationRepository = (ctx as WearableHBPApplication).appContainer.locationRepository

    override suspend fun doWork(): Result {
        try {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return Result.failure()
            }



            latest.getCurrentLocation(
                Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                CancellationTokenSource().token
            ).await().let { location ->
                location?.let {

                    locationRepository.addLocation(
                        LocationEntity(
                            latitude = location.latitude,
                            longitude = location.longitude,
                            date = Instant.ofEpochMilli(location.time).truncatedTo(ChronoUnit.DAYS)
                                .toEpochMilli(),
                            time = location.time,

                            )
                    )

                }
                Log.d(
                    "GetLatestLocation",
                    "Current Location: ${location.latitude}, ${location.longitude}"
                )
            }


        } catch (e: Exception) {
            Log.d("GetLatestLocation", "Error: ${e.message}")
            return Result.failure()
        }


        return Result.success()
    }
}

/**
 * Extension function to convert Task to suspend function
 */
suspend fun <T> Task<T>.await(): T {
    return suspendCancellableCoroutine { cont ->
        addOnSuccessListener { result -> cont.resume(result) }
        addOnFailureListener { exception -> cont.resumeWithException(exception) }
        addOnCanceledListener { cont.cancel() }
    }
}