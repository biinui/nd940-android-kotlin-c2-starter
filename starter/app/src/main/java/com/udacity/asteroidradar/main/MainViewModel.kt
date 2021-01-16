package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.isNetworkAvailable
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch
import timber.log.Timber

enum class AsteroidFilter(val filter: String) { SHOW_WEEK("week"), SHOW_TODAY("today"), SHOW_SAVED("saved") }

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AsteroidDatabase.getInstance(application)
    private val repository = AsteroidRepository(database)

    private val _asteroidFilter = MutableLiveData<AsteroidFilter>(AsteroidFilter.SHOW_WEEK)
    private val _asteroidList = _asteroidFilter.switchMap { filter ->
        when (filter) {
            AsteroidFilter.SHOW_WEEK    -> repository.showAsteroidsThisWeek()
            AsteroidFilter.SHOW_TODAY   -> repository.showAsteroidsToday()
            else                        -> repository.showAsteroidsSaved()
        }
    }

    val asteroidList: LiveData<List<Asteroid>>
        get() = _asteroidList

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid>()
    val navigateToSelectedAsteroid: LiveData<Asteroid>
        get() = _navigateToSelectedAsteroid

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    init {
        if (isNetworkAvailable(application)) {
            getAsteroids()
            getPictureOfDay()
        }
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
                Timber.d(_pictureOfDay.value?.url)
            } catch (e: Exception) {
                Timber.e(e.message)
            }
        }
    }

    fun navigateToSelectedAsteroid(selectedAsteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = selectedAsteroid
    }

    fun navigateToSelectedAsteroidDone() {
        _navigateToSelectedAsteroid.value = null
    }

    fun onUpdateFilterToToday() {
        _asteroidFilter.value = AsteroidFilter.SHOW_TODAY
    }

    fun onUpdateFilterToThisWeek() {
        _asteroidFilter.value = AsteroidFilter.SHOW_WEEK
    }

    fun onUpdateFilterToSaved() {
        _asteroidFilter.value = AsteroidFilter.SHOW_SAVED
    }

    val pictureOfDayContentDescription = Transformations.map(pictureOfDay) {
        application.applicationContext.getString(R.string.nasa_picture_of_day_content_description_format, pictureOfDay.value?.title)
    }
}