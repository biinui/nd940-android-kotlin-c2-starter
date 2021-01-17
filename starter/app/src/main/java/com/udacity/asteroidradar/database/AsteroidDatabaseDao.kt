package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.getDateSevenDaysFromToday
import com.udacity.asteroidradar.getDateToday

@Dao
interface AsteroidDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: DatabaseAsteroid)

    @Query("SELECT * FROM asteroids_table ORDER BY close_approach_date")
    fun getAllAsteroids(): LiveData<List<DatabaseAsteroid>>

    @Query("SELECT * FROM asteroids_table WHERE close_approach_date == :today ORDER BY close_approach_date")
    fun getAllAsteroidsToday(today: String = getDateToday()): LiveData<List<DatabaseAsteroid>>

    @Query("SELECT * FROM asteroids_table WHERE close_approach_date >= :startDate AND close_approach_date <= :endDate ORDER BY close_approach_date")
    fun getAllAsteroidsThisWeek(startDate: String = getDateToday(), endDate: String = getDateSevenDaysFromToday()): LiveData<List<DatabaseAsteroid>>

    @Query("DELETE FROM asteroids_table WHERE close_approach_date < :today")
    fun deleteAsteroidsBeforeToday(today: String = getDateToday())

}