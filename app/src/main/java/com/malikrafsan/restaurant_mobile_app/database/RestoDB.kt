package com.malikrafsan.restaurant_mobile_app.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.malikrafsan.restaurant_mobile_app.entity.Cart
import com.malikrafsan.restaurant_mobile_app.entity.CartDao

@Database(
    entities = [Cart::class],
    version = 1,
    exportSchema = false
)
abstract class RestoDB: RoomDatabase() {
    abstract val cartDAO : CartDao
}
