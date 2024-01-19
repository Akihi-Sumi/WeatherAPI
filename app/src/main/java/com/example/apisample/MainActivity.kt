package com.example.apisample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.apisample.databinding.ActivityMainBinding
import com.example.apisample.model.Weather
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var recyclerView: RecyclerView
    private lateinit var weatherList: ArrayList<WeatherCardData>
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

        setHorizontalWeatherViewAdapter()

        binding.button.setOnClickListener {

            setWeathersDataToUI()
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

    private fun setHorizontalWeatherViewAdapter() {
        recyclerView = findViewById(R.id.recycleView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        weatherList = ArrayList()

        binding.recycleView.onFlingListener = null
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)

        addDummyDataToList()

        weatherAdapter = WeatherAdapter(weatherList)
        binding.recycleView.adapter = weatherAdapter
    }

    private fun setWeathersDataToUI() {
        val cityNumber = binding.editTextNumber.text
        val requestUrl = "https://weather.tsukumijima.net/api/forecast/city/$cityNumber"

        Thread {
            val responseBody = startGetRequest(requestUrl)

            if (responseBody != null) {
                val weather = Json.decodeFromString<Weather>(responseBody)

                for (i in 0 .. 2) {
                    val dateLabel = weather.forecasts?.get(i)?.dateLabel
                    val detailWeather = weather.forecasts?.get(i)?.detail?.weather
                    val imageUrl = weather.forecasts?.get(i)?.image?.url
                    val maxTemperature = weather.forecasts?.get(i)?.temperature?.max?.celsius
                    val minTemperature = weather.forecasts?.get(i)?.temperature?.min?.celsius
                    val detailWind = weather.forecasts?.get(i)?.detail?.wind

                    val eachWeatherData = WeatherCardData(
                        dateLabel,
                        detailWeather,
                        imageUrl,
                        maxTemperature,
                        minTemperature,
                        detailWind
                    )

                    weatherList[i] = eachWeatherData

                    weatherAdapter.notifyItemChanged(i)

                }
            }
        }.start()
    }

    private fun addDummyDataToList() {
        weatherList.add(WeatherCardData("今日", "晴れ", "https://www.jma.go.jp/bosai/forecast/img/100.svg", "20", "10", "西風"))
        weatherList.add(WeatherCardData("明日", "曇り", "https://www.jma.go.jp/bosai/forecast/img/200.svg", "10", "0", "南の風　やや強く"))
        weatherList.add(WeatherCardData("明後日", "雨", "https://www.jma.go.jp/bosai/forecast/img/300.svg", "0", "-10", "北西の風　海上では　やや強く"))
    }
}