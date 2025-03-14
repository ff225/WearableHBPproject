package it.unibo.alessiociarrocchi.tesiahc

import okhttp3.MultipartBody
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

private const val BASE_URL =
    "http://192.168.144.147:5001/"

private val retrofit = Retrofit.Builder().addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()


interface ApiIpfs {
    @Multipart
    @POST("/api/v0/add")
    suspend fun addData(@Part file: MultipartBody.Part): String

    @POST("/api/v0/cat")
    suspend fun getData(@Query("arg") arg: String): String
}

object RetrofitAPI {
    val retrofitService: ApiIpfs by lazy {
        retrofit.create(ApiIpfs::class.java)
    }
}
