package com.malikrafsan.restaurant_mobile_app.ui.restaurant

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malikrafsan.restaurant_mobile_app.ui.keranjang.RestaurantViewModel

class RestaurantAdapter (private var data: ArrayList<RestaurantViewModel>): RecyclerView.Adapter<RestaurantAdapter.ViewHolder>() {
    // create new views
    public fun setData(data: ArrayList<RestaurantViewModel>) {
        this.data = data
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(com.malikrafsan.restaurant_mobile_app.R.layout.fragment_daftar_restaurant, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val datum = this.data[position]

        holder.namaRestaurant.text = datum.namaRestaurant
        holder.alamatRestaurant.text = datum.alamatRestaurant
        holder.telpRestaurant.text = datum.telpRestaurant
        val context = holder.telpRestaurant.context
        holder.mapsButton.setOnClickListener {
            val gmmIntentUri = Uri.parse("geo:0,0?q=" + datum.latitude.toBigDecimal().toPlainString() + "," + datum.longitude.toBigDecimal().toPlainString() + "(" + datum.namaRestaurant +")")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            context.startActivity(mapIntent)
            // val gmmIntentUri = Uri.parse("http://maps.google.com/maps?saddr=" + datum.latitude.toBigDecimal().toPlainString() + "," + datum.longitude.toBigDecimal().toPlainString())
            //val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            //mapIntent.setPackage("com.google.android.apps.maps")
            //context.startActivity(mapIntent)
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }
    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val namaRestaurant: TextView = itemView.findViewById(com.malikrafsan.restaurant_mobile_app.R.id.namaRestaurant)
        val alamatRestaurant: TextView = itemView.findViewById(com.malikrafsan.restaurant_mobile_app.R.id.alamat)
        val telpRestaurant: TextView = itemView.findViewById(com.malikrafsan.restaurant_mobile_app.R.id.telp)
        val mapsButton: Button = itemView.findViewById(com.malikrafsan.restaurant_mobile_app.R.id.maps)
    }

}