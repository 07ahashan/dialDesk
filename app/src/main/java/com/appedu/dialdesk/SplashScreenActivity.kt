package com.appedu.dialdesk

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.appedu.dialdesk.databinding.ActivitySplashScreenBinding
import com.appedu.dialdesk.ui.HomeScreenActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var splashBinding : ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashBinding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(splashBinding.root)

        lifecycleScope.launch {
            delay(2500)
            startActivity(Intent(this@SplashScreenActivity, HomeScreenActivity::class.java))
            finish()
        }
    }
}