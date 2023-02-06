package com.malikrafsan.restaurant_mobile_app.dto

data class BranchData(
    val address: String,
    val contact_person: String,
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val phone_number: String,
    val popular_food: String
)