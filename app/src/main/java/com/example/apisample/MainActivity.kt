package com.example.apisample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import coil.decode.SvgDecoder
import coil.load
import com.example.apisample.databinding.ActivityMainBinding
import com.example.apisample.model.Weather
import com.pixplicity.sharp.Sharp
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.io.InputStream
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

        recyclerView.onFlingListener = null;
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)
        // addDataToList()

        weatherAdapter = WeatherAdapter(weatherList)
        recyclerView.adapter = weatherAdapter
    }

    private fun setWeathersDataToUI() {
        val cityNumber = binding.editTextNumber.text
        val requestUrl = "https://weather.tsukumijima.net/api/forecast/city/$cityNumber"

        // weatherList = ArrayList()

        Thread {
            // val handler = Handler(Looper.getMainLooper())
            val responseBody = startGetRequest(requestUrl)

            if (responseBody != null) {
                val weather = Json.decodeFromString<Weather>(responseBody)

                for (i in 0 .. 2) {
                    val eachDaysWeather = weather.forecasts?.get(i)?.telop
                    val eachDaysImageSrc = weather.forecasts?.get(i)?.image?.url
                    val eachDaysImageView = binding.recycleView.findViewById<ImageView>(R.id.weatherIcon)

                    val eachDaysImage = eachDaysImageView?.load(eachDaysImageSrc) {
                        decoderFactory { result, options, _ ->
                            SvgDecoder(
                                result.source, options
                            )
                        }
                    }
                    val eachWeatherData = com.example.apisample.Weather(
                        eachDaysImage,
                        eachDaysWeather,
                    )

                    weatherList.add(eachWeatherData)

                }
            }
        }.start()
    }
}