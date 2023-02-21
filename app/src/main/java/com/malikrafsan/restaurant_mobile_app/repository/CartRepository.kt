package com.malikrafsan.restaurant_mobile_app.repository

import com.malikrafsan.restaurant_mobile_app.entity.Cart
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun getAll() : Flow<List<Cart>>

    suspend fun update(cart: Cart)

    suspend fun insert(cart: Cart)

    suspend fun delete(cart: Cart)
}