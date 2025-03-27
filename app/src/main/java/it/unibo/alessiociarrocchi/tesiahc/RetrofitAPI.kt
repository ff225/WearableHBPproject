package it.unibo.alessiociarrocchi.tesiahc

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL =
    BuildConfig.PINATA_GATEWAY

private val retrofit = Retrofit.Builder().addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl("https://api.pinata.cloud/")
    .build()

private val retrofitIPFS = Retrofit.Builder().addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface ApiIpfs {
    @Multipart
    @POST("/api/v0/add")
    suspend fun addData(@Part file: MultipartBody.Part): String

    @POST("/api/v0/cat")
    suspend fun getData(@Query("arg") arg: String): String

    @GET("data/testAuthentication")
    suspend fun testAuthentication(
        @Header("Authorization") authHeader: String
    ): String

    @POST("pinning/pinJSONToIPFS")
    fun pinJsonToIPFS(
        @Header("Authorization") authHeader: String,
        @Body jsonBody: RequestBody
    ): Call<ResponseBody>

    // GET data from IPFS
    @GET("/ipfs/{cid}")
    fun retrieveData(@Path("cid") cid: String): Call<ResponseBody>
}

object RetrofitAPI {
    val retrofitService: ApiIpfs by lazy {
        retrofit.create(ApiIpfs::class.java)
    }

    val retrofitRetrieveData: ApiIpfs by lazy {
        retrofitIPFS.create(ApiIpfs::class.java)
    }
}
