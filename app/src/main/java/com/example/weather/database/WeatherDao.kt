package com.example.login.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) //if some data is same/conflict, it'll be replace with new data.
    suspend fun insertNote(note: WeatherModel)




    @Query("SELECT * FROM weather_table")
    fun getAllWeatherData(): LiveData<List<WeatherModel>>
    // why not use suspend ? because Room does not support LiveData with suspended functions.
    // LiveData already works on a background thread and should be used directly without using coroutines

}