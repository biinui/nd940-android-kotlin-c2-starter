package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.getDateToday
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.DatabaseAsteroid
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.Asteroid
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidRepository(private val database: AsteroidDatabase) {

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val jsonStr = NasaApi.RetrofitScalarService.getAsteroids(getDateToday(), API_KEY)
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

    fun showAsteroidsToday(): LiveData<List<Asteroid>> {
        return Transformations.map(database.asteroidDao.getAllAsteroidsToday()) { databaseAsteroidList ->
            databaseAsteroidList.asDomainModel()
        }
    }

    fun showAsteroidsThisWeek(): LiveData<List<Asteroid>> {
        return Transformations.map(database.asteroidDao.getAllAsteroidsThisWeek()) { databaseAsteroidList ->
            databaseAsteroidList.asDomainModel()
        }
    }

    fun showAsteroidsSaved(): LiveData<List<Asteroid>> {
        return Transformations.map(database.asteroidDao.getAllAsteroids()) { databaseAsteroidList ->
            databaseAsteroidList.asDomainModel()
        }
    }

}