package com.malikrafsan.restaurant_mobile_app.ui.keranjang

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.malikrafsan.restaurant_mobile_app.ui.scan_payment.ScanPaymentActivity
import com.malikrafsan.restaurant_mobile_app.databinding.FragmentKeranjangBinding
import com.malikrafsan.restaurant_mobile_app.entity.Cart
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class KeranjangFragment : Fragment() {
    private var _binding: FragmentKeranjangBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private var carts: MutableList<Cart> = mutableListOf()
    private var totalAmount: Int = 0
    private val viewModel: KeranjangViewModel by viewModels()
    private lateinit var totalPriceTextView: TextView

    private fun registerEvent(view: View) {
        view.findViewById<Button>(com.malikrafsan.restaurant_mobile_app.R.id.bayar).setOnClickListener {
            startActivity(
                Intent(
                    requireContext(),
                    ScanPaymentActivity::class.java
                )
            )
        }
    }

    private fun registerElmt(view: View) {
        totalPriceTextView = view.findViewById(com.malikrafsan.restaurant_mobile_app.R.id.total_price)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this._binding = FragmentKeranjangBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun collectCart(lst: List<Cart>) {
        this.carts.clear()
        this.totalAmount = 0
        Log.d("this Carts", this.carts.toString())
        lst.forEach {
            this.carts.add(it)
            this.totalAmount += it.price * it.qty
        }
        Log.d("test", "up")
        this.totalPriceTextView.text = context?.getString(com.malikrafsan.restaurant_mobile_app.R.string.hargaMakanan, "IDR", this.totalAmount.toString().reversed().chunked(3).joinToString(".").reversed())

        recyclerView.adapter?.notifyDataSetChanged()
        Log.d("test", "end")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = KeranjangAdapter(
            requireContext(),
            carts,
            viewModel
        )

        registerEvent(view)
        registerElmt(view)

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