package com.malikrafsan.restaurant_mobile_app.api

import com.malikrafsan.restaurant_mobile_app.dto.PayResponse
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Path

interface Payment {
    @POST("payment/{code}")
    fun pay(@Path("code") code: String): Call<PayResponse>
}