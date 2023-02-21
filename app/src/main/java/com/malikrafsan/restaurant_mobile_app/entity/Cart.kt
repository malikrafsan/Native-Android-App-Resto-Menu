package com.malikrafsan.restaurant_mobile_app.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.malikrafsan.restaurant_mobile_app.dto.Menu
import com.malikrafsan.restaurant_mobile_app.dto.MenuData

@Entity(tableName = "carts")
data class Cart(
    @PrimaryKey val id: String,
    val name: String,
    val currency: String,
    val description: String,
    val price: Int,
    val sold: Int,
    val type: String,
    val qty: Int,
) {
    companion object {
        private fun createId(menu: MenuData): String {
            return "${menu.name}-${menu.price}-${menu.currency}-${menu.type}"
        }

        fun fromMenu(menu: MenuData): Cart {
            return Cart(
                id = createId(menu),
                name = menu.name,
                currency = menu.currency,
                description = menu.description,
                price = menu.price,
                sold = menu.sold,
                type = menu.type,
                qty = 0,
            )
        }
    }
}
