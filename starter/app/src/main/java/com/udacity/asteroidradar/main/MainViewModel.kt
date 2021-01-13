package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
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

    init {
        getAsteroids()
        getImageOfTheDay()
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

    private fun getImageOfTheDay() {
        viewModelScope.launch {
            try {
                val jsonObject = NasaApi.RetrofitJsonService.getImageOfTheDay(API_KEY)
                Timber.i(jsonObject.toString())
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