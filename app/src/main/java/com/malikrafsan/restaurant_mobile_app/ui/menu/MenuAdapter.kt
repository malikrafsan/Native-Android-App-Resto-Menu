package com.malikrafsan.restaurant_mobile_app.ui.menu

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.TypedArrayUtils.getString
import androidx.recyclerview.widget.RecyclerView
import com.malikrafsan.restaurant_mobile_app.entity.Cart
import com.malikrafsan.restaurant_mobile_app.event.CartEvent
import com.malikrafsan.restaurant_mobile_app.ui.keranjang.CartViewModel

class MenuAdapter (
    private val context: Context,
    private val listMenu: List<Cart>,
    private val viewModel: CartViewModel
): RecyclerView.Adapter<MenuAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val itemView = LayoutInflater.from(parent.context).inflate(com.malikrafsan.restaurant_mobile_app.R.layout.list_makanan, parent, false)
        return Holder(itemView)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val currentItem = this.listMenu[position]

        var harga : String = currentItem.price.toString().reversed().chunked(3).joinToString(".").reversed()
        var sold : String = if (currentItem.sold > 1000000) {
            (currentItem.sold / 1000000).toString() + "JT+ terjual"
        } else if (currentItem.sold > 1000) {
            (currentItem.sold / 1000).toString() + "RB+ terjual"
        } else {
            currentItem.sold.toString() + " terjual"
        }

        holder.namaMakanan.text = currentItem.name
        holder.hargaMakanan.text = context.getString(com.malikrafsan.restaurant_mobile_app.R.string.hargaMakanan, currentItem.currency, harga)
        holder.terjualMakanan.text = sold
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
            if (currentItem.qty == 0){
                viewModel.onEvent(CartEvent.onAddClick(currentItem))
                holder.minusButton.visibility = View.VISIBLE
                holder.totalPesanMakanan.visibility = View.VISIBLE
            } else {
                viewModel.onEvent(CartEvent.ChangeQty(currentItem, currentItem.qty + 1))
            }
            currentItem.qty++
        }

        holder.minusButton.setOnClickListener {
            viewModel.onEvent(CartEvent.ChangeQty(currentItem, currentItem.qty - 1))
            currentItem.qty--

            if (currentItem.qty == 0) {
                holder.minusButton.visibility = View.INVISIBLE
                holder.totalPesanMakanan.visibility = View.INVISIBLE
            }
        }

    }

    override fun getItemCount(): Int {
        return this.listMenu.size
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