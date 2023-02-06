package com.malikrafsan.restaurant_mobile_app

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.malikrafsan.restaurant_mobile_app.api.BranchApi
import com.malikrafsan.restaurant_mobile_app.api.MenuApi
import com.malikrafsan.restaurant_mobile_app.builder.ApiBuilder
import com.malikrafsan.restaurant_mobile_app.databinding.ActivityMainBinding
import com.malikrafsan.restaurant_mobile_app.dto.Branch
import com.malikrafsan.restaurant_mobile_app.dto.Menu

import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

class MainActivity : AppCompatActivity() {

private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

     binding = ActivityMainBinding.inflate(layoutInflater)
     setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        loadMenu()
        loadFoodMenu()
        loadDrinkMenu()
        loadBranch()
    }

    private fun loadMenu() {
        val menu = ApiBuilder.buildApi(MenuApi::class.java)
        val requestCall = menu.getMenu()

        val textViewMenu = this.findViewById<TextView>(R.id.textViewMenu)
        requestCall.enqueue(object : Callback<Menu> {
            override fun onResponse(call: Call<Menu>, response: Response<Menu>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        textViewMenu.text = it.toString()
                    }
                }
            }
            override fun onFailure(call: Call<Menu>, t: Throwable) {
                Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                textViewMenu.text = t.message
            }
        })
    }

    private fun loadFoodMenu() {
        val menu = ApiBuilder.buildApi(MenuApi::class.java)
        val requestCall = menu.getFoodMenu()

        val textViewFoodMenu = this.findViewById<TextView>(R.id.textViewFoodMenu)
        requestCall.enqueue(object : Callback<Menu> {
            override fun onResponse(call: Call<Menu>, response: Response<Menu>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        textViewFoodMenu.text = it.toString()
                    }
                }
            }
            override fun onFailure(call: Call<Menu>, t: Throwable) {
                Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                textViewFoodMenu.text = t.message
            }
        })
    }

    private fun loadDrinkMenu() {
        val menu = ApiBuilder.buildApi(MenuApi::class.java)
        val requestCall = menu.getDrinkMenu()

        val textViewDrinkMenu = this.findViewById<TextView>(R.id.textViewDrinkMenu)
        requestCall.enqueue(object : Callback<Menu> {
            override fun onResponse(call: Call<Menu>, response: Response<Menu>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        textViewDrinkMenu.text = it.toString()
                    }
                }
            }
            override fun onFailure(call: Call<Menu>, t: Throwable) {
                Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                textViewDrinkMenu.text = t.message
            }
        })
    }

    private fun loadBranch() {
        val branch = ApiBuilder.buildApi(BranchApi::class.java)
        val requestCall = branch.getBranch()

        val textViewBranch = this.findViewById<TextView>(R.id.textViewBranch)
        requestCall.enqueue(object : Callback<Branch> {
            override fun onResponse(call: Call<Branch>, response: Response<Branch>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        textViewBranch.text = it.toString()
                    }
                }
            }
            override fun onFailure(call: Call<Branch>, t: Throwable) {
                Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
                textViewBranch.text = t.message
            }
        })
    }
}