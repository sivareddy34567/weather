package com.example.login.model

data class WeatherResponse(
    var base:String,
    var message:String,
    var weather:List<WeatherData>,
    var sys:WeatherCountryData)

