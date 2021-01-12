package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.NeoWsApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.launch
import timber.log.Timber

private const val API_KEY = " glq0VDZWt07dtBPsgfYjslGmd400xXadacFfr6YJ"

class MainViewModel : ViewModel() {

    private var _asteroidList = MutableLiveData<List<Asteroid>>()
    val asteroidList: LiveData<List<Asteroid>>
        get() = _asteroidList

    private var _navigateToSelectedAsteroid = MutableLiveData<Asteroid>()
    val navigateToSelectedAsteroid: LiveData<Asteroid>
        get() = _navigateToSelectedAsteroid

    init {
        getAsteroids()
    }

    private fun getAsteroids() {
        viewModelScope.launch {
            try {
                val jsonResult = NeoWsApi.retrofitService.getAsteroids("2021-02-01","2021-02-02", API_KEY)
                _asteroidList.value = parseAsteroidsJsonResult(jsonResult)
            } catch (e: Exception) {
                _asteroidList.value = ArrayList()
            }
        }
    }

    fun navigateToSelectedAsteroid(selectedAsteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = selectedAsteroid
    }

    fun navigateToSelectedAsteroidDone() {
        _navigateToSelectedAsteroid.value = null
    }

    private fun populateWithDummyData() {
        val asteroid1 = Asteroid( 3726710
                                , "(2015 RC)"
                                , "2015-09-08"
                                , 24.3
                                , 0.0820427065
                                , 19.4850295284
                                , 0.0269230459
                                , false )

        val dummyList = listOf<Asteroid>(asteroid1)
        _asteroidList.value = dummyList
    }

}