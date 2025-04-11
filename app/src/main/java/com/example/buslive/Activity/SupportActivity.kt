package com.example.buslive.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.buslive.R

class SupportActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_support)

        // Khai báo câu hỏi & câu trả lời tương ứng
        val faqs = mapOf(
            R.id.textViewQuestion1 to "Bạn có thể đặt vé bằng cách chọn điểm xuất phát, điểm đến, ngày đi, và nhấn vào nút 'Tìm chuyến'.",
            R.id.textViewQuestion2 to "Có, bạn có thể thay đổi hoặc hủy vé đã đặt.",
            R.id.textViewQuestion3 to "Bạn có thể thanh toán qua thẻ ngân hàng hoặc ví điện tử.",
            R.id.textViewQuestion4 to "Bạn có thể xem thông tin chuyến đi qua chi tiết vé."
        )

        // Gắn sự kiện click cho từng câu hỏi
        for ((questionId, answer) in faqs) {
            findViewById<TextView>(questionId).setOnClickListener {
                showAnswer(answer)
            }
        }

        // Nút quay lại
        findViewById<ImageView>(R.id.btn_back).setOnClickListener {
            finish()
        }

        // Nút liên hệ
        findViewById<Button>(R.id.btnLienHe).setOnClickListener {
            startActivity(Intent(this, GopyActivity::class.java))
        }
    }

    // Hiển thị dialog câu trả lời
    private fun showAnswer(answer: String) {
        AlertDialog.Builder(this)
            .setTitle("Câu trả lời")
            .setMessage(answer)
            .setPositiveButton("OK", null)
            .show()
    }
}
