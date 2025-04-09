package com.example.buslive.Activity

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.buslive.R

class SupportActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_support)

        val btnBack = findViewById<ImageView>(R.id.btn_back)
        btnBack.setOnClickListener {
            finish() // Quay lại
        }

        val btnLienHe = findViewById<Button>(R.id.btnLienHe)
        btnLienHe.setOnClickListener {
            Toast.makeText(this, "Liên hệ với BusLive", Toast.LENGTH_SHORT).show()
            // Hoặc mở Chat, Email, Messenger...
        }
    }
}