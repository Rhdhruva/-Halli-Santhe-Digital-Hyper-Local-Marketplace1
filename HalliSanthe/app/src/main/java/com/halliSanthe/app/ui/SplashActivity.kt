package com.halliSanthe.app.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.halliSanthe.app.databinding.ActivitySplashBinding
import com.halliSanthe.app.ui.artisan.ArtisanDashboardActivity
import com.halliSanthe.app.ui.buyer.MainActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBuyer.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.btnArtisan.setOnClickListener {
            startActivity(Intent(this, ArtisanDashboardActivity::class.java))
        }
    }
}
