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

    private val stepsRepository =
        (ctx as WearableHBPApplication).appContainer.stepsRepository

    private val exerciseRepository =
        (ctx as WearableHBPApplication).appContainer.exerciseRepository

    private val bloodOxygenRepository =
        (ctx as WearableHBPApplication).appContainer.boRepository

    override suspend fun doWork(): Result {

        Firebase.initialize(applicationContext)
        val firebaseInstallation = FirebaseInstallations.getInstance().id.await()

        bloodPressureRepository.getItemsUnsynced().forEach { bloodPressureData ->
            firebase.child("${firebaseInstallation}/blood-pressure").child(bloodPressureData.uid)
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
                    firebase.child("${firebaseInstallation}/blood-pressure").child(bloodPressureData.uid)
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
        stepsRepository.getStepsUnsynced().forEach { stepsData ->
            firebase.child("${firebaseInstallation}/steps").child(stepsData.uid)
                .setValue(
                    mapOf(
                        "id" to stepsData.id,
                        "uid" to stepsData.uid,
                        "start_time" to stepsData.startTime,
                        "start_time_zone" to stepsData.startTimeZone,
                        "end_time" to stepsData.endTime,
                        "end_time_zone" to stepsData.endTimeZone,
                        "count" to stepsData.count,
                    )
                )
            stepsRepository.updateItem(stepsData.copy(synced = true))
        }
        exerciseRepository.getExerciseUnsynced().forEach { exerciseData ->
            firebase.child("${firebaseInstallation}/exercises").child(exerciseData.uid)
                .setValue(
                    mapOf(
                        "id" to exerciseData.id,
                        "uid" to exerciseData.uid,
                        "start_time" to exerciseData.startTime,
                        "start_time_zone" to exerciseData.startTimeZone,
                        "end_time" to exerciseData.endTime,
                        "end_time_zone" to exerciseData.endTimeZone,
                        "exercise_type" to exerciseData.exerciseType,
                    )
                )
            exerciseRepository.updateItem(exerciseData.copy(synced = true))
        }

        bloodOxygenRepository.getBOUnsynced().forEach { boData ->
            firebase.child("${firebaseInstallation}/blood-oxygen").child(boData.uid)
                .setValue(
                    mapOf(
                        "id" to boData.id,
                        "uid" to boData.uid,
                        "time" to boData.time,
                        "time_zone" to boData.timeZone,
                        "percentage" to boData.percentage,
                    )
                )
            bloodOxygenRepository.updateItem(boData.copy(synced = true))
        }
        return Result.success()
    }

}