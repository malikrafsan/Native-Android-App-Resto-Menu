package com.malikrafsan.restaurant_mobile_app.ui.menu

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malikrafsan.restaurant_mobile_app.entity.Cart
import com.malikrafsan.restaurant_mobile_app.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel@Inject constructor(
//    private val repository: CartRepository
) : ViewModel() {
//    private val carts = repository.getAll()
    private val _menuMakanan = MutableLiveData<List<Cart>>()

    val menuMakanan :  LiveData<List<Cart>>
        get() = _menuMakanan
    fun updateMenuMakanan(menuMakanan: ArrayList<Cart>) {
        menuMakanan.forEach {
            _menuMakanan.value!!.plus(it)
        }
//        TODO("Update menu dari menuMakanan dan carts")
//        1. Iterasi menuMakanan
//        2. Cari padanan di carts
//        3. Update nilai qty
//        4. Masukkan ke _menuMakanan

    }

//    fun addQty(currentMenu: Cart) {
//        Log.d("ViewModel", "Add qty")
//        viewModelScope.launch {
//            if (currentMenu.qty == 0) {
//                repository.insert(currentMenu)
//            } else {
//                repository.insert(
//                    Cart(currentMenu.id, currentMenu.name, currentMenu.currency, currentMenu.description, currentMenu.price, currentMenu.sold, currentMenu.type, currentMenu.qty+1)
//                )
//            }
//        }
//    }
//
//    fun subsQty(currentMenu: Cart) {
//        Log.d("ViewModel", "Subs qty")
//        viewModelScope.launch {
//            if (currentMenu.qty == 1) {
//                repository.delete(currentMenu)
//            } else {
//                repository.insert(
//                    Cart(currentMenu.id, currentMenu.name, currentMenu.currency, currentMenu.description, currentMenu.price, currentMenu.sold, currentMenu.type, currentMenu.qty-1)
//                )
//            }
//        }
//    }
}