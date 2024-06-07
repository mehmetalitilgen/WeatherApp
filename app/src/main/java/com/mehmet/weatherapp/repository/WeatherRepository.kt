package com.mehmet.weatherapp.repository

import com.mehmet.weatherapp.api.RetrofitInstance
import com.mehmet.weatherapp.api.WeatherAPI
import com.mehmet.weatherapp.util.Constants.Companion.API_KEY
import com.mehmet.weatherapp.util.Constants.Companion.UNIT
import kotlin.math.ln

class WeatherRepository(private val retrofit: WeatherAPI) {

    fun getCurrentWeather(lat: Double, lon: Double) =
        retrofit.getCurrentWeather(lat, lon, UNIT, API_KEY)

    fun getForecastWeather(lat: Double, lon: Double) =
        retrofit.getForecastWeather(lat,lon,UNIT, API_KEY)

}