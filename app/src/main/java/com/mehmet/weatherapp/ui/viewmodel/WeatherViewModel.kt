package com.mehmet.weatherapp.ui.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mehmet.weatherapp.api.RetrofitInstance
import com.mehmet.weatherapp.api.WeatherAPI

import com.mehmet.weatherapp.repository.WeatherRepository
import kotlinx.coroutines.launch
import java.io.IOException


class WeatherViewModel(private val weatherRepository: WeatherRepository) : ViewModel() {

    constructor() : this(WeatherRepository(RetrofitInstance().getClient().create(WeatherAPI::class.java)))

    fun loadCurrentWeather(lat:Double,lon: Double) =
        weatherRepository.getCurrentWeather(lat,lon)

    fun loadForecastWeather(lat: Double,lon: Double) =
        weatherRepository.getForecastWeather(lat,lon)

}