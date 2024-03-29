package com.malikrafsan.restaurant_mobile_app.ui.menu

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
class MenuViewModel @Inject constructor(
    private val repository: CartRepository
) : ViewModel() {
    val carts = repository.getAll()
    var searchQuery : String = ""

    fun onEvent(event: CartEvent){
        when(event){
            is CartEvent.ChangeQty -> {
                viewModelScope.launch {
                    if (event.qty == 0) {
                        repository.delete(event.cart)
                    } else {
                        repository.insert(
                            event.cart.copy(
                                qty = event.qty
                            )
                        )
                    }
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

    suspend fun deleteCart(cart: Cart) {
        repository.delete(cart)
    }
}