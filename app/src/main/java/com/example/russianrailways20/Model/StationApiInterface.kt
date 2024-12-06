package com.example.russianrailways20.Model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface StationApiInterface {
    @GET("nearest_stations")
    suspend fun getStationsList(
        @Query("apikey") apiKey2: String = "2041e738-0df6-4c62-9051-e5a46bc42a1f",
        @Query("format") fileFormat2: String = "json",
        @Query("lat") lat: String = "55.0415",
        @Query("lng") lng: String = "82.9346",
        @Query("distance") dist: String = "50",
        @Query("lang") lang2: String = "ru_RU",
        @Query("transport_types") transportType: String = "train"
    ):StationResponse

    companion object{
        private const val BASE_URL2 = "https://api.rasp.yandex.net/v3.0/"

        fun create(): StationApiInterface{
            val retrofit2 = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL2)
                .build()
            return retrofit2.create(StationApiInterface::class.java)
        }
    }
}