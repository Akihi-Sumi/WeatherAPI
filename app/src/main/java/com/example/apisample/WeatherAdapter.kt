package com.example.apisample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.decode.SvgDecoder
import coil.load

class WeatherAdapter(private var weatherList: List<WeatherCardData>) :
    RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_item, parent, false)
        return WeatherViewHolder(view)
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val weather = weatherList[position]

        holder.detailWeatherTextView.text = weather.detailWeather
        holder.dateLabelTextView.text = weather.dateLabel
        holder.weatherImageView.load(weather.imageUrl) {
            decoderFactory { result, options, _ -> SvgDecoder(result.source, options) }
        }
        holder.maxTempTextView.text = weather.maxTemperature
        holder.minTempTextView.text = weather.minTemperature
        holder.windTextView.text = weather.wind
    }

    class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val detailWeatherTextView : TextView = itemView.findViewById(R.id.detailWeather)
        val dateLabelTextView: TextView = itemView.findViewById(R.id.dateLabel)
        val weatherImageView : ImageView = itemView.findViewById(R.id.weatherIcon)
        val maxTempTextView: TextView = itemView.findViewById(R.id.maxTemperature)
        val minTempTextView: TextView = itemView.findViewById(R.id.minTemperature)
        val windTextView: TextView = itemView.findViewById(R.id.detailWind)
    }
}