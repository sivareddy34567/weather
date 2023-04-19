package com.example.login.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_table")
data class WeatherModel(
@PrimaryKey(autoGenerate = true)
val id: Int?,
val main: String?,
val description: String?
)