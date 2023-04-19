package com.example.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.login.BaseResponse
import com.example.login.database.WeatherModel
import com.example.login.model.WeatherRequest
import com.example.login.model.WeatherResponse
import com.example.login.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class WeatherViewModel @Inject constructor(  val repository: WeatherRepository) :ViewModel(){
   private var _weather : MutableLiveData<BaseResponse<WeatherResponse>> ?= null

    fun weather(cityName: String, appid: String,): LiveData<BaseResponse<WeatherResponse>>? {
        WeatherRequest(cityName,appid)
        _weather= repository.getWeatherApiCall(cityName,appid)
        return _weather
    }


}