package com.malikrafsan.restaurant_mobile_app.ui.keranjang

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RestaurantViewModel(val namaRestaurant: String, val alamatRestaurant: String, val telpRestaurant: String, val longitude: Double, val latitude: Double) : ViewModel(), Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readDouble()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(namaRestaurant)
        parcel.writeString(alamatRestaurant)
        parcel.writeString(telpRestaurant)
        parcel.writeDouble(longitude)
        parcel.writeDouble(latitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RestaurantViewModel> {
        override fun createFromParcel(parcel: Parcel): RestaurantViewModel {
            return RestaurantViewModel(parcel)
        }

        override fun newArray(size: Int): Array<RestaurantViewModel?> {
            return arrayOfNulls(size)
        }
    }

}