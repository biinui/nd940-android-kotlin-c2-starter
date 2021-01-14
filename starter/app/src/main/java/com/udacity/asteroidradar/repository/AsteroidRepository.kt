package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.TODAY
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.DatabaseAsteroid
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.Asteroid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

private const val API_KEY = "glq0VDZWt07dtBPsgfYjslGmd400xXadacFfr6YJ"


class AsteroidRepository(private val database: AsteroidDatabase) {

    val asteroids: LiveData<List<Asteroid>> = Transformations.map(database.asteroidDao.getAllAsteroids(TODAY())) { databaseAsteroidList ->
        databaseAsteroidList.asDomainModel()
    }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val jsonStr = NasaApi.RetrofitScalarService.getAsteroids(TODAY(), API_KEY)
            val jsonObj = JSONObject(jsonStr)
            val asteroidsList = parseAsteroidsJsonResult(jsonObj)
            val asteroidsArray = asteroidsList.map { asteroid ->
                DatabaseAsteroid(
                    id = asteroid.id,
                    codename = asteroid.codename,
                    closeApproachDate = asteroid.closeApproachDate,
                    absoluteMagnitude = asteroid.absoluteMagnitude,
                    estimatedDiameter = asteroid.estimatedDiameter,
                    relativeVelocity = asteroid.relativeVelocity,
                    distanceFromEarth = asteroid.distanceFromEarth,
                    isPotentiallyHazardous = asteroid.isPotentiallyHazardous
                )
            }.toTypedArray()
            database.asteroidDao.insertAll(*asteroidsArray)
        }
    }

    suspend fun deleteOldAsteroids() {
        database.asteroidDao.deleteAsteroidsBeforeToday()
    }

}