package com.malikrafsan.restaurant_mobile_app.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.malikrafsan.restaurant_mobile_app.ui.menu.MenuAdapter
import com.malikrafsan.restaurant_mobile_app.ui.menu.MenuViewModel

class MenuFragment : Fragment() {

private var _binding = null
  // This property is only valid between onCreateView and
  // onDestroyView.

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater!!.inflate(com.malikrafsan.restaurant_mobile_app.R.layout.fragment_menu, container, false)

        // getting the recyclerview by its id
        val menuMakananRecyclerView = view.findViewById<RecyclerView>(com.malikrafsan.restaurant_mobile_app.R.id.menuMakananRecyclerView)

        // this creates a vertical layout Manager
        menuMakananRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // ArrayList of class ItemsViewModel
        val data = ArrayList<MenuViewModel>()

        // This loop will create 20 Views containing
        // the image with the count of view
        for (i in 1..10) {
            data.add(MenuViewModel("Makanan ke-" + i.toString(), "Rp", 10000, 120, "Deskripsi makanan ke-" + i.toString(), "makanan"))
        }

        // This will pass the ArrayList to our Adapter
        val adapter = MenuAdapter(data)

        // Setting the Adapter with the recyclerview
        menuMakananRecyclerView.adapter = adapter

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