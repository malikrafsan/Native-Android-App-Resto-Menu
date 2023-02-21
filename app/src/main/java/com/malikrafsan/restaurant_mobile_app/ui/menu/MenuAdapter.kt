package com.malikrafsan.restaurant_mobile_app.ui.menu

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.malikrafsan.restaurant_mobile_app.entity.Cart
import com.malikrafsan.restaurant_mobile_app.event.CartEvent
import com.malikrafsan.restaurant_mobile_app.ui.menu.MenuViewModel

class MenuAdapter(
    private val context: Context,
    private val menu: List<Cart>,
//    private val menuViewModel: MenuViewModel
    ): RecyclerView.Adapter<MenuAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val itemView = LayoutInflater.from(parent.context).inflate(com.malikrafsan.restaurant_mobile_app.R.layout.list_makanan, parent, false)
        return Holder(itemView)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val currentItem = this.menu[position]
        holder.namaMakanan.text = currentItem.name
//        holder.hargaMakanan.text = currentItem.currency + currentItem.price.toString()
        holder.hargaMakanan.text = currentItem.currency + currentItem.price.toString()
        holder.terjualMakanan.text = currentItem.sold.toString() + " terjual"
        holder.deskripsiMakanan.text = currentItem.description
        holder.totalPesanMakanan.text = currentItem.qty.toString()

        if (currentItem.qty == 0) {
            holder.minusButton.visibility = View.INVISIBLE
            holder.totalPesanMakanan.visibility = View.INVISIBLE
        } else {
            holder.minusButton.visibility = View.VISIBLE
            holder.totalPesanMakanan.visibility = View.VISIBLE
        }

        holder.plusButton.setOnClickListener {
            Log.d("btnClick", "Add qty")
//            menuViewModel.addQty(currentItem)
            holder.totalPesanMakanan.text = (currentItem.qty + 1).toString()
            holder.minusButton.visibility = View.VISIBLE
            holder.totalPesanMakanan.visibility = View.VISIBLE
        }

        holder.minusButton.setOnClickListener {
            Log.d("btnClick", "Subs qty")
//            menuViewModel.subsQty(currentItem)
            holder.totalPesanMakanan.text = (currentItem.qty - 1).toString()

            if (currentItem.qty == 1) {
                holder.minusButton.visibility = View.INVISIBLE
                holder.totalPesanMakanan.visibility = View.INVISIBLE
            }
        }

    }

    override fun getItemCount(): Int {
        return this.menu.size
    }

    class Holder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val namaMakanan: TextView = itemView.findViewById(com.malikrafsan.restaurant_mobile_app.R.id.namaMakanan)
        val hargaMakanan: TextView = itemView.findViewById(com.malikrafsan.restaurant_mobile_app.R.id.hargaMakanan)
        val terjualMakanan: TextView = itemView.findViewById(com.malikrafsan.restaurant_mobile_app.R.id.terjualMakanan)
        val deskripsiMakanan: TextView = itemView.findViewById(com.malikrafsan.restaurant_mobile_app.R.id.deskripsiMakanan)
        val totalPesanMakanan: TextView = itemView.findViewById(com.malikrafsan.restaurant_mobile_app.R.id.totalPesanMakanan)
        val minusButton: TextView = itemView.findViewById(com.malikrafsan.restaurant_mobile_app.R.id.minusButton)
        val plusButton: TextView = itemView.findViewById(com.malikrafsan.restaurant_mobile_app.R.id.plusButton)
    }
}