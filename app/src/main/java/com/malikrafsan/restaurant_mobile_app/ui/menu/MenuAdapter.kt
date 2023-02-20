package com.malikrafsan.restaurant_mobile_app.ui.menu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.TypedArrayUtils.getString
import androidx.recyclerview.widget.RecyclerView

class MenuAdapter (private val listMenu: ArrayList<MenuViewModel>): RecyclerView.Adapter<MenuAdapter.Holder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val itemView = LayoutInflater.from(parent.context).inflate(com.malikrafsan.restaurant_mobile_app.R.layout.list_makanan, parent, false)
        return Holder(itemView)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val currentItem = listMenu[position]
        holder.namaMakanan.text = currentItem.name
//        holder.hargaMakanan.text = currentItem.currency + currentItem.price.toString()
        holder.hargaMakanan.text = currentItem.currency + currentItem.price.toString()
        holder.terjualMakanan.text = currentItem.sold.toString() + " terjual"
        holder.deskripsiMakanan.text = currentItem.description
        holder.totalPesanMakanan.text = currentItem.buy.toString()

        if (currentItem.buy == 0) {
            holder.minusButton.visibility = View.INVISIBLE
            holder.totalPesanMakanan.visibility = View.INVISIBLE
        } else {
            holder.minusButton.visibility = View.VISIBLE
            holder.totalPesanMakanan.visibility = View.VISIBLE
        }

        holder.plusButton.setOnClickListener {
            currentItem.buy = currentItem.buy + 1
            holder.totalPesanMakanan.text = currentItem.buy.toString()
            holder.minusButton.visibility = View.VISIBLE
            holder.totalPesanMakanan.visibility = View.VISIBLE
        }

        holder.minusButton.setOnClickListener {
            currentItem.buy = currentItem.buy - 1
            holder.totalPesanMakanan.text = currentItem.buy.toString()

            if (currentItem.buy == 0) {
                holder.minusButton.visibility = View.INVISIBLE
                holder.totalPesanMakanan.visibility = View.INVISIBLE
            }
        }

    }

    override fun getItemCount(): Int {
        return listMenu.size
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