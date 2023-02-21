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
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.malikrafsan.restaurant_mobile_app.api.MenuApi
import com.malikrafsan.restaurant_mobile_app.builder.ApiBuilder
import com.malikrafsan.restaurant_mobile_app.databinding.FragmentMenuBinding
import com.malikrafsan.restaurant_mobile_app.dto.MenuData
import com.malikrafsan.restaurant_mobile_app.entity.Cart
import com.malikrafsan.restaurant_mobile_app.ui.menu.MenuViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuFragment : Fragment() {

    private var _binding : FragmentMenuBinding? = null
    private val binding get() = _binding!!

    private lateinit var makananRecyclerView: RecyclerView
    private lateinit var minumanRecyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var makananAdapter: MenuAdapter
    private lateinit var minumanAdapter: MenuAdapter
    private lateinit var makananSection: LinearLayout
    private lateinit var minumanSection: LinearLayout

    private val viewModel: MenuViewModel by viewModels()
    private var menuMakanan = ArrayList<Cart>()
    private var tempMenuMakanan: MutableList<Cart> = mutableListOf()
    private var menuMinuman: MutableList<Cart> = mutableListOf()
    private var tempMenuMinuman: MutableList<Cart> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this._binding = FragmentMenuBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerElmt()
//        registerEvent()

        loadMenu()
//        viewModel.updateMenuMakanan(menuMakanan)
//        Log.d("Menu", viewModel.menuMakanan.value.toString())

        makananRecyclerView.layoutManager = LinearLayoutManager(context)
        minumanRecyclerView.layoutManager = LinearLayoutManager(context)

//        makananRecyclerView.adapter = MenuAdapter(requireContext(), menuMakanan, viewModel)
//        minumanRecyclerView.adapter = MenuAdapter(requireContext(), menuMinuman, viewModel)
        makananRecyclerView.adapter = MenuAdapter(requireContext(), menuMakanan)
        minumanRecyclerView.adapter = MenuAdapter(requireContext(), menuMinuman)

    }

    private fun registerElmt() {
        makananRecyclerView = binding.menuMakananRecyclerView
        minumanRecyclerView = binding.menuMinumanRecyclerView
        searchView = binding.searchView
        searchView.clearFocus()
    }

    private fun registerEvent() {
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                hideSection()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val searchText = newText!!.lowercase()
                if (searchText.isNotEmpty()) {
                    tempMenuMakanan.clear()
                    tempMenuMinuman.clear()
                    menuMakanan.forEach {
                        if (it.name.lowercase().contains(searchText)) {
                            tempMenuMakanan.add((it))
                        }
                    }
                    menuMinuman.forEach {
                        if (it.name.lowercase().contains(searchText)) {
                            tempMenuMinuman.add((it))
                        }
                    }
                    makananRecyclerView.adapter!!.notifyDataSetChanged()
                    minumanRecyclerView.adapter!!.notifyDataSetChanged()
                } else {
                    tempMenuMakanan.clear()
                    tempMenuMakanan.addAll(menuMakanan)
                    makananRecyclerView.adapter!!.notifyDataSetChanged()

                    tempMenuMinuman.clear()
                    tempMenuMinuman.addAll(menuMinuman)
                    minumanRecyclerView.adapter!!.notifyDataSetChanged()
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