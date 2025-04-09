package com.example.buslive.Activity

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.buslive.R
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login) // Đảm bảo rằng layout này là giao diện login của bạn

        auth = FirebaseAuth.getInstance()

        val emailLogin = findViewById<EditText>(R.id.edt_username) // ID của EditText nhập email
        val passwordLogin = findViewById<EditText>(R.id.edt_password) // ID của EditText nhập mật khẩu
        val btnLogin = findViewById<Button>(R.id.btn_login) // ID của nút Đăng Nhập (LinearLayout)
        val tvForgotPassword = findViewById<TextView>(R.id.txt_forgetpassword) // ID của TextView "Quên mật khẩu?"
        val tvRegister = findViewById<TextView>(R.id.txt_signup) // ID của TextView "Đăng Ký Ngay"

        val imgToggle = findViewById<ImageView>(R.id.img_showpassword)

        imgToggle.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                // Hiện mật khẩu
                passwordLogin.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                imgToggle.setImageResource(R.drawable.eye)
            } else {
                // Ẩn mật khẩu
                passwordLogin.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                imgToggle.setImageResource(R.drawable.eye)
            }
            // Giữ con trỏ ở cuối văn bản
            passwordLogin.setSelection(passwordLogin.text.length)
        }

        // Xử lý sự kiện khi bấm vào nút Đăng Nhập
        btnLogin.setOnClickListener {
            val input = emailLogin.text.toString().trim()
            val password = passwordLogin.text.toString().trim()

            if (input.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(input, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java)) // Navigate to the main activity
                    finish()
                } else {
                    Toast.makeText(this, "Lỗi: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }

//            when {
//                input.contains("@") -> signInWithEmail(input, password) // Login with email
//                input.all { it.isDigit() } -> findEmailByPhone(input, password) // Login with phone number
//                else -> findEmailByUsername(input, password) // Login with username
//            }
        }

        // Xử lý khi bấm vào "Quên mật khẩu?"
        tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgetPasswordActivity::class.java))
        }

        // Xử lý khi bấm vào "Đăng Ký Ngay"
        tvRegister.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
//    private fun findEmailByPhone(phone: String, password: String) {
//        val usersRef = FirebaseDatabase.getInstance().getReference("Users")
//
//        usersRef.orderByChild("phone").equalTo(phone).get().addOnSuccessListener { snapshot ->
//            val email = snapshot.children.firstOrNull()?.child("email")?.getValue(String::class.java)
//            if (email != null) {
//                signInWithEmail(email, password)
//            } else {
//                Toast.makeText(this, "Số điện thoại không tồn tại!", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    private fun findEmailByUsername(username: String, password: String) {
//        val usersRef = FirebaseDatabase.getInstance().getReference("Users")
//
//        usersRef.orderByChild("username").equalTo(username).get().addOnSuccessListener { snapshot ->
//            val email = snapshot.children.firstOrNull()?.child("email")?.getValue(String::class.java)
//            if (email != null) {
//                signInWithEmail(email, password)
//            } else {
//                Toast.makeText(this, "Tên đăng nhập không tồn tại!", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

//    private fun signInWithEmail(email: String, password: String) {
//        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
//                startActivity(Intent(this, LoginActivity::class.java)) // Navigate to the main activity
//                finish()
//            } else {
//                Toast.makeText(this, "Lỗi: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
}