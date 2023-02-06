package com.malikrafsan.restaurant_mobile_app.api

import retrofit2.http.POST
import retrofit2.http.Path

interface Payment {
    @POST("payment/{code}")
    fun pay(@Path("code") code: String)
}