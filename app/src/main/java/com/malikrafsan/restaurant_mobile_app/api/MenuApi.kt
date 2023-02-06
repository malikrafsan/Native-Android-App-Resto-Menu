package com.malikrafsan.restaurant_mobile_app.api

import com.malikrafsan.restaurant_mobile_app.dto.Menu
import retrofit2.Call
import retrofit2.http.GET

interface MenuApi {
    @GET("menu")
    fun getMenu(): Call<Menu>

    @GET("menu/food")
    fun getFoodMenu(): Call<Menu>

    @GET("menu/drink")
    fun getDrinkMenu(): Call<Menu>
}