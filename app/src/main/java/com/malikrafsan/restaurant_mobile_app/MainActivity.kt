package com.malikrafsan.restaurant_mobile_app

import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
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
import com.malikrafsan.restaurant_mobile_app.api.Payment
import com.malikrafsan.restaurant_mobile_app.builder.ApiBuilder
import com.malikrafsan.restaurant_mobile_app.databinding.ActivityMainBinding
import com.malikrafsan.restaurant_mobile_app.dto.Branch
import com.malikrafsan.restaurant_mobile_app.dto.Menu
import com.malikrafsan.restaurant_mobile_app.dto.PayResponse
import dagger.hilt.android.AndroidEntryPoint

import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), SensorEventListener {

private lateinit var binding: ActivityMainBinding
private lateinit var sensor: Sensor
private lateinit var sensorManager: SensorManager
private var isTemperatureSensorAvailable: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

     binding = ActivityMainBinding.inflate(layoutInflater)
     setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_twibbon, R.id.navigation_lokasi, R.id.navigation_menu, R.id.navigation_keranjang))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        if (sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null) {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
            isTemperatureSensorAvailable = true
        } else {
            findViewById<TextView>(R.id.textViewTemperature).text = "Temperature sensor is not available"
            isTemperatureSensorAvailable = false
        }

//        loadMenu()
//        loadFoodMenu()
//        loadDrinkMenu()
//        loadBranch()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        findViewById<TextView>(R.id.textViewTemperature).text = "Temperature: ${event?.values?.get(0)} C"
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d("MainActivity", "onAccuracyChanged: $accuracy")
    }

    override fun onResume() {
        super.onResume()
        if (isTemperatureSensorAvailable) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        if (isTemperatureSensorAvailable) {
            sensorManager.unregisterListener(this)
        }
    }

    private fun loadMenu() {
        val menu = ApiBuilder.buildApi(MenuApi::class.java)
        val requestCall = menu.getMenu()

        requestCall.enqueue(object : Callback<Menu> {
            override fun onResponse(call: Call<Menu>, response: Response<Menu>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        Toast.makeText(this@MainActivity, "$it", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            override fun onFailure(call: Call<Menu>, t: Throwable) {
                Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadFoodMenu() {
        val menu = ApiBuilder.buildApi(MenuApi::class.java)
        val requestCall = menu.getFoodMenu()

        requestCall.enqueue(object : Callback<Menu> {
            override fun onResponse(call: Call<Menu>, response: Response<Menu>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        Toast.makeText(this@MainActivity, "$it", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            override fun onFailure(call: Call<Menu>, t: Throwable) {
                Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadDrinkMenu() {
        val menu = ApiBuilder.buildApi(MenuApi::class.java)
        val requestCall = menu.getDrinkMenu()

        requestCall.enqueue(object : Callback<Menu> {
            override fun onResponse(call: Call<Menu>, response: Response<Menu>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        Toast.makeText(this@MainActivity, "$it", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            override fun onFailure(call: Call<Menu>, t: Throwable) {
                Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadBranch() {
        val branch = ApiBuilder.buildApi(BranchApi::class.java)
        val requestCall = branch.getBranch()

        requestCall.enqueue(object : Callback<Branch> {
            override fun onResponse(call: Call<Branch>, response: Response<Branch>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        Toast.makeText(this@MainActivity, "$it", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            override fun onFailure(call: Call<Branch>, t: Throwable) {
                Toast.makeText(this@MainActivity, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}