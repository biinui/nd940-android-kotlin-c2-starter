package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.domain.PictureOfDay
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber

private const val API_KEY = "glq0VDZWt07dtBPsgfYjslGmd400xXadacFfr6YJ"

class MainViewModel : ViewModel() {

    private val _asteroidList = MutableLiveData<List<Asteroid>>()
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
            try {
                val jsonStr = NasaApi.RetrofitScalarService.getAsteroids("2021-01-13", API_KEY)
                val jsonObject = JSONObject(jsonStr)
                _asteroidList.value = parseAsteroidsJsonResult(jsonObject)
                Timber.i(asteroidList.value?.size.toString())
            } catch (e: Exception) {
                Timber.i(e.message)
            }
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