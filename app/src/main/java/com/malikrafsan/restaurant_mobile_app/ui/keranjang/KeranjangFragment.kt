package com.malikrafsan.restaurant_mobile_app.ui.keranjang

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.malikrafsan.restaurant_mobile_app.ScanPaymentActivity


class KeranjangFragment : Fragment() {

private var _binding = null
  // This property is only valid between onCreateView and
  // onDestroyView.

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater!!.inflate(com.malikrafsan.restaurant_mobile_app.R.layout.fragment_keranjang, container, false)
        view.findViewById<Button>(com.malikrafsan.restaurant_mobile_app.R.id.bayar).setOnClickListener {
            startActivity(
                Intent(
                    requireContext(),
                    ScanPaymentActivity::class.java
                )
            )
        }

        // getting the recyclerview by its id
        val recyclerview = view.findViewById<RecyclerView>(com.malikrafsan.restaurant_mobile_app.R.id.recyclerview)

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(requireContext())

        // ArrayList of class ItemsViewModel
        val data = ArrayList<KeranjangViewModel>()

        // This loop will create 20 Views containing
        // the image with the count of view
        for (i in 1..20) {
            data.add(KeranjangViewModel("Makanan ke " + i.toString(), hargaMakanan = "50000", terjualMakanan = 1))
        }

        // This will pass the ArrayList to our Adapter
        val adapter = KeranjangAdapter(data)

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter

        return view
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }



override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}