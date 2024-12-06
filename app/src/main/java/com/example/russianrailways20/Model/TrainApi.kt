package com.example.russianrailways20.Model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface TrainApiInterface {
    @GET("search")
    suspend fun getTrainSchedule(
        @Query("apikey") apiKey: String = "2041e738-0df6-4c62-9051-e5a46bc42a1f",
        @Query("format") fileFormat: String = "json",
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("page") page: Int = 1,
        @Query("date") dateOfReq: String
    ):TrainResponse

    companion object{
        private const val BASE_URL = "https://api.rasp.yandex.net/v3.0/"

        fun create(): TrainApiInterface{
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(TrainApiInterface::class.java)
        }
    }
}