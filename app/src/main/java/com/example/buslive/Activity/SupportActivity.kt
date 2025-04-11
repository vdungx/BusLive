package com.example.buslive.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.buslive.R

class SupportActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_support)

        findViewById<TextView>(R.id.textViewQuestion1).setOnClickListener {
            showAnswer("Bạn có thể đặt vé bằng cách chọn điểm xuất phát, điểm đến, ngày đi, và nhấn vào nút 'Tìm chuyến'.")
        }

        findViewById<TextView>(R.id.textViewQuestion2).setOnClickListener {
            showAnswer("Có, bạn có thể thay đổi hoặc hủy vé đã đặt.")
        }

        findViewById<TextView>(R.id.textViewQuestion3).setOnClickListener {
            showAnswer("Bạn có thể thanh toán qua thẻ ngân hàng hoặc ví điện tử.")
        }

        findViewById<TextView>(R.id.textViewQuestion4).setOnClickListener {
            showAnswer("Bạn có thể xem thông tin chuyến đi qua chi tiết vé.")
        }

        val btnBack = findViewById<ImageView>(R.id.btn_back)
        btnBack.setOnClickListener {
            finish() // Quay lại
        }

        val btnLienHe = findViewById<Button>(R.id.btnLienHe)
        btnLienHe.setOnClickListener {
            val intent = Intent(this, GopyActivity::class.java)
            startActivity(intent)
        }
    }
    private fun showAnswer(answer: String) {
        AlertDialog.Builder(this)
            .setTitle("Câu trả lời")
            .setMessage(answer)
            .setPositiveButton("OK", null)
            .show()
    }
}