package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.Asteroid

class MainViewModel : ViewModel() {

    private var _asteroidList = MutableLiveData<List<Asteroid>>()
    val asteroidList: LiveData<List<Asteroid>>
        get() = _asteroidList

    private var _navigateToSelectedAsteroid = MutableLiveData<Asteroid>()
    val navigateToSelectedAsteroid: LiveData<Asteroid>
        get() = _navigateToSelectedAsteroid

    init {
        populateWithDummyData()
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