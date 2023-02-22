package com.malikrafsan.restaurant_mobile_app.repository

import com.malikrafsan.restaurant_mobile_app.entity.Cart
import com.malikrafsan.restaurant_mobile_app.entity.CartDao
import kotlinx.coroutines.flow.Flow

class CartRepositoryImpl(
    private val dao : CartDao
): CartRepository {
    override fun getAll(): Flow<List<Cart>> {
        return dao.getAll()
    }

    override suspend fun update(cart: Cart) {
        dao.update(cart)
    }

    override suspend fun insert(cart: Cart) {
        dao.insert(cart)
    }

    override suspend fun delete(cart: Cart) {
        dao.delete(cart)
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }
}