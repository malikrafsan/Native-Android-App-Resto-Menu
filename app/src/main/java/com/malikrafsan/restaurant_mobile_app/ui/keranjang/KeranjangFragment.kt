package com.malikrafsan.restaurant_mobile_app.ui.keranjang

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class KeranjangFragment : Fragment() {

private var _binding = null
  // This property is only valid between onCreateView and
  // onDestroyView.


override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}