package com.malikrafsan.restaurant_mobile_app.builder

import com.malikrafsan.restaurant_mobile_app.config.InternetConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiBuilder {
    private const val DEFAULT_URL = InternetConfig.BASE_URL
    private val URL = System.getenv("BE_URL") ?: DEFAULT_URL

    private val okHttp = OkHttpClient.Builder()

    private val builder = Retrofit.Builder().baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttp.build())

    private val retrofit = builder.build()

    fun <T> buildApi (apiType :Class<T>):T{
        return retrofit.create(apiType)
    }
}