package com.malikrafsan.restaurant_mobile_app.ui.restaurant

import android.os.Bundle
import android.os.Parcelable
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
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.lifecycle.lifecycleScope

class RestaurantFragment : Fragment() {

    private var _binding = null
    private var datarestaurant = ArrayList<RestaurantViewModel>()
    private lateinit var recyclerView: RecyclerView
    private val TAG = "RestaurantFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater!!.inflate(com.malikrafsan.restaurant_mobile_app.R.layout.fragment_restaurant, container, false)

        recyclerView = view.findViewById<RecyclerView>(com.malikrafsan.restaurant_mobile_app.R.id.restaurantRecyclerview)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        Log.d(TAG, "onCreateView: $datarestaurant")

        val deferredResult = GlobalScope.async(Dispatchers.Default) {
            val branch = ApiBuilder.buildApi(BranchApi::class.java)
            val requestCall = branch.getBranch()
            requestCall.enqueue(object : Callback<Branch> {
                override fun onResponse(call: Call<Branch>, response: Response<Branch>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            val fetchedMenu: Branch? = response.body()
                            fetchedMenu?.data?.forEach{
                                datarestaurant.add(RestaurantViewModel(it.name, it.address, it.phone_number, it.latitude, it.longitude))
                            }
                            datarestaurant.sortBy {
                                it.namaRestaurant
                            }

                            val adapter = RestaurantAdapter(datarestaurant)
                            recyclerView.adapter = adapter
                        }
                    } else {
                        Toast.makeText(context, "Error ${response.message()} ${response.errorBody()}", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<Branch>, t: Throwable) {
                     Toast.makeText(context, "${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
        lifecycleScope.launch {
            deferredResult.await()
        }

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val state = recyclerView.layoutManager?.onSaveInstanceState()

        outState.putParcelable("state", state)
        outState.putParcelableArrayList("data", datarestaurant)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        if (savedInstanceState != null) {
            val state = savedInstanceState.getParcelable<Parcelable>("state")
            val data = savedInstanceState.getParcelableArrayList<RestaurantViewModel>("data")

            for (i in data!!) {
                datarestaurant.add(i)
            }

            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.layoutManager?.onRestoreInstanceState(state)
            recyclerView.adapter = RestaurantAdapter(data)
        }
    }
}