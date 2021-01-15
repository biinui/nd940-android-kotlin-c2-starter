package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.SEVEN_DAYS_FROM_NOW
import com.udacity.asteroidradar.TODAY

@Dao
interface AsteroidDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: DatabaseAsteroid)

    @Query("SELECT * FROM asteroids_table ORDER BY close_approach_date")
    fun getAllAsteroids(): LiveData<List<DatabaseAsteroid>>

    @Query("SELECT * FROM asteroids_table WHERE close_approach_date == :today ORDER BY close_approach_date")
    fun getAllAsteroidsToday(today: String = TODAY()): LiveData<List<DatabaseAsteroid>>

    @Query("SELECT * FROM asteroids_table WHERE close_approach_date >= :startDate AND close_approach_date <= :endDate ORDER BY close_approach_date")
    fun getAllAsteroidsThisWeek(startDate: String = TODAY(), endDate: String = SEVEN_DAYS_FROM_NOW()): LiveData<List<DatabaseAsteroid>>

    @Query("DELETE FROM asteroids_table WHERE close_approach_date < :today")
    fun deleteAsteroidsBeforeToday(today: String = TODAY())

}