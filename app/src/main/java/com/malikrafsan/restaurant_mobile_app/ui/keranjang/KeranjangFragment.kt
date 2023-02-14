package com.malikrafsan.restaurant_mobile_app.ui.keranjang

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
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