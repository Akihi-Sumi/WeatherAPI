package com.example.apisample.model

import kotlinx.serialization.Serializable

@Serializable
data class Weather(
    val publicTime: String?,
    val publicTimeFormatted: String?,
    val publishingOffice: String?,
    val title: String?,
    val link: String?,
    val description: Description?,
    val forecasts: List<Forecast>?,
    val location: Location?,
    val copyright: Copyright?,
) {

    @Serializable
    data class Description(
        val publicTime: String?,
        val publicTimeFormatted: String?,
        val headlineText: String?,
        val bodyText: String?,
        val text: String?,
    )

    @Serializable
    data class Forecast(
        val date: String?,
        val dateLabel: String?,
        val telop: String?,
        val detail: Detail?,
        val temperature: Temperature?,
        val chanceOfRain: ChanceOfRain?,
        val image: Image?,
    ) {

        @Serializable
        data class Detail(
            val weather: String?,
            val wind: String?,
            val wave: String?,
        )

        @Serializable
        data class Temperature(
            val min: TemperatureItem?,
            val max: TemperatureItem?,
        ) {

            @Serializable
            data class TemperatureItem(
                val celsius: Float?,
                val fahrenheit: Float?,
            )
        }

        @Serializable
        data class ChanceOfRain(
            val T00_06: String?,
            val T06_12: String?,
            val T12_18: String?,
            val T18_24: String?,
        )

        @Serializable
        data class Image(
            val title: String?,
            val url: String?,
            val width: Int?,
            val height: Int?,
        )
    }

    @Serializable
    data class Location(
        val area: String?,
        val prefecture: String?,
        val district: String?,
        val city: String?,
    )

    @Serializable
    data class Copyright(
        val title: String?,
        val link: String?,
        val image: CopyrightImage?,
        val provider: List<Provider>?,
    ) {
        @Serializable
        data class CopyrightImage(
            val title: String?,
            val link: String?,
            val url: String?,
            val width: Int?,
            val height: Int?,
        )

        @Serializable
        data class Provider(
            val link: String?,
            val name: String?,
            val note: String?,
        )
    }
}
