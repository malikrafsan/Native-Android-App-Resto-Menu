package com.malikrafsan.restaurant_mobile_app.ui.scan_payment

import android.Manifest
import android.Manifest.permission.CAMERA
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.budiyev.android.codescanner.*
import com.malikrafsan.restaurant_mobile_app.MainActivity
import com.malikrafsan.restaurant_mobile_app.R
import com.malikrafsan.restaurant_mobile_app.api.Payment
import com.malikrafsan.restaurant_mobile_app.builder.ApiBuilder
import com.malikrafsan.restaurant_mobile_app.databinding.ActivityScanPaymentBinding
import com.malikrafsan.restaurant_mobile_app.dto.PayResponse
import com.malikrafsan.restaurant_mobile_app.entity.Cart
import com.malikrafsan.restaurant_mobile_app.ui.shared.HeaderFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class ScanPaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScanPaymentBinding
    private lateinit var codeScanner: CodeScanner
    private lateinit var scanStatusLayout: LinearLayout
    private lateinit var totalPriceTextView: TextView
    private lateinit var imageViewStatusIcon: ImageView
    private lateinit var textViewStatus: TextView
    private lateinit var textViewDescription: TextView
    private lateinit var viewModel: ScanPaymentViewModel
    private lateinit var headerFragment: HeaderFragment
    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationChannel: NotificationChannel
    private lateinit var builder: Notification.Builder

    private val CHANNEL_ID = "i.apps.notifications"
    private val descChannel = "Scan Payment notification"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        headerFragment = supportFragmentManager.findFragmentById(R.id.header_fragment) as HeaderFragment
        headerFragment.setTempTextVisibility(View.GONE)
        headerFragment.setArrowBackVisibility(View.VISIBLE)
        headerFragment.setHeaderText("Pembayaran")

        viewModel = ViewModelProvider(this).get(ScanPaymentViewModel::class.java)

        scanStatusLayout = findViewById(R.id.scanStatusLayout)
        totalPriceTextView = findViewById(R.id.totalPriceTextView)
        imageViewStatusIcon = findViewById(R.id.imageViewStatusIcon)
        textViewStatus = findViewById(R.id.textViewStatus)
        textViewDescription = findViewById(R.id.textViewDescription)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.carts.collect {
                    calculateTotalPrice(it)
                }
            }
        }

        if (ContextCompat.checkSelfPermission(this, CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, arrayOf(CAMERA), 123)
        } else {
            startScanning()
        }
    }

    private fun calculateTotalPrice(carts: List<Cart>) {
        var total = 0
        for (cart in carts) {
            total += cart.qty * cart.price
        }
        this.totalPriceTextView.text = getString(R.string.hargaMakanan, "IDR", total.toString().reversed().chunked(3).joinToString(".").reversed())
    }

    private fun startScanning() {
        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)
        codeScanner = CodeScanner(this, scannerView)
        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.formats = CodeScanner.ALL_FORMATS

        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.scanMode = ScanMode.SINGLE
        codeScanner.isAutoFocusEnabled = true
        codeScanner.isFlashEnabled = false

        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                Toast.makeText(this, "Paying", Toast.LENGTH_SHORT).show()
                pay(it.text)
            }
        }

        codeScanner.errorCallback = ErrorCallback {
            runOnUiThread {
                Toast.makeText(this, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_SHORT).show()
            }
        }

        scannerView.setOnClickListener {
            hideStatus()
            codeScanner.startPreview()
        }
    }

    private fun pay(token: String) {
        val paymentApi = ApiBuilder.buildApi(Payment::class.java)
        val requestCall = paymentApi.pay(token)

        requestCall.enqueue(object : Callback<PayResponse> {
            @RequiresApi(Build.VERSION_CODES.TIRAMISU)
            override fun onResponse(call: Call<PayResponse>, response: Response<PayResponse>) {
                Log.d("PAYMENT", "ON RESPONSE ${response.body()}")

                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.status == "FAILED") {
                            Log.d("PAYMENT", "FAILED: $it")
                            Log.d("PAYMENT", it.toString())

                            createNotif(false, "Gagal", "Pesanan gagal dibayarkan")
                            displayStatus(false, "Gagal", "Pesanan gagal dibayarkan")

                            val newIntent = Intent(this@ScanPaymentActivity, ScanPaymentActivity::class.java)
                            startActivity(newIntent)
                            finish()
                            return
                        }

                        Log.d("PAYMENT", "SUCCESSFUL: $it")
                        Log.d("PAYMENT", it.toString())

                        createNotif(true, "Berhasil", "Pesanan berhasil dibayarkan")
                        displayStatus(true, "Berhasil", "Pesanan berhasil dibayarkan")
                        lifecycleScope.launch {
                            viewModel.clearCart()
                            Handler(Looper.getMainLooper()).postDelayed({
                                val newIntent = Intent(this@ScanPaymentActivity, MainActivity::class.java)
                                startActivity(newIntent)
                                finish()
                            }, 5000)
                        }

                    }
                } else {
                    Log.d("PAYMENT", "UNSUCCESSFUL: " + response.errorBody().toString())

                    createNotif(false, "Gagal", "Pesanan gagal dibayarkan")
                    displayStatus(false, "Gagal", "Pesanan gagal dibayarkan")
                    val newIntent = Intent(this@ScanPaymentActivity, ScanPaymentActivity::class.java)
                    startActivity(newIntent)
                    finish()
                }
            }
            @RequiresApi(Build.VERSION_CODES.TIRAMISU)
            override fun onFailure(call: Call<PayResponse>, t: Throwable) {
                Log.d("PAYMENT", "FAILED: " + t.message.toString())

                createNotif(false, "Gagal", "Pesanan gagal dibayarkan")
                displayStatus(false, "Gagal", "Pesanan gagal dibayarkan")
                val newIntent = Intent(this@ScanPaymentActivity, ScanPaymentActivity::class.java)
                startActivity(newIntent)
                finish()
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun createNotif(isSuccess: Boolean, statusMsg: String, statusDesc: String) {
        val intent = Intent(this, ScanPaymentActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val contentView = RemoteViews(packageName, R.layout.notif_layout)
        val title = "Status Pembayaran Majika"
        val icon = if (isSuccess) R.drawable.ok else R.drawable.cancel

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(CHANNEL_ID, descChannel, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(this, CHANNEL_ID)
                .setContent(contentView)
                .setContentTitle(title)
                .setContentText(statusMsg)
                .setStyle(Notification.BigTextStyle()
                    .bigText(statusDesc))
                .setSmallIcon(icon)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources, icon))
                .setContentIntent(pendingIntent)
        } else {
            builder = Notification.Builder(this)
                .setContent(contentView)
                .setContentTitle(title)
                .setContentText(statusMsg)
                .setStyle(Notification.BigTextStyle()
                    .bigText(statusDesc))
                .setSmallIcon(icon)
                .setLargeIcon(BitmapFactory.decodeResource(this.resources, icon))
                .setContentIntent(pendingIntent)
        }

        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    this@ScanPaymentActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this@ScanPaymentActivity,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    123)
                return
            }
            notify(1, builder.build())
        }
//        notificationManager.notify(1234, builder.build())
    }

    private fun displayStatus(isSuccess: Boolean, statusMsg: String, statusDesc: String) {
        this.scanStatusLayout.visibility = LinearLayout.VISIBLE
        if (isSuccess) {
            this.imageViewStatusIcon.setImageResource(R.drawable.ok)
        } else {
            this.imageViewStatusIcon.setImageResource(R.drawable.cancel)
        }

        this.textViewStatus.text = statusMsg
        this.textViewDescription.text = statusDesc

        if (isSuccess) {
            val imgAnim = AnimationUtils.loadAnimation(this, R.anim.grow_fade_in)
            this.imageViewStatusIcon.startAnimation(imgAnim)
        }
    }

    private fun hideStatus() {
        this.scanStatusLayout.visibility = LinearLayout.GONE
        this.imageViewStatusIcon.setImageResource(0)
        this.textViewStatus.text = ""
        this.textViewDescription.text = ""
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 123 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startScanning()
        } else {
            Toast.makeText(this, "Camera permission is required to use this app.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onResume() {
        super.onResume()
        if (::codeScanner.isInitialized) {
            codeScanner.startPreview()
        }
    }

    override fun onPause() {
        if (::codeScanner.isInitialized) {
            codeScanner.releaseResources()
        }

        super.onPause()
    }
}