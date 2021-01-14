package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch
import timber.log.Timber

private const val API_KEY = "glq0VDZWt07dtBPsgfYjslGmd400xXadacFfr6YJ"

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AsteroidDatabase.getInstance(application)
    private val repository = AsteroidRepository(database)

    private val _asteroidList = repository.asteroids
    val asteroidList: LiveData<List<Asteroid>>
        get() = _asteroidList

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid>()
    val navigateToSelectedAsteroid: LiveData<Asteroid>
        get() = _navigateToSelectedAsteroid

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    init {
        getAsteroids()
        getPictureOfDay()
    }

    private fun getAsteroids() {
        viewModelScope.launch {
            repository.refreshAsteroids()
        }
    }

    private fun getPictureOfDay() {
        viewModelScope.launch {
            try {
                _pictureOfDay.value = NasaApi.RetrofitJsonService.getImageOfTheDay(API_KEY)
                Timber.i(_pictureOfDay.value?.url)
            } catch (e: Exception) {
                Timber.i(e.message)
            }
        }
    }

    fun navigateToSelectedAsteroid(selectedAsteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = selectedAsteroid
    }

    fun navigateToSelectedAsteroidDone() {
        _navigateToSelectedAsteroid.value = null
    }
}