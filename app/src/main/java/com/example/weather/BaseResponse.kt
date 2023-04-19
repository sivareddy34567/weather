package com.example.login

import com.example.login.model.ErrorResponse
import com.example.login.model.WeatherResponse

sealed class BaseResponse<T>(val data:T? = null, val message: String?=null){
    class Success<T>(data: T) : BaseResponse<T>(data)
    class Error<T>(message: String?,data: T?=null) : BaseResponse<T>(data,message)


}
