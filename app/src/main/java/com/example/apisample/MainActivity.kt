package com.example.apisample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.ListAdapter
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



        binding.button.setOnClickListener {
            setHorizontalWeatherViewAdapter()
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

        // addDummyDataToList()

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
                    val eachDaysWeather = weather.forecasts?.get(i)?.telop
                    val eachDaysImageSrc = weather.forecasts?.get(i)?.image?.url

                    val eachWeatherData = com.example.apisample.Weather(
                        eachDaysImageSrc,
                        eachDaysWeather,
                    )

                    weatherList.add(eachWeatherData)

                    val adapter = recyclerView.adapter
                    adapter?.notifyItemChanged(i)

                }
            }
        }.start()
    }

//    private fun addDummyDataToList() {
//        weatherList.add(Weather("https://www.jma.go.jp/bosai/forecast/img/100.svg", "晴れ"))
//        weatherList.add(Weather("https://www.jma.go.jp/bosai/forecast/img/200.svg", "曇り"))
//        weatherList.add(Weather("https://www.jma.go.jp/bosai/forecast/img/300.svg", "雨"))
//    }
}