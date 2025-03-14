package it.unibo.alessiociarrocchi.tesiahc.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.google.firebase.initialize
import com.google.firebase.installations.FirebaseInstallations
import it.unibo.alessiociarrocchi.tesiahc.WearableHBPApplication

class SendDataToFirebase(ctx: Context, workParams: WorkerParameters) :
    CoroutineWorker(ctx, workParams) {

    private val firebase =
        Firebase.database("https://wearablehbpproject-default-rtdb.europe-west1.firebasedatabase.app/").reference
    private val bloodPressureRepository =
        (ctx as WearableHBPApplication).appContainer.bloodPressureRepository
    private val heartRateRepository =
        (ctx as WearableHBPApplication).appContainer.heartRateRepository

    override suspend fun doWork(): Result {

        Firebase.initialize(applicationContext)
        val firebaseInstallation = FirebaseInstallations.getInstance().id.await()

        bloodPressureRepository.getItemsUnsynced().forEach { bloodPressureData ->
            firebase.child(firebaseInstallation).child(bloodPressureData.uid)
                .setValue(
                    mapOf(
                        "id" to bloodPressureData.id,
                        "uid" to bloodPressureData.uid,
                        "timestamp" to bloodPressureData.time,
                        "timezone" to bloodPressureData.timezone,
                        "systolic" to bloodPressureData.systolic,
                        "diastolic" to bloodPressureData.diastolic,
                        "description" to bloodPressureData.description,
                        "bodyPosition" to bloodPressureData.bodyPosition,
                        "latitude" to bloodPressureData.latitude,
                        "longitude" to bloodPressureData.longitude,
                    )
                )
            heartRateRepository.getItemByExternalId(bloodPressureData.uid).let { heartRateData ->
                heartRateData?.let {
                    firebase.child(firebaseInstallation).child(bloodPressureData.uid)
                        .updateChildren(
                            mapOf(
                                "heartRate" to mapOf(
                                    "hrStart" to it.hrStart.time,
                                    "hrEnd" to it.hrEnd.time,
                                    "hrAVG" to it.hrAVG,
                                    "hrMIN" to it.hrMIN,
                                    "hrMAX" to it.hrMAX,
                                    "hrMC" to it.hrMC,
                                )
                            )
                        )
                }
            }
            bloodPressureRepository.updateItem(bloodPressureData.copy(synced = true))
        }

        return Result.success()
    }

}