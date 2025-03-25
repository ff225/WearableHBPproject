package it.unibo.alessiociarrocchi.tesiahc.worker

import android.content.Context
import android.os.Build
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import it.unibo.alessiociarrocchi.tesiahc.BuildConfig
import it.unibo.alessiociarrocchi.tesiahc.RetrofitAPI
import it.unibo.alessiociarrocchi.tesiahc.WearableHBPApplication
import it.unibo.alessiociarrocchi.tesiahc.data.model.IPFSEntity
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.awaitResponse

class SendDataToIPFS(ctx: Context, workParams: WorkerParameters) :
    CoroutineWorker(ctx, workParams) {

    private val bloodPressureRepository =
        (ctx as WearableHBPApplication).appContainer.bloodPressureRepository
    private val heartRateRepository =
        (ctx as WearableHBPApplication).appContainer.heartRateRepository

    private val ipfsRepository = (ctx as WearableHBPApplication).appContainer.ipfsRepository

    override suspend fun doWork(): Result {

        println(RetrofitAPI.retrofitService.testAuthentication(" Bearer ${BuildConfig.PINATA_TOKEN}"))

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


            val response = RetrofitAPI.retrofitService.pinJsonToIPFS(
                "Bearer ${BuildConfig.PINATA_TOKEN}",
                RequestBody.create(MediaType.parse("application/json"), JSONObject().apply {
                    put("pinataOptions", JSONObject().apply {
                        put("cidVersion", 1)
                    })
                    put("pinataMetadata", JSONObject().apply {
                        put("name", "${bloodPressureData.uid}.json")
                    })
                    put("pinataContent", jsonObject)
                }.toString())
            )


            val result = response.awaitResponse()

            if (result.isSuccessful)
                result.body()?.let {
                    val jsonObject = JSONObject(it.string())
                    val hash = jsonObject.getString("IpfsHash")
                    ipfsRepository.addData(IPFSEntity(name = bloodPressureData.uid, cid = hash))
                    bloodPressureRepository.updateItem(bloodPressureData.copy(synced = true))
                }
        }

        return Result.success()
    }

}