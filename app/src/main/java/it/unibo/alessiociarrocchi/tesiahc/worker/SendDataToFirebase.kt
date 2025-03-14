package it.unibo.alessiociarrocchi.tesiahc.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.google.firebase.initialize
import com.google.firebase.installations.FirebaseInstallations
import it.unibo.alessiociarrocchi.tesiahc.RetrofitAPI
import it.unibo.alessiociarrocchi.tesiahc.WearableHBPApplication
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject

class SendDataToFirebase(ctx: Context, workParams: WorkerParameters) :
    CoroutineWorker(ctx, workParams) {

    private val firebase =
        Firebase.database("https://wearablehbpproject-default-rtdb.europe-west1.firebasedatabase.app/").reference
    private val bloodPressureRepository =
        (ctx as WearableHBPApplication).appContainer.bloodPressureRepository
    private val heartRateRepository =
        (ctx as WearableHBPApplication).appContainer.heartRateRepository

    override suspend fun doWork(): Result {

        Log.d(
            "GET- SendData",
            RetrofitAPI.retrofitService.getData("Qmc8v7rbuejvAPtcN8V2AVZFqL9hsWNShAhPxa9j1YEo28")
        )
        val jsonObject = JSONObject().apply {
            put("chiave1", "valore1")
            put("chiave2", "valore2")
        }
        val jsonString = jsonObject.toString()

        // Creazione del RequestBody direttamente dalla stringa JSON
        val requestBody = RequestBody.create(MediaType.parse("application/json"), jsonString)

        // Creazione della parte Multipart senza file fisico
        val jsonPart = MultipartBody.Part.createFormData("file", "temp.json", requestBody)

        // Invio del file
        Log.d(
            "POST - SendData",
            RetrofitAPI.retrofitService.addData(jsonPart)
        )

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