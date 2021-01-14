package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.TODAY

@Dao
interface AsteroidDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: DatabaseAsteroid)

    @Query("SELECT * FROM asteroids_table WHERE close_approach_date >= :startDate ORDER BY close_approach_date")
    fun getAllAsteroids(startDate: String): LiveData<List<DatabaseAsteroid>>

    @Query("DELETE FROM asteroids_table WHERE close_approach_date < :today")
    fun deleteAsteroidsBeforeToday(today: String = TODAY())

}