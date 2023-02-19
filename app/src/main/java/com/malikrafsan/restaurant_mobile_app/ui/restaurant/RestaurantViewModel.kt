package com.malikrafsan.restaurant_mobile_app.ui.keranjang

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RestaurantViewModel(val namaRestaurant: String, val alamatRestaurant: String, val telpRestaurant: String, val longitude: Double, val latitude: Double) : ViewModel() {

}