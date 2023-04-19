package com.example.login.repository

import androidx.lifecycle.MutableLiveData
import com.example.login.BaseResponse
import com.example.login.database.WeatherDatabase
import com.example.login.model.WeatherResponse
import com.example.login.network.RetrofitClient
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeatherRepository(var weatherDatabase: WeatherDatabase) {
    val serviceLoginResponse = MutableLiveData<BaseResponse<WeatherResponse>>()
    fun getWeatherApiCall(cityName: String, appid: String): MutableLiveData<BaseResponse<WeatherResponse>> {
        val call = RetrofitClient.apiInterface.getWeatherResponse(cityName,appid)
        call.enqueue(object: Callback<WeatherResponse> {
            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                print(t.message)
            }
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                if (response?.code() == 200) {
                    serviceLoginResponse.value =BaseResponse.Success(response.body()!!)
                }else{
              val errorObj=JSONObject(response.errorBody()!!.charStream().readText())
                   val mesaage= errorObj.getString("message")
                    serviceLoginResponse.value = BaseResponse.Error(errorObj.getString("message"))

                }

            }
        })

        return serviceLoginResponse
    }

}