package com.malikrafsan.restaurant_mobile_app.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
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
    private lateinit var makananRecyclerView: RecyclerView
    private lateinit var minumanRecyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var makananAdapter: MenuAdapter
    private lateinit var minumanAdapter: MenuAdapter
    private val allMenu: ArrayList<MenuViewModel> = ArrayList<MenuViewModel>()
    private val menuMakanan: ArrayList<MenuViewModel> = ArrayList<MenuViewModel>()
    private val tempMenuMakanan: ArrayList<MenuViewModel> = ArrayList<MenuViewModel>()
    private val menuMinuman: ArrayList<MenuViewModel> = ArrayList<MenuViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater!!.inflate(com.malikrafsan.restaurant_mobile_app.R.layout.fragment_menu, container, false)

        makananRecyclerView = view.findViewById(com.malikrafsan.restaurant_mobile_app.R.id.menuMakananRecyclerView)
        makananRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        loadMenu()
//        loadFoodMenu()
//        loadDrinkMenu()
        makananAdapter = MenuAdapter(tempMenuMakanan)
        makananRecyclerView.adapter = makananAdapter

//        minumanRecyclerView = view.findViewById(com.malikrafsan.restaurant_mobile_app.R.id.menuMinumanRecyclerView)
//        minumanRecyclerView.layoutManager = LinearLayoutManager(requireContext())
//        minumanAdapter = MenuAdapter(menuMinuman)
//        minumanRecyclerView.adapter = minumanAdapter

        searchView = view.findViewById(com.malikrafsan.restaurant_mobile_app.R.id.searchView)
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val searchText = newText!!.lowercase()
                if (searchText.isNotEmpty()) {
                    tempMenuMakanan.clear()
                    menuMakanan.forEach {
                        if (it.name.lowercase().contains(searchText)) {
                            tempMenuMakanan.add((it))
                        }
                    }
                    makananRecyclerView.adapter!!.notifyDataSetChanged()
                } else {
                    tempMenuMakanan.clear()
                    tempMenuMakanan.addAll(menuMakanan)
                    makananRecyclerView.adapter!!.notifyDataSetChanged()
                }

                return true
            }
        })



        return view
    }

    private fun loadMenu() {
        val menu = ApiBuilder.buildApi(MenuApi::class.java)
        val requestCall = menu.getMenu()

        requestCall.enqueue(object : Callback<com.malikrafsan.restaurant_mobile_app.dto.Menu> {
            override fun onResponse(call: Call<com.malikrafsan.restaurant_mobile_app.dto.Menu>, response: Response<com.malikrafsan.restaurant_mobile_app.dto.Menu>) {
                if (response.isSuccessful) {
                    val fetchedMenu: com.malikrafsan.restaurant_mobile_app.dto.Menu? = response.body()

                    fetchedMenu?.data?.forEach {
                        val m : MenuViewModel = MenuViewModel(it.name,it.currency, it.price, it.sold, it.description, it.type)
                        allMenu.add(m)
                        if (it.type == "Food") {
                            menuMakanan.add(m)
                            tempMenuMakanan.add(m)
                        } else {
                            menuMinuman.add(m)
                        }
                    }
                }
            }
            override fun onFailure(call: Call<com.malikrafsan.restaurant_mobile_app.dto.Menu>, t: Throwable) {
                Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadFoodMenu() {
        val menu = ApiBuilder.buildApi(MenuApi::class.java)
        val requestCall = menu.getFoodMenu()

        requestCall.enqueue(object : Callback<com.malikrafsan.restaurant_mobile_app.dto.Menu> {
            override fun onResponse(call: Call<com.malikrafsan.restaurant_mobile_app.dto.Menu>, response: Response<com.malikrafsan.restaurant_mobile_app.dto.Menu>) {
                if (response.isSuccessful) {
                    val fetchedMenu: com.malikrafsan.restaurant_mobile_app.dto.Menu? = response.body()

                    fetchedMenu?.data?.forEach{
                        menuMakanan.add(MenuViewModel(it.name,it.currency, it.price, it.sold, it.description, it.type))
                        tempMenuMakanan.add(MenuViewModel(it.name,it.currency, it.price, it.sold, it.description, it.type))
                    }
                }
            }
            override fun onFailure(call: Call<com.malikrafsan.restaurant_mobile_app.dto.Menu>, t: Throwable) {
                Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadDrinkMenu() {
        val menu = ApiBuilder.buildApi(MenuApi::class.java)
        val requestCall = menu.getDrinkMenu()

        requestCall.enqueue(object : Callback<com.malikrafsan.restaurant_mobile_app.dto.Menu> {
            override fun onResponse(call: Call<com.malikrafsan.restaurant_mobile_app.dto.Menu>, response: Response<com.malikrafsan.restaurant_mobile_app.dto.Menu>) {
                if (response.isSuccessful) {
                    val fetchedMenu: com.malikrafsan.restaurant_mobile_app.dto.Menu? = response.body()

                    fetchedMenu?.data?.forEach{
                        menuMinuman.add(MenuViewModel(it.name,it.currency, it.price, it.sold, it.description, it.type))
                    }
                }
            }
            override fun onFailure(call: Call<com.malikrafsan.restaurant_mobile_app.dto.Menu>, t: Throwable) {
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