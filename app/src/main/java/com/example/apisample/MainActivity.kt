package com.example.apisample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
            val cityNumber = binding.editTextNumber.text
            val requestUrl = "https://weather.tsukumijima.net/api/forecast/city/$cityNumber"

            Thread {
                val handler = Handler(Looper.getMainLooper())
                val responseBody = startGetRequest(requestUrl)

                if (responseBody != null) {
                    val weather = Json.decodeFromString<Weather>(responseBody)
                    val weatherText = weather.forecasts?.get(0)?.detail?.weather
                    val weatherIcon = weather.forecasts?.get(0)?.image?.url

                    if (weatherIcon != null) {
                        Utils().fetchSVG(this, weatherIcon, binding.weatherImage)
                    }

                    handler.post {
                        binding.weatherText.text = weatherText
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
}