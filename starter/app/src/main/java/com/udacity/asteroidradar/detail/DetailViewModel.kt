package com.udacity.asteroidradar.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.domain.Asteroid

class DetailViewModel(asteroid: Asteroid, application: Application) : AndroidViewModel(application) {

    private val _selectedAsteroid = MutableLiveData<Asteroid>()
    private val selectedAsteroid: LiveData<Asteroid>
        get() = _selectedAsteroid

    val asteroidTypeContentDescription = Transformations.map(selectedAsteroid) {
            application.applicationContext.getString(
                when(it.isPotentiallyHazardous) {
                    true  -> R.string.potentially_hazardous_asteroid_image
                    false -> R.string.not_hazardous_asteroid_image
                })
    }

}