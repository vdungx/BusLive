package com.example.buslive.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.buslive.R

class GopyActivity : AppCompatActivity() {

    private lateinit var edtEmail: EditText
    private lateinit var edtPhone: EditText
    private lateinit var edtDescription: EditText
    private lateinit var btnSend: Button
    private lateinit var btnBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gopy)

        // Ánh xạ view
        edtEmail = findViewById(R.id.edtEmail)
        edtPhone = findViewById(R.id.edtPhone)
        edtDescription = findViewById(R.id.edtDescription)
        btnSend = findViewById(R.id.btnSend)
        btnBack = findViewById(R.id.btn_backgopy)

        // Sự kiện gửi góp ý
        btnSend.setOnClickListener { sendFeedback() }

        // Nút quay lại
        btnBack.setOnClickListener { finish() }
    }

    private fun sendFeedback() {
        val email = edtEmail.text.toString().trim()
        val phone = edtPhone.text.toString().trim()
        val description = edtDescription.text.toString().trim()
        val recipient = "dungskbg2004@gmail.com"

        // Kiểm tra hợp lệ
        if (email.isEmpty() || phone.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.error = "Email không hợp lệ"
            edtEmail.requestFocus()
            return
        }

        // Nội dung email
        val emailBody = """
            Email: $email
            Số điện thoại: $phone
            Mô tả: $description
        """.trimIndent()

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
            putExtra(Intent.EXTRA_SUBJECT, "Góp ý từ $email")
            putExtra(Intent.EXTRA_TEXT, emailBody)
        }

        startActivity(Intent.createChooser(intent, "Gửi góp ý qua:"))
    }
}
