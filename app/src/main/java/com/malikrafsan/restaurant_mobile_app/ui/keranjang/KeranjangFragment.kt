package com.malikrafsan.restaurant_mobile_app.ui.keranjang

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment


class KeranjangFragment : Fragment() {

private var _binding = null
  // This property is only valid between onCreateView and
  // onDestroyView.

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(com.malikrafsan.restaurant_mobile_app.R.layout.fragment_keranjang, container, false)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }



override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}