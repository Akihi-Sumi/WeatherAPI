package com.example.apisample

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WeatherAdapter(private val weatherList: List<Weather>) :
    RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {
    class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val weatherImageView : ImageView = itemView.findViewById(R.id.weatherIcon)
        val weatherTitle : TextView = itemView.findViewById(R.id.weatherTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_item, parent, false)
        return WeatherViewHolder(view)
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val weather = weatherList[position]
        // holder.weatherImageView.setBackgroundResource(weather.weatherImage)
        holder.weatherTitle.text = weather.weatherTitle
    }
}