package com.example.buslive.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.buslive.R
import com.google.firebase.auth.FirebaseAuth

class ForgetPasswordActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_pass)

        auth = FirebaseAuth.getInstance()

        // Khai báo các thành phần UI
        val edtEmail = findViewById<EditText>(R.id.edt_emailforgetpassword)
        val btnSend = findViewById<Button>(R.id.btn_send)
        val btnBack = findViewById<ImageView>(R.id.btn_back)

        // Gửi email đặt lại mật khẩu
        btnSend.setOnClickListener {
            val email = edtEmail.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập email!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            this,
                            "Email đặt lại mật khẩu đã được gửi!",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            this,
                            "Lỗi: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        // Quay lại màn hình đăng nhập
        btnBack.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
