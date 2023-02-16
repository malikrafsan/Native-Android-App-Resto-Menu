package com.malikrafsan.restaurant_mobile_app.ui.keranjang

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.malikrafsan.restaurant_mobile_app.ScanPaymentActivity
import com.malikrafsan.restaurant_mobile_app.databinding.FragmentKeranjangBinding
import com.malikrafsan.restaurant_mobile_app.dto.Menu
import com.malikrafsan.restaurant_mobile_app.dto.MenuData
import com.malikrafsan.restaurant_mobile_app.entity.Cart
import com.malikrafsan.restaurant_mobile_app.event.CartEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.random.Random


@AndroidEntryPoint
class KeranjangFragment : Fragment() {
    private var _binding: FragmentKeranjangBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private var carts: MutableList<Cart> = mutableListOf()
    private var totalAmount: Int = 0
    private val viewModel: CartViewModel by viewModels()

  // This property is only valid between onCreateView and
  // onDestroyView.

    private fun registerEvent(inflater: LayoutInflater, container: ViewGroup?) {
        val view: View = inflater!!.inflate(com.malikrafsan.restaurant_mobile_app.R.layout.fragment_keranjang, container, false)
        view.findViewById<Button>(com.malikrafsan.restaurant_mobile_app.R.id.bayar).setOnClickListener {
            startActivity(
                Intent(
                    requireContext(),
                    ScanPaymentActivity::class.java
                )
            )
        }
        view.findViewById<Button>(com.malikrafsan.restaurant_mobile_app.R.id.addNewCart).setOnClickListener {
            val menuData = MenuData(
                name = "Makanan 1",
                price = 50000,
                description = "Makanan 1",
                currency = "IDR",
                sold = 90,
                type = "Food",
            )

            val cart = Cart.fromMenu(menuData)
            viewModel.onEvent(CartEvent.onAddClick(cart))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        registerEvent(inflater, container)

        // getting the recyclerview by its id
        val recyclerview = view?.findViewById<RecyclerView>(com.malikrafsan.restaurant_mobile_app.R.id.recyclerview)

        // this creates a vertical layout Manager
        recyclerview?.layoutManager = LinearLayoutManager(requireContext())

        // ArrayList of class ItemsViewModel
//        val data = ArrayList<KeranjangViewModel>()

//        // This loop will create 20 Views containing
//        // the image with the count of view
//        for (i in 1..20) {
//            data.add(KeranjangViewModel("Makanan ke " + i.toString(), hargaMakanan = "50000", terjualMakanan = 1))
//        }

        // This will pass the ArrayList to our Adapter
        val adapter = KeranjangAdapter(
            requireContext(),
            carts,
            viewModel
        )

        // Setting the Adapter with the recyclerview
        recyclerview?.adapter = adapter

        return view
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun collectCart(lst: List<Cart>) {
        this.carts = lst as MutableList<Cart>
        this.totalAmount = 0
        lst.forEach {
            this.totalAmount += it.price * it.qty
        }
        this.recyclerView.adapter?.notifyDataSetChanged()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(context)

        Log.i("Collect", "Upper")
        viewLifecycleOwner.lifecycleScope.launch{
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.carts.collect {
                    collectCart(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}