package com.malikrafsan.restaurant_mobile_app.ui.keranjang

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malikrafsan.restaurant_mobile_app.entity.Cart
import com.malikrafsan.restaurant_mobile_app.ui.keranjang.KeranjangViewModel

class KeranjangAdapter(
    private val ctx: Context,
    private val data: List<Cart>,
    private val cartViewModel: CartViewModel
) : RecyclerView.Adapter<KeranjangAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(com.malikrafsan.restaurant_mobile_app.R.layout.fragment_daftar_menu, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val datum = this.data[position]

        // sets the image to the imageview from our itemHolder class
        holder.nama.text = datum.name

        // sets the text to the textview from our itemHolder class
        holder.harga.text = datum.price.toString()

        holder.qtyPesanan.text = datum.sold.toString()

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return this.data.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val nama: TextView = itemView.findViewById(com.malikrafsan.restaurant_mobile_app.R.id.namaMakanan)
        val harga: TextView = itemView.findViewById(com.malikrafsan.restaurant_mobile_app.R.id.hargaMakanan)
        val qtyPesanan: TextView = itemView.findViewById(com.malikrafsan.restaurant_mobile_app.R.id.jumlahMakanan)
    }
}
