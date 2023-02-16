package com.malikrafsan.restaurant_mobile_app.ui.keranjang

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malikrafsan.restaurant_mobile_app.entity.Cart
import com.malikrafsan.restaurant_mobile_app.event.CartEvent
import com.malikrafsan.restaurant_mobile_app.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val repository: CartRepository
) : ViewModel() {
    val carts = repository.getAll()

    fun onEvent(event: CartEvent){
        when(event){
            is CartEvent.ChangeQty -> {
                viewModelScope.launch {
                    repository.insert(
                        event.cart.copy(
                            qty = event.qty
                        )
                    )
                }
            }
            is CartEvent.onAddClick -> {
                Log.i("Onclick", "onAddClick")
                viewModelScope.launch {
                    repository.insert(
                        event.cart
                    )
                }
                Log.i("OnClick", carts.toString())
            }
        }
    }

    suspend fun addCart(cart: Cart){
        repository.insert(cart);
    }
}