package com.example.buslive

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.buslive.Activity.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class ForgetPasswordActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_pass) // Đảm bảo layout này là giao diện quên mật khẩu của bạn

        auth = FirebaseAuth.getInstance()

        val emailEditText = findViewById<EditText>(R.id.edt_emailforgetpassword) // ID của EditText nhập email
        val sendButton = findViewById<Button>(R.id.btn_send) // ID của nút Gửi

        // Xử lý khi nhấn nút "Gửi"
        sendButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()

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
                        Toast.makeText(this, "Lỗi: ${task.exception?.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }

        // Xử lý khi nhấn nút quay lại
        val tvbacktologin = findViewById<ImageView>(R.id.btn_back)
        tvbacktologin.setOnClickListener {
            // Chuyển sang màn hình đăng ký
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}