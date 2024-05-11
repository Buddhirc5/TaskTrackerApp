package com.example.labexam4

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()

        val intent = Intent(this, MainActivity::class.java)
        val runnable = Runnable {
            startActivity(intent)
            finish()
        }
        Handler(Looper.getMainLooper()).postDelayed(runnable, 1000)
    }
}