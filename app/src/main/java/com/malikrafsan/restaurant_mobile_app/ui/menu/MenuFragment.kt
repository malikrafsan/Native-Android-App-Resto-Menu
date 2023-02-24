package com.malikrafsan.restaurant_mobile_app.ui.menu

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.malikrafsan.restaurant_mobile_app.api.MenuApi
import com.malikrafsan.restaurant_mobile_app.builder.ApiBuilder
import com.malikrafsan.restaurant_mobile_app.databinding.FragmentMenuBinding
import com.malikrafsan.restaurant_mobile_app.entity.Cart
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class MenuFragment : Fragment() {

    private var _binding : FragmentMenuBinding? = null
    private val binding get() = _binding!!

    private lateinit var menuMakananRecyclerView: RecyclerView
    private lateinit var menuMinumanRecyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var makananSection: LinearLayout
    private lateinit var minumanSection: LinearLayout
    private lateinit var notFoundTextView: TextView
    private lateinit var makananTextView: TextView
    private lateinit var minumanTextView: TextView
    private val menuMakanan: MutableList<Cart> = mutableListOf()
    private val tempMenuMakanan: MutableList<Cart> = mutableListOf()
    private val menuMinuman: MutableList<Cart> = mutableListOf()
    private val tempMenuMinuman: MutableList<Cart> = mutableListOf()

    private val viewModel: MenuViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this._binding = FragmentMenuBinding.inflate(
            inflater,
            container,
            false
        )

        Log.d("Element", "Start")
        registerElmt()
        registerEvent()
        Log.d("Element", "Finish")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        registerElmt()
//        registerEvent()

        Log.d("MenuFragment", "Load menu from database")
        loadMenu()

        menuMakananRecyclerView.adapter = MenuAdapter(
            requireContext(),
            tempMenuMakanan,
            viewModel
        )

        menuMinumanRecyclerView.adapter = MenuAdapter(
            requireContext(),
            tempMenuMinuman,
            viewModel
        )

        Log.d("MenuFragment", "Coroutine start")
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.carts.collect {
                    syncMenu(it)
                }
            }
        }
    }

    private fun registerEvent() {
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d("Search", "Enter $query")
                if (query!!.isNotEmpty()) {
                    tempMenuMakanan.clear()
                    tempMenuMinuman.clear()

                    menuMakanan.forEach { currentMenu ->
                        if (currentMenu.name.lowercase().contains(query)) tempMenuMakanan.add(currentMenu)
                    }

                    menuMinuman.forEach { currentMenu ->
                        if (currentMenu.name.lowercase().contains(query)) tempMenuMinuman.add(currentMenu)
                    }

                    menuMakananRecyclerView.adapter!!.notifyDataSetChanged()
                    menuMinumanRecyclerView.adapter!!.notifyDataSetChanged()
                    hideSection()
                } else {
                    notifyDataChanged()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d("Search", "Type $newText")
                val searchText: String = newText!!.lowercase()

                if (searchText.isNotEmpty()) {
                    tempMenuMakanan.clear()
                    tempMenuMinuman.clear()

                    menuMakanan.forEach { currentMenu ->
                        if (currentMenu.name.lowercase().contains(searchText)) tempMenuMakanan.add(currentMenu)
                    }

                    menuMinuman.forEach { currentMenu ->
                        if (currentMenu.name.lowercase().contains(searchText)) tempMenuMinuman.add(currentMenu)
                    }

                    menuMakananRecyclerView.adapter!!.notifyDataSetChanged()
                    menuMinumanRecyclerView.adapter!!.notifyDataSetChanged()
                    hideSection()
                } else {
                    notifyDataChanged()
                }

                viewModel.searchQuery = newText
                return true
            }
        })
    }

    private suspend fun syncMenu(listCart: List<Cart>) {
        Log.d("MenuFragment", "Sync menu from database")
        for (cart in listCart) {
            Log.d("MenuFragment", "Cart: ${cart.id} ${cart.name} ${cart.qty} ${cart.price} ${cart.type}")
        }

        var found: Boolean = false
        menuMakanan.forEach {currentMenu ->
            found = false
            listCart.forEach {currentCart ->
                if (currentMenu.id == currentCart.id) {
                    found = true
                    currentMenu.qty = currentCart.qty
                }
            }
            if (!found) {
                currentMenu.qty = 0
            }
        }

        menuMinuman.forEach {currentMenu ->
            found = false
            listCart.forEach {currentCart ->
                if (currentMenu.id == currentCart.id) {
                    found = true
                    currentMenu.qty = currentCart.qty
                }
            }
            if (!found) {
                currentMenu.qty = 0
            }
        }

        Log.d("MenuFragment", "menu makanan size ${menuMakanan.size}, menu minuman size ${menuMinuman.size}")
        if (menuMakanan.size != 0 && menuMinuman.size != 0) {
            listCart.forEach {currentCart ->
                if (!menuMakanan.contains(currentCart) and !menuMinuman.contains(currentCart)) {
                    viewModel.deleteCart(currentCart)
                }
            }
        }

        notifyDataChanged()
        searchView.setQuery(viewModel.searchQuery, true)
        Log.d("MenuFragment", "Finish sync menu")
    }

    private fun notifyDataChanged() {
        tempMenuMakanan.clear()
        tempMenuMakanan.addAll(menuMakanan)
        menuMakananRecyclerView.adapter!!.notifyDataSetChanged()

        tempMenuMinuman.clear()
        tempMenuMinuman.addAll(menuMinuman)
        menuMinumanRecyclerView.adapter!!.notifyDataSetChanged()
    }

    private fun registerElmt() {
        menuMakananRecyclerView = binding.menuMakananRecyclerView
        menuMinumanRecyclerView = binding.menuMinumanRecyclerView
        notFoundTextView = binding.notFoundTextView
        makananTextView = binding.makananTextView
        minumanTextView = binding.minumanTextView
        searchView = binding.searchView
        searchView.clearFocus()

        menuMakananRecyclerView.layoutManager = LinearLayoutManager(context)
        menuMinumanRecyclerView.layoutManager = LinearLayoutManager(context)

        makananSection = binding.makananSection
        minumanSection = binding.minumanSection
    }

    private fun loadMenu() {
        val menu = ApiBuilder.buildApi(MenuApi::class.java)
        val requestCall = menu.getMenu()

        requestCall.enqueue(object : Callback<com.malikrafsan.restaurant_mobile_app.dto.Menu> {
            override fun onResponse(
                call: Call<com.malikrafsan.restaurant_mobile_app.dto.Menu>,
                response: Response<com.malikrafsan.restaurant_mobile_app.dto.Menu>
            ) {
                if (response.isSuccessful) {
                    val fetchedMenuMakanan: com.malikrafsan.restaurant_mobile_app.dto.Menu? =
                        response.body()
                    menuMakanan.clear()
                    menuMinuman.clear()

                    fetchedMenuMakanan?.data?.forEach {
                        if (it.type == "Food") {
                            menuMakanan.add(Cart.fromMenu(it))
                        } else {
                            menuMinuman.add(Cart.fromMenu(it))
                        }
                    }

                    tempMenuMakanan.addAll(menuMakanan)
                    tempMenuMinuman.addAll(menuMinuman)

                    viewLifecycleOwner.lifecycleScope.launch {
                        viewModel.carts.collect {
                            syncMenu(it)
                        }
                    }

                    hideSection()
                    menuMakananRecyclerView.adapter!!.notifyDataSetChanged()
                    menuMinumanRecyclerView.adapter!!.notifyDataSetChanged()
                }
            }

            override fun onFailure(
                call: Call<com.malikrafsan.restaurant_mobile_app.dto.Menu>,
                t: Throwable
            ) {
                Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun hideSection() {
        Log.d("Hide Menu", "First")
        if (((menuMakanan.size != 0) and (tempMenuMakanan.size == 0)) and ((menuMinuman.size != 0) and (tempMenuMinuman.size == 0))) {
            Log.d("Hide Menu", "Tidak ada data sama sekali")
            notFoundTextView.visibility = View.VISIBLE
            makananTextView.visibility = View.GONE
            minumanTextView.visibility = View.GONE
        } else {
            Log.d("Hide Menu", "Terdapat beberapa data")
            notFoundTextView.visibility = View.GONE
            if ((menuMakanan.size != 0) and (tempMenuMakanan.size == 0)) {
                makananTextView.visibility = View.GONE
            } else {
                makananTextView.visibility = View.VISIBLE
            }
            if ((menuMinuman.size != 0) and (tempMenuMinuman.size == 0)) {
                minumanTextView.visibility = View.GONE
            } else {
                minumanTextView.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}