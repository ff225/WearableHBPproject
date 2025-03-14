package it.unibo.alessiociarrocchi.tesiahc.worker

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import it.unibo.alessiociarrocchi.tesiahc.RetrofitAPI
import it.unibo.alessiociarrocchi.tesiahc.WearableHBPApplication
import it.unibo.alessiociarrocchi.tesiahc.data.model.IPFSEntity
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject

class SendDataToIPFS(ctx: Context, workParams: WorkerParameters) :
    CoroutineWorker(ctx, workParams) {

    private val bloodPressureRepository =
        (ctx as WearableHBPApplication).appContainer.bloodPressureRepository
    private val heartRateRepository =
        (ctx as WearableHBPApplication).appContainer.heartRateRepository

    private val ipfsRepository = (ctx as WearableHBPApplication).appContainer.ipfsRepository

    override suspend fun doWork(): Result {

        bloodPressureRepository.getItemsUnsynced().forEach { bloodPressureData ->
            val jsonObject = JSONObject().apply {
                put("id", bloodPressureData.id)
                put("uid", bloodPressureData.uid)
                put("timestamp", bloodPressureData.time)
                put("timezone", bloodPressureData.timezone)
                put("systolic", bloodPressureData.systolic)
                put("diastolic", bloodPressureData.diastolic)
                put("description", bloodPressureData.description)
                put("bodyPosition", bloodPressureData.bodyPosition)
                put("latitude", bloodPressureData.latitude)
                put("longitude", bloodPressureData.longitude)
            }
            heartRateRepository.getItemByExternalId(bloodPressureData.uid).let { heartRateData ->
                heartRateData?.let {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        val heartRateJson = JSONObject().apply {
                            put("hrStart", heartRateData.hrStart.time)
                            put("hrEnd", heartRateData.hrEnd.time)
                            put("hrAVG", heartRateData.hrAVG)
                            put("hrMIN", heartRateData.hrMIN)
                            put("hrMAX", heartRateData.hrMAX)
                            put("hrMC", heartRateData.hrMC)
                        }
                        jsonObject.put("heartRate", heartRateJson)
                    } else {
                        TODO("VERSION.SDK_INT < TIRAMISU")
                    }
                }
            }
            val jsonString = jsonObject.toString()

            val requestBody = RequestBody.create(MediaType.parse("application/json"), jsonString)

            val jsonPart = MultipartBody.Part.createFormData(
                "file",
                "${bloodPressureData.uid}.json",
                requestBody
            )

            val response = RetrofitAPI.retrofitService.addData(jsonPart)

            Log.d(
                "POST - SendData",
                response
            )

            response.let { responseBody ->
                try {
                    val jsonObject = JSONObject(responseBody)
                    val name = jsonObject.getString("Name")
                    val hash = jsonObject.getString("Hash")

                    ipfsRepository.addData(IPFSEntity(name = name, cid = hash))
                    bloodPressureRepository.updateItem(bloodPressureData.copy(synced = true))
                } catch (e: Exception) {
                    Log.e("POST - SendData", e.message.toString())
                }
            }

        }

        return Result.success()
    }

}