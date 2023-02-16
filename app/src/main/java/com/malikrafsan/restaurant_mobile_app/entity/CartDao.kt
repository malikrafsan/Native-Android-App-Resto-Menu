package com.malikrafsan.restaurant_mobile_app.entity

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM carts order by id")
    fun getAll() : Flow<List<Cart>>

    @Query("SELECT * FROM carts WHERE id = :id")
    suspend fun getById(id: String) : List<Cart>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cart: Cart)

    @Delete
    suspend fun delete(cart: Cart)
}