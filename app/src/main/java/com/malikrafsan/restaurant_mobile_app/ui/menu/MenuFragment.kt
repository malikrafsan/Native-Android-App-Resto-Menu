package com.malikrafsan.restaurant_mobile_app.ui.keranjang

import androidx.fragment.app.Fragment

class MenuFragment : Fragment() {

private var _binding = null
  // This property is only valid between onCreateView and
  // onDestroyView.


override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}