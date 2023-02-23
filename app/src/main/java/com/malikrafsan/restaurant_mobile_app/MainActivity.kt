package com.malikrafsan.restaurant_mobile_app

import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
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
import com.malikrafsan.restaurant_mobile_app.ui.keranjang.KeranjangFragment
import com.malikrafsan.restaurant_mobile_app.ui.menu.MenuFragment
import com.malikrafsan.restaurant_mobile_app.ui.restaurant.RestaurantFragment
import com.malikrafsan.restaurant_mobile_app.ui.shared.HeaderFragment
import com.malikrafsan.restaurant_mobile_app.ui.twibbon.TwibbonFragment
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
    private lateinit var headerFragment: HeaderFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        headerFragment = supportFragmentManager.findFragmentById(R.id.header_fragment) as HeaderFragment

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_twibbon, R.id.navigation_lokasi, R.id.navigation_menu, R.id.navigation_keranjang))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.setBackgroundColor(resources.getColor(R.color.primary))

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        if (sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null) {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
            isTemperatureSensorAvailable = true
        } else {
            isTemperatureSensorAvailable = false
        }

        supportFragmentManager.registerFragmentLifecycleCallbacks(object : FragmentManager.FragmentLifecycleCallbacks() {
            override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
                super.onFragmentResumed(fm, f)
                when (f) {
                    is TwibbonFragment -> {
                        headerFragment.setHeaderText("Twibbon")
                        headerFragment.setArrowBackVisibility(View.GONE)
                        headerFragment.setTempTextVisibility(View.GONE)
                    }
                    is RestaurantFragment -> {
                        headerFragment.setHeaderText("Cabang Restoran")
                        headerFragment.setArrowBackVisibility(View.GONE)
                        headerFragment.setTempTextVisibility(View.GONE)
                    }
                    is MenuFragment -> {
                        headerFragment.setHeaderText("Menu")
                        headerFragment.setArrowBackVisibility(View.GONE)
                        headerFragment.setTempTextVisibility(View.VISIBLE)
                    }
                    is KeranjangFragment -> {
                        headerFragment.setHeaderText("Keranjang")
                        headerFragment.setArrowBackVisibility(View.GONE)
                        headerFragment.setTempTextVisibility(View.GONE)
                    }
                    else -> {

                    }
                }
            }
        }, true)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val temp = event?.values?.get(0)
        headerFragment.setTempText(temp)
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
}