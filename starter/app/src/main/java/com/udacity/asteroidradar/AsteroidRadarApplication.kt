package com.udacity.asteroidradar

import android.app.Application
import androidx.work.*
import com.udacity.asteroidradar.database.DeleteOldAsteroidsWorker
import com.udacity.asteroidradar.database.GetNewAsteroidsWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

class AsteroidRadarApplication : Application() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)
    private fun delayedInit() = applicationScope.launch {
        setupRecurringWork()
    }

    private fun setupRecurringWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresCharging(true)
            .build()

        val getNewAsteroidsRequest = PeriodicWorkRequestBuilder<GetNewAsteroidsWorker>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        val deleteOldAsteroidsRequest = PeriodicWorkRequestBuilder<GetNewAsteroidsWorker>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            GetNewAsteroidsWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            getNewAsteroidsRequest
        )

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            DeleteOldAsteroidsWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            deleteOldAsteroidsRequest
        )
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        delayedInit()
    }
}