package com.malikrafsan.restaurant_mobile_app.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
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
    private val menuMinuman: ArrayList<MenuViewModel> = ArrayList<MenuViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater!!.inflate(com.malikrafsan.restaurant_mobile_app.R.layout.fragment_menu, container, false)

        makananRecyclerView = view.findViewById(com.malikrafsan.restaurant_mobile_app.R.id.menuMakananRecyclerView)
        makananRecyclerView.layoutManager = LinearLayoutManager(requireContext())

//        loadMenu()
        loadFoodMenu()
//        loadDrinkMenu()
        makananAdapter = MenuAdapter(menuMakanan)
        makananRecyclerView.adapter = makananAdapter

//        minumanRecyclerView = view.findViewById(com.malikrafsan.restaurant_mobile_app.R.id.menuMinumanRecyclerView)
//        minumanRecyclerView.layoutManager = LinearLayoutManager(requireContext())
//        minumanAdapter = MenuAdapter(menuMinuman)
//        minumanRecyclerView.adapter = minumanAdapter



        return view
    }

    private fun loadMenu() {
        val menu = ApiBuilder.buildApi(MenuApi::class.java)
        val requestCall = menu.getMenu()

        requestCall.enqueue(object : Callback<com.malikrafsan.restaurant_mobile_app.dto.Menu> {
            override fun onResponse(call: Call<com.malikrafsan.restaurant_mobile_app.dto.Menu>, response: Response<com.malikrafsan.restaurant_mobile_app.dto.Menu>) {
                if (response.isSuccessful) {
                    val fetchedMenu: com.malikrafsan.restaurant_mobile_app.dto.Menu? = response.body()

                    fetchedMenu?.data?.forEach{
                        allMenu.add(MenuViewModel(it.name,it.currency, it.price, it.sold, it.description, it.type))
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