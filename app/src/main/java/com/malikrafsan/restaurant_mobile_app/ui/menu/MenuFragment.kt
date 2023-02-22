package com.malikrafsan.restaurant_mobile_app.ui.menu

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
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
import com.malikrafsan.restaurant_mobile_app.ui.keranjang.CartViewModel
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
    private lateinit var makananAdapter: MenuAdapter
    private lateinit var minumanAdapter: MenuAdapter
    private lateinit var makananSection: LinearLayout
    private lateinit var minumanSection: LinearLayout
    private val menuMakanan: MutableList<Cart> = mutableListOf()
    private val tempMenuMakanan: MutableList<Cart> = mutableListOf()
    private val menuMinuman: MutableList<Cart> = mutableListOf()
    private val tempMenuMinuman: MutableList<Cart> = mutableListOf()

    private val viewModel: CartViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this._binding = FragmentMenuBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerElmt()
        registerEvent()

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
                hideSection()
                return true
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
                } else {
                    notifyDataChanged()
                }

                hideSection()
                return true
            }
        })
    }

    private fun hideSection() {
        if (tempMenuMakanan.size == 0) {
            makananSection.visibility = View.GONE
        } else {
            makananSection.visibility = View.VISIBLE
        }

        if (tempMenuMinuman.size == 0) {
            minumanSection.visibility = View.GONE
        } else {
            minumanSection.visibility = View.VISIBLE
        }
    }

    private suspend fun syncMenu(listCart: List<Cart>) {
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


        listCart.forEach {currentCart ->
            if (!menuMakanan.contains(currentCart) and !menuMinuman.contains(currentCart)) {
                viewModel.deleteCart(currentCart)
            }
        }

        notifyDataChanged()
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

                    fetchedMenuMakanan?.data?.forEach {
                        if (it.type == "Food") {
                            menuMakanan.add(Cart.fromMenu(it))
                        } else {
                            menuMinuman.add(Cart.fromMenu(it))
                        }
                    }

                    tempMenuMakanan.addAll(menuMakanan)
                    tempMenuMinuman.addAll(menuMinuman)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}