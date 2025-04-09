package com.example.buslive.Activity

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.buslive.R

class GopyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gopy)
        val btnBack = findViewById<ImageView>(R.id.btn_backgopy)
        btnBack.setOnClickListener {
            finish() // Quay lại
        }
    }
}