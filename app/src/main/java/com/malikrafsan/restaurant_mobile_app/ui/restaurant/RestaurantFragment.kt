package com.malikrafsan.restaurant_mobile_app.ui.restaurant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.malikrafsan.restaurant_mobile_app.ui.keranjang.RestaurantViewModel
import com.malikrafsan.restaurant_mobile_app.ui.menu.MenuAdapter
import com.malikrafsan.restaurant_mobile_app.ui.menu.MenuViewModel

class RestaurantFragment : Fragment() {

    private var _binding = null
    // This property is only valid between onCreateView and
    // onDestroyView.

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater!!.inflate(com.malikrafsan.restaurant_mobile_app.R.layout.fragment_restaurant, container, false)

        // getting the recyclerview by its id
        val restaurantRecyclerview = view.findViewById<RecyclerView>(com.malikrafsan.restaurant_mobile_app.R.id.restaurantRecyclerview)

        // this creates a vertical layout Manager
        restaurantRecyclerview.layoutManager = LinearLayoutManager(requireContext())

        // ArrayList of class ItemsViewModel
        val data = ArrayList<RestaurantViewModel>()

        // This loop will create 20 Views containing
        // the image with the count of view
        for (i in 1..10) {
            data.add(RestaurantViewModel("Restorant ke-" + i.toString(), "Sangkuriang", "10000", "120", "50"))
        }

        // This will pass the ArrayList to our Adapter
        val adapter = RestaurantAdapter(data)

        // Setting the Adapter with the recyclerview
        restaurantRecyclerview.adapter = adapter

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