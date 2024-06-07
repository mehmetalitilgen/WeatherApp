package com.mehmet.weatherapp.adapter

import android.icu.util.Calendar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mehmet.weatherapp.R
import com.mehmet.weatherapp.databinding.ForecastItemBinding
import com.mehmet.weatherapp.models.ForecastResponse
import java.text.SimpleDateFormat

class ForecastAdapter: RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

    private lateinit var binding: ForecastItemBinding

    inner class ForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private lateinit var dayNameText : TextView
    private lateinit var hourText: TextView
    private lateinit var tempText: TextView
    private lateinit var image: ImageView



    private val differCallback = object :DiffUtil.ItemCallback<ForecastResponse.data>() {
        override fun areItemsTheSame(
            oldItem: ForecastResponse.data,
            newItem: ForecastResponse.data
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ForecastResponse.data,
            newItem: ForecastResponse.data
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,differCallback)




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        return ForecastViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.forecast_item,parent,false))
    }

    override fun getItemCount(): Int {
       return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val binding = ForecastItemBinding.bind(holder.itemView)
        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(differ.currentList[position].dtTxt.toString())
        val calendar = Calendar.getInstance()
        calendar.time = date

        val dayOfWeekName = when(calendar.get(Calendar.DAY_OF_WEEK)) {
            1 -> "Sun"
            2 -> "Mon"
            3 -> "Tue"
            4 -> "Wed"
            5 -> "Thu"
            6 -> "Fri"
            7 -> "Sat"
            else -> "-"
        }

        binding.dayNameTxt.text = dayOfWeekName
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val amPm = if (hour < 12) "am" else "pm"
        val hour12 = calendar.get(Calendar.HOUR)
        binding.hourTxt.text = hour12.toString() + amPm
        binding.tempTxt.text = differ.currentList[position].main?.temp?.let { Math.round(it) }.toString() + "°"

        val icon = when(differ.currentList[position].weather?.get(0)?.icon.toString()){
            "01d", "0n" -> "sunny"
            "02d", "02n" -> "cloudy_sunny"
            "3d", "03n" -> "cloudy_sunny"
            "4d", "04n" -> "cloudy"
            "9d", "09n" -> "rainy"
            "10d", "10n" -> "rainy"
            "11d", "11n" -> "storm"
            "13d", "13n" -> "snowy"
            "50d", "50n" -> "windy"
            else -> "sunny"

        }

        val drawableResourceId : Int = binding.root.resources.getIdentifier(icon,"drawable",binding.root.context.packageName)

        Glide.with(binding.root.context)
            .load(drawableResourceId)
            .into(binding.pic)

    }

}