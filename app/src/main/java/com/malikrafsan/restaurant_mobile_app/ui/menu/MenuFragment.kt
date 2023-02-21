package com.malikrafsan.restaurant_mobile_app.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.malikrafsan.restaurant_mobile_app.api.MenuApi
import com.malikrafsan.restaurant_mobile_app.builder.ApiBuilder
import com.malikrafsan.restaurant_mobile_app.ui.menu.MenuAdapter
import com.malikrafsan.restaurant_mobile_app.ui.menu.MenuViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList

class MenuFragment : Fragment() {

    private var _binding = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private lateinit var menuMakananRecyclerView: RecyclerView
    private lateinit var menuMinumanRecyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var makananAdapter: MenuAdapter
    private lateinit var minumanAdapter: MenuAdapter
    private lateinit var makananSection: LinearLayout
    private lateinit var minumanSection: LinearLayout
    private val menuMakanan: ArrayList<MenuViewModel> = ArrayList()
    private val tempMenuMakanan: ArrayList<MenuViewModel> = ArrayList()
    private val menuMinuman: ArrayList<MenuViewModel> = ArrayList()
    private val tempMenuMinuman: ArrayList<MenuViewModel> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater!!.inflate(
            com.malikrafsan.restaurant_mobile_app.R.layout.fragment_menu,
            container,
            false
        )

        loadMenu()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        menuMakananRecyclerView =
            view.findViewById(com.malikrafsan.restaurant_mobile_app.R.id.menuMakananRecyclerView)
        menuMakananRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        menuMinumanRecyclerView =
            view.findViewById(com.malikrafsan.restaurant_mobile_app.R.id.menuMinumanRecyclerView)
        menuMinumanRecyclerView.layoutManager = LinearLayoutManager(requireContext())

//        tempMenuMinuman.clear()

        makananAdapter = MenuAdapter(tempMenuMakanan)
        menuMakananRecyclerView.adapter = makananAdapter

        minumanAdapter = MenuAdapter(tempMenuMinuman)
        menuMinumanRecyclerView.adapter = minumanAdapter

        searchView = view.findViewById(com.malikrafsan.restaurant_mobile_app.R.id.searchView)
        searchView.clearFocus()
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
                    menuMakananRecyclerView.adapter!!.notifyDataSetChanged()
                    menuMinumanRecyclerView.adapter!!.notifyDataSetChanged()
                } else {
                    tempMenuMakanan.clear()
                    tempMenuMakanan.addAll(menuMakanan)
                    menuMakananRecyclerView.adapter!!.notifyDataSetChanged()

                    tempMenuMinuman.clear()
                    tempMenuMinuman.addAll(menuMinuman)
                    menuMinumanRecyclerView.adapter!!.notifyDataSetChanged()
                }

                hideSection()
                return true
            }
        })

        makananSection = view.findViewById(com.malikrafsan.restaurant_mobile_app.R.id.makananSection)
        minumanSection = view.findViewById(com.malikrafsan.restaurant_mobile_app.R.id.minumanSection)

        hideSection()

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
                            menuMakanan.add(
                                MenuViewModel(
                                    it.name,
                                    it.currency,
                                    it.price,
                                    it.sold,
                                    it.description,
                                    it.type,
                                    0
                                )
                            )
                        } else {
                            menuMinuman.add(
                                MenuViewModel(
                                    it.name,
                                    it.currency,
                                    it.price,
                                    it.sold,
                                    it.description,
                                    it.type,
                                    0
                                )
                            )
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