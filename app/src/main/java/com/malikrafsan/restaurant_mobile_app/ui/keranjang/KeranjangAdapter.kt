package com.malikrafsan.restaurant_mobile_app.ui.keranjang

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malikrafsan.restaurant_mobile_app.ui.keranjang.KeranjangViewModel

class KeranjangAdapter(private val mList: List<KeranjangViewModel>) : RecyclerView.Adapter<KeranjangAdapter.ViewHolder>() {

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

        val KeranjangViewModel = mList[position]

        // sets the image to the imageview from our itemHolder class
        holder.namaMakanan.text = KeranjangViewModel.namaMakanan

        // sets the text to the textview from our itemHolder class
        holder.hargaMakanan.text = KeranjangViewModel.hargaMakanan

        holder.terjualMakanan.text = KeranjangViewModel.terjualMakanan.toString()

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val namaMakanan: TextView = itemView.findViewById(com.malikrafsan.restaurant_mobile_app.R.id.namaMakanan)
        val hargaMakanan: TextView = itemView.findViewById(com.malikrafsan.restaurant_mobile_app.R.id.hargaMakanan)
        val terjualMakanan: TextView = itemView.findViewById(com.malikrafsan.restaurant_mobile_app.R.id.jumlahMakanan)
    }
}
