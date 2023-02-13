package com.malikrafsan.restaurant_mobile_app.ui.restaurant

import androidx.fragment.app.Fragment

class RestaurantFragment : Fragment() {

private var _binding = null
  // This property is only valid between onCreateView and
  // onDestroyView.


override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}