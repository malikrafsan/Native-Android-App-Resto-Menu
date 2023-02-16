package com.malikrafsan.restaurant_mobile_app.event

import com.malikrafsan.restaurant_mobile_app.entity.Cart

sealed class CartEvent{
    data class ChangeQty(val cart: Cart, val qty: Int) : CartEvent()
    data class onAddClick(val cart: Cart): CartEvent()
}
