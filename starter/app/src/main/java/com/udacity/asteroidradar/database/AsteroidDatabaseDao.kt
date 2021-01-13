package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface AsteroidDatabaseDao {

    @Insert
    fun insert(asteroid: DatabaseAsteroid)

    @Update
    fun update(asteroid: DatabaseAsteroid)

    @Query("SELECT * FROM asteroids_table ORDER BY close_approach_date")
    fun getAllAsteroids(): LiveData<List<DatabaseAsteroid>>

}