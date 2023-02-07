package com.malikrafsan.restaurant_mobile_app.builder

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiBuilder {
    private const val DEFAULT_URL = "http://192.168.18.232:9000/v1/"
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