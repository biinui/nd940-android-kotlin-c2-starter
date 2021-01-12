package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Asteroid
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.nasa.gov/neo/rest/v1/"

private val moshi = Moshi.Builder()
                         .add(KotlinJsonAdapterFactory())
                         .build()
private val moshiConverterFactory = MoshiConverterFactory.create(moshi)
private val retrofit = Retrofit.Builder()
        .addConverterFactory(moshiConverterFactory)
        .baseUrl(BASE_URL)
        .build()

interface NeoWsApiService {
    @GET("feed")
    fun getAsteroids( @Query("start_date")  startDate: String
                    , @Query("end_date")    endDate: String
                    , @Query("api_key")     apiKey: String
                    ): JSONObject
}

object NeoWsApi {
    val retrofitService : NeoWsApiService by lazy {
        retrofit.create(NeoWsApiService::class.java)
    }
}