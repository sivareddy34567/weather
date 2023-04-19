package com.example.login.network

import com.example.login.model.LoginRequest
import com.example.login.model.WeatherResponse
import retrofit2.Call
import retrofit2.http.*
interface ApiInterface {

    @GET("data/2.5/weather")
    fun getWeatherResponse(@Query("q") cityName: String,
                          @Query("appid") appid: String)

    : Call< WeatherResponse>
}