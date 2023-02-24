package com.malikrafsan.restaurant_mobile_app.entity

import android.os.Parcel
import android.os.Parcelable
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
    var qty: Int,
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(currency)
        parcel.writeString(description)
        parcel.writeInt(price)
        parcel.writeInt(sold)
        parcel.writeString(type)
        parcel.writeInt(qty)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Cart> {
        override fun createFromParcel(parcel: Parcel): Cart {
            return Cart(parcel)
        }

        override fun newArray(size: Int): Array<Cart?> {
            return arrayOfNulls(size)
        }

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
