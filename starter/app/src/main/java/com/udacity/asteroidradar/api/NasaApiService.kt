package com.udacity.asteroidradar.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants.BASE_URL
import com.udacity.asteroidradar.domain.PictureOfDay
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val retrofitScalar = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
private val moshiConverterFactory = MoshiConverterFactory.create(moshi)
private val retrofitJson = Retrofit.Builder()
    .addConverterFactory(moshiConverterFactory)
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface NasaApiService {
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids( @Query("start_date")  startDate: String
                            , @Query("api_key")     apiKey   : String
                            ): String

    @GET("planetary/apod")
    suspend fun getImageOfTheDay(@Query("api_key") apiKey: String): PictureOfDay
}

object NasaApi {
    val RetrofitScalarService : NasaApiService by lazy {
        retrofitScalar.create(NasaApiService::class.java)
    }
    val RetrofitJsonService : NasaApiService by lazy {
        retrofitJson.create(NasaApiService::class.java)
    }
}