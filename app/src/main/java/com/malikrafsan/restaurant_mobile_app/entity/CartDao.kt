package com.malikrafsan.restaurant_mobile_app.entity

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM carts order by id")
    fun getAll() : Flow<List<Cart>>

    @Query("SELECT * FROM carts WHERE id = :id")
    suspend fun getById(id: String) : List<Cart>

    @Update
    suspend fun update(cart: Cart)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cart: Cart)

    @Delete
    suspend fun delete(cart: Cart)

    @Query("DELETE FROM carts")
    suspend fun deleteAll()
}