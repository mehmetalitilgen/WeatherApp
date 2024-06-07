package com.mehmet.weatherapp.api


import com.mehmet.weatherapp.models.CurrentResponse
import com.mehmet.weatherapp.models.ForecastResponse
import com.mehmet.weatherapp.util.Constants.Companion.API_KEY
import com.mehmet.weatherapp.util.Constants.Companion.UNIT
import retrofit2.Call

import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherAPI {
    @GET("data/2.5/weather")
    fun getCurrentWeather(
        @Query("lat")
        lat: Double,
        @Query("lon")
        lon: Double,
        @Query("units")
        units: String ,
        @Query("appid")
        appid: String = API_KEY,
    ): Call<CurrentResponse>


    @GET("data/2.5/forecast")
    fun getForecastWeather(
        @Query("lat")
        lat: Double,
        @Query("lon")
        lon: Double,
        @Query("units")
        units: String,
        @Query("appid")
        appid: String = API_KEY

    ): Call<ForecastResponse>

}