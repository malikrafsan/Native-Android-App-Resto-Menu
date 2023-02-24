package com.malikrafsan.restaurant_mobile_app.dto

data class MenuData(
    val currency: String,
    val description: String,
    val name: String,
    val price: Int,
    val sold: Int,
    val type: String,
    var qty: Int = 0,
)