package com.malikrafsan.restaurant_mobile_app.ui.twibbon

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.malikrafsan.restaurant_mobile_app.R
import com.malikrafsan.restaurant_mobile_app.databinding.FragmentTwibbonBinding

class TwibbonFragment : Fragment() {
    private var _binding: FragmentTwibbonBinding? = null
    private val binding get() = _binding!!

    private var permissionRequired = arrayOf(Manifest.permission.CAMERA)
    private var imagePreview : Preview? = null
    private var cameraSelector : CameraSelector? = null
    private lateinit var cameraProvider : ProcessCameraProvider
    private var freeze = false
    private lateinit var captionBtn: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this._binding = FragmentTwibbonBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val checkPermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
        permission ->
            var isGranted = true

            permission.entries.forEach{
                if(it.key in permissionRequired && !it.value) isGranted = false
            }

            if(!isGranted){
                Toast.makeText(context, "PERMISSION REQUEST DENIED", Toast.LENGTH_LONG).show()
            }else{
                setupCam()
            }
    }

    private fun setupCam() {
        val camProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraSelector = CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_FRONT).build()

        camProviderFuture.addListener({
            imagePreview = Preview.Builder().apply {
                setTargetAspectRatio(AspectRatio.RATIO_16_9)
                setTargetRotation(binding.twibbonImage.display.rotation)
            }.build()

            cameraProvider = camProviderFuture.get()
            cameraProvider.bindToLifecycle(this, cameraSelector!!, imagePreview)
            binding.twibbonImage.implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            binding.twibbonImage.overlay.add(binding.twibbonFrame)
            imagePreview?.setSurfaceProvider(binding.twibbonImage.surfaceProvider)
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun onCapture(){
        if (freeze) {
            imagePreview!!.setSurfaceProvider(null)
            this.captionBtn.text = "Take Again?"
        } else {
            imagePreview!!.setSurfaceProvider(binding.twibbonImage.surfaceProvider)
            this.captionBtn.text = "Capture"
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(hasPermissions(requireContext(), permissionRequired)){
            setupCam()
        }else{
            checkPermission.launch(permissionRequired)
        }

        registerElmt(view)

        freeze = false
        binding.captureIcon.setOnClickListener {
            freeze = !freeze
            onCapture()
        }
    }

    private fun registerElmt(view: View) {
        captionBtn = view.findViewById(R.id.capture_text)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        fun hasPermissions(context: Context, permission: Array<String>) = permission.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }
}