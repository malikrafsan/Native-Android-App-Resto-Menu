package com.malikrafsan.restaurant_mobile_app.ui.splash_screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import com.malikrafsan.restaurant_mobile_app.MainActivity
import com.malikrafsan.restaurant_mobile_app.R

class SplashActivity : AppCompatActivity() {
    private lateinit var imgView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        supportActionBar?.hide()

        val imgAnim = AnimationUtils.loadAnimation(this, R.anim.grow_fade_in)
        imgView = findViewById(R.id.imageView)
        imgView.startAnimation(imgAnim)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}