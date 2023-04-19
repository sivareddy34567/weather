package com.example.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.login.repository.WeatherRepository


class WeatherViewModelFactory(
     val repository: WeatherRepository
) : ViewModelProvider.NewInstanceFactory() {

     override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        try {
            var constructor = modelClass.getDeclaredConstructor(WeatherRepository::class.java)
            return constructor.newInstance(repository)
        } catch (e: Exception) {
            Log.d("",e.message.toString())
        }
        return super.create(modelClass)
    }
}