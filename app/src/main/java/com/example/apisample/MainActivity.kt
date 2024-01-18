package com.example.apisample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.apisample.databinding.ActivityMainBinding
import com.example.apisample.model.Weather
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var recyclerView: RecyclerView
    private lateinit var weatherList: ArrayList<com.example.apisample.Weather>
    private lateinit var weatherAdapter: WeatherAdapter

    private val client = OkHttpClient.Builder()
        .connectTimeout(10000, TimeUnit.MILLISECONDS)
        .readTimeout(10000.toLong(), TimeUnit.MILLISECONDS)
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        init()

        binding.button.setOnClickListener {
            val cityNumber = binding.editTextNumber.text
            val requestUrl = "https://weather.tsukumijima.net/api/forecast/city/$cityNumber"

            Thread {
                val handler = Handler(Looper.getMainLooper())
                val responseBody = startGetRequest(requestUrl)

                if (responseBody != null) {
                    val weather = Json.decodeFromString<Weather>(responseBody)

                    val weatherTitle = weather.title
                    val weatherText = weather.forecasts?.get(0)?.detail?.weather
                    val weatherIcon = weather.forecasts?.get(0)?.image?.url
                    val weatherTomorrowText = weather.forecasts?.get(1)?.detail?.weather
                    val weatherTomorrowIcon = weather.forecasts?.get(1)?.image?.url
                    val weatherDatText = weather.forecasts?.get(1)?.detail?.weather
                    val weatherDatIcon = weather.forecasts?.get(1)?.image?.url

                    if (weatherIcon != null && weatherTomorrowIcon != null && weatherDatIcon != null) {
                       Utils().fetchSVG(this, weatherIcon, recyclerView.findViewById(R.id.weatherIcon))
                    }

                    handler.post {
                        recyclerView.findViewById<TextView>(R.id.weatherTitle).text = weatherText
                    }
                }
            }.start()
        }
    }

    @Throws(IOException::class)
    private fun startGetRequest(url: String): String? {
        val request: Request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).execute().use { response ->
            return response.body?.string()
        }
    }

    private fun init() {
        recyclerView = findViewById(R.id.recycleView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        weatherList = ArrayList()

        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)
        addDataToList()

        weatherAdapter = WeatherAdapter(weatherList)
        recyclerView.adapter = weatherAdapter
    }

    private fun addDataToList() {
        weatherList.add(Weather("晴れ"))
        weatherList.add(Weather("曇り"))
        weatherList.add(Weather("雨"))
    }
}