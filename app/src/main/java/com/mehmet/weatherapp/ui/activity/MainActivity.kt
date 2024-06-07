package com.mehmet.weatherapp.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.matteobattilana.weather.PrecipType
import com.mehmet.weatherapp.R
import com.mehmet.weatherapp.adapter.ForecastAdapter
import com.mehmet.weatherapp.databinding.ActivityMainBinding
import com.mehmet.weatherapp.models.CurrentResponse
import com.mehmet.weatherapp.models.ForecastResponse
import com.mehmet.weatherapp.ui.viewmodel.WeatherViewModel
import eightbitlab.com.blurview.RenderScriptBlur
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val weatherViewModel : WeatherViewModel by viewModels()
    private val calendar by lazy { Calendar.getInstance() }
    private val forecastAdapter by lazy { ForecastAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.apply {
            val lat = 38.42
            val lon = 27.14
            val name = "Izmir"

            cityText.text = name
            progressBar.visibility = View.VISIBLE
            weatherViewModel.loadCurrentWeather(lat, lon).enqueue(object :Callback<CurrentResponse>{
                @SuppressLint("SetTextI18n")
                override fun onResponse(p0: Call<CurrentResponse>, response: Response<CurrentResponse>) {
                   if(response.isSuccessful){
                       val data = response.body()
                       progressBar.visibility = View.GONE
                       dailyLayout.visibility = View.VISIBLE
                       data?.let {
                           statusText.text = it.weather?.get(0)?.main ?: "-"
                           windText.text= it.wind?.speed?.let { Math.round(it).toString() } + "Km"
                           currenText.text= it.main?.temp?.let { Math.round(it).toString() } + "°"
                           maxTempText.text= it.main?.tempMax?.let { Math.round(it).toString() } + "°"
                           minTempText.text= it.main?.tempMin?.let { Math.round(it).toString() } + "°"
                           humidityText.text = it.main?.humidity?.let { Math.round(it.toDouble()).toString() } + "%"


                           val drawable = if(isNight()) R.drawable.night_bg
                           else {
                               setDynamicallyWallpaper(it.weather?.get(0)?.icon?:"-")
                           }

                           backgroundImage.setImageResource(drawable)
                           setEffectRainSnow(it.weather?.get(0)?.icon ?: "-")

                       }
                   }
                }

                override fun onFailure(p0: Call<CurrentResponse>, p1: Throwable) {
                    Toast.makeText(this@MainActivity, p1.toString(),Toast.LENGTH_LONG).show()
                }

            })


            //settings
            var radius = 10f
            val decorView = window.decorView
            val rootView: ViewGroup? = (decorView.findViewById(android.R.id.content))
            val windowBackground = decorView.background

            rootView?.let {
                blueView.setupWith(it,RenderScriptBlur(this@MainActivity))
                    .setFrameClearDrawable(windowBackground)
                    .setBlurRadius(radius)
                blueView.outlineProvider = ViewOutlineProvider.BACKGROUND
                blueView.clipToOutline = true
            }

            // forecast temp

            weatherViewModel.loadForecastWeather(lat,lon).enqueue(object : Callback<ForecastResponse>{
                override fun onResponse(
                    p0: Call<ForecastResponse>,
                    response: Response<ForecastResponse>
                ) {
                    if (response.isSuccessful){
                        val data = response.body()
                        blueView.visibility = View.VISIBLE
                        data?.let {
                            forecastAdapter.differ.submitList(it.list)
                            forecastRecyclerView.apply {
                                layoutManager = LinearLayoutManager(this@MainActivity,
                                    LinearLayoutManager.HORIZONTAL,false)
                                adapter = forecastAdapter
                            }

                        }
                    }
                }

                override fun onFailure(p0: Call<ForecastResponse>, p1: Throwable) {
                    Toast.makeText(this@MainActivity, p1.toString(),Toast.LENGTH_LONG).show()
                }
            })


        }
    }

    private fun isNight(): Boolean {
        return calendar.get(Calendar.HOUR_OF_DAY) >= 18
    }

    private fun setDynamicallyWallpaper(icon:String):Int {
        return when(icon.dropLast(1)){
            "01" -> {
                initWeatherView(PrecipType.CLEAR)
                R.drawable.sunny_bg

            }
            "02","03","04"-> {
                initWeatherView(PrecipType.CLEAR)
                R.drawable.cloudy_bg

            }
            "09","10","11" -> {
                initWeatherView(PrecipType.RAIN)
                R.drawable.rainy_bg

            }
            "13" -> {
                initWeatherView(PrecipType.SNOW)
                R.drawable.snow_bg

            }
            "50" -> {
                initWeatherView(PrecipType.CLEAR)
                R.drawable.haze_bg

            }

            else ->0
        }
    }


    private fun setEffectRainSnow(icon:String) {
         when(icon.dropLast(1)){
            "01" -> {
                initWeatherView(PrecipType.CLEAR)
                R.drawable.snow_bg

            }
            "02","03","04"-> {
                initWeatherView(PrecipType.CLEAR)
                R.drawable.cloudy_bg

            }
            "09","10","11" -> {
                initWeatherView(PrecipType.RAIN)
                R.drawable.rainy_bg

            }
            "13" -> {
                initWeatherView(PrecipType.SNOW)
                R.drawable.snow_bg

            }
            "50" -> {
                initWeatherView(PrecipType.CLEAR)
                R.drawable.haze_bg

            }

        }
    }

    private fun initWeatherView(type: PrecipType) {
        binding.weatherView.apply {
            setWeatherData(type)
            angle=20
            emissionRate=100.0f
        }

    }


}
