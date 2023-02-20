package com.malikrafsan.restaurant_mobile_app.ui.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MenuViewModel(val name: String, val currency: String, val price: Int, val sold: Int, val description: String, val type: String, var buy: Int) : ViewModel() {

}