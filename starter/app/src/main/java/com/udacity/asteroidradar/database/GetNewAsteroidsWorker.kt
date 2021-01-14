package com.udacity.asteroidradar.database

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.repository.AsteroidRepository
import java.lang.Exception

class GetNewAsteroidsWorker(applicationContext: Context, workerParameters: WorkerParameters)
    : CoroutineWorker(applicationContext, workerParameters) {

    companion object {
        const val WORK_NAME = "GetAsteroidsWorker"
    }

    override suspend fun doWork(): Result {
        val database = AsteroidDatabase.getInstance(applicationContext)
        val repository = AsteroidRepository(database)

        return try {
            repository.refreshAsteroids()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

}