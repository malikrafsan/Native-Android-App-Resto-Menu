package com.malikrafsan.restaurant_mobile_app.ui.restaurant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.malikrafsan.restaurant_mobile_app.api.BranchApi
import com.malikrafsan.restaurant_mobile_app.builder.ApiBuilder
import com.malikrafsan.restaurant_mobile_app.dto.Branch
import com.malikrafsan.restaurant_mobile_app.ui.keranjang.RestaurantViewModel
import com.malikrafsan.restaurant_mobile_app.ui.menu.MenuAdapter
import com.malikrafsan.restaurant_mobile_app.ui.menu.MenuViewModel
import kotlinx.coroutines.*
import kotlin.system.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.lifecycle.lifecycleScope

class RestaurantFragment : Fragment() {

    private var _binding = null
    var datarestaurant = ArrayList<RestaurantViewModel>()
    private lateinit var recyclerView: RecyclerView
    // var restaurantRecyclerview = null
    // This property is only valid between onCreateView and
    // onDestroyView.

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater!!.inflate(com.malikrafsan.restaurant_mobile_app.R.layout.fragment_restaurant, container, false)
        // getting the recyclerview by its id
        var restaurantRecyclerview = view.findViewById<RecyclerView>(com.malikrafsan.restaurant_mobile_app.R.id.restaurantRecyclerview)

        // this creates a vertical layout Manager
        restaurantRecyclerview.layoutManager = LinearLayoutManager(requireContext())

        // ArrayList of class ItemsViewModel

        // This loop will create 20 Views containing
        // the image with the count of view
        val deferredResult = GlobalScope.async(Dispatchers.Default) {
            val branch = ApiBuilder.buildApi(BranchApi::class.java)
            val requestCall = branch.getBranch()
            requestCall.enqueue(object : Callback<Branch> {
                override fun onResponse(call: Call<Branch>, response: Response<Branch>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            val fetchedMenu: com.malikrafsan.restaurant_mobile_app.dto.Branch? = response.body()
                            fetchedMenu?.data?.forEach{
                                datarestaurant.add(RestaurantViewModel(it.name, it.address, it.contact_person, it.latitude, it.longitude))
                            }
                            val adapter = RestaurantAdapter(datarestaurant)

                            // Setting the Adapter with the recyclerview
                            restaurantRecyclerview.adapter = adapter
                            recyclerView = restaurantRecyclerview
                            // Toast.makeText(context, "$it", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                override fun onFailure(call: Call<Branch>, t: Throwable) {
                    // Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
        lifecycleScope.launch {
            deferredResult.await()
        }

        // This will pass the ArrayList to our Adapter
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadBranch() {
        val branch = ApiBuilder.buildApi(BranchApi::class.java)
        val requestCall = branch.getBranch()

        requestCall.enqueue(object : Callback<Branch> {
            override fun onResponse(call: Call<Branch>, response: Response<Branch>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        val fetchedMenu: com.malikrafsan.restaurant_mobile_app.dto.Branch? = response.body()
                        println("MASUK")
                        fetchedMenu?.data?.forEach{
                            println(fetchedMenu.data)
                            datarestaurant.add(RestaurantViewModel(it.name, it.address, it.contact_person, it.latitude, it.longitude))
                        }
                        // Toast.makeText(context, "$it", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            override fun onFailure(call: Call<Branch>, t: Throwable) {
                // Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}