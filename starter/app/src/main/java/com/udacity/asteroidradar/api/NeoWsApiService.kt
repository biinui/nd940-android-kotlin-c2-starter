package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.nasa.gov/"

private val retrofitScalar = Retrofit.Builder()
        .addConverterFactory(ScalarsConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
private val moshiConverterFactory = MoshiConverterFactory.create(moshi)
private val retrofitJson = Retrofit.Builder()
    .addConverterFactory(moshiConverterFactory)
    .baseUrl(BASE_URL)
    .build()

interface NeoWsApiService {
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids( @Query("start_date")  startDate: String
                            , @Query("api_key")     apiKey   : String
                            ): String

    @GET("planetary/apod")
    suspend fun getImageOfTheDay(@Query("api_key") apiKey: String): JSONObject
}

object NeoWsApi {
    val retrofitScalarService : NeoWsApiService by lazy {
        retrofitScalar.create(NeoWsApiService::class.java)
    }
    val retrofitJsonService : NeoWsApiService by lazy {
        retrofitJson.create(NeoWsApiService::class.java)
    }
}