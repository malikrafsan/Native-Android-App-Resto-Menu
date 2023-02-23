package com.malikrafsan.restaurant_mobile_app.ui.shared

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.malikrafsan.restaurant_mobile_app.R
import com.malikrafsan.restaurant_mobile_app.databinding.FragmentHeaderBinding

class HeaderFragment : Fragment() {
    private lateinit var tempTextView: TextView
    private lateinit var headerTextView: TextView
    private lateinit var arrowBack: ImageView
    private var _binding: FragmentHeaderBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this._binding = FragmentHeaderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tempTextView = binding.tempText
        headerTextView = binding.headerText
        arrowBack = binding.arrowBack
        arrowBack.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    fun setHeaderText(text: String) {
        headerTextView.text = text
    }

    fun setArrowBackVisibility(visibility: Int) {
        arrowBack.visibility = visibility
    }

    fun setTempTextVisibility(visibility: Int) {
        tempTextView.visibility = visibility
    }

    fun setTempText(temp: Float?) {
        tempTextView.text = "$temp°C"
    }
}