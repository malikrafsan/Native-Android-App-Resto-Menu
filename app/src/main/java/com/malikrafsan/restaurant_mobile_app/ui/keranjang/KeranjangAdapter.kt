package com.malikrafsan.restaurant_mobile_app.ui.keranjang

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malikrafsan.restaurant_mobile_app.entity.Cart
import com.malikrafsan.restaurant_mobile_app.event.CartEvent

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
        Log.d("KeranjangAdapter", "onBindViewHolder: ${data[position]}")

        val datum = this.data[position]

        // sets the image to the imageview from our itemHolder class
        holder.nama.text = datum.name

        // sets the text to the textview from our itemHolder class
        val context = holder.nama.context
        holder.harga.text = context.getString(com.malikrafsan.restaurant_mobile_app.R.string.hargaMakanan, datum.currency, datum.price.toString().reversed().chunked(3).joinToString(".").reversed())

        holder.qtyPesanan.text = datum.qty.toString()
        holder.plusQtyBtn.setOnClickListener {
            cartViewModel.onEvent(CartEvent.ChangeQty(datum, datum.qty + 1))
        }
        holder.minusQtyBtn.setOnClickListener {
            cartViewModel.onEvent(CartEvent.ChangeQty(datum, datum.qty - 1))
        }

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

        val plusQtyBtn: Button = itemView.findViewById(com.malikrafsan.restaurant_mobile_app.R.id.plusQtyButton)
        val minusQtyBtn: Button = itemView.findViewById(com.malikrafsan.restaurant_mobile_app.R.id.minusQtyButton)
    }
}
