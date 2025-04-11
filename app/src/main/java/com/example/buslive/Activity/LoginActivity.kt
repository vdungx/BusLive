package com.example.buslive.Activity

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.buslive.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        // Khai báo View
        val edtInput = findViewById<EditText>(R.id.edt_username)
        val edtPassword = findViewById<EditText>(R.id.edt_password)
        val btnLogin = findViewById<Button>(R.id.btn_login)
        val tvForgotPassword = findViewById<TextView>(R.id.txt_forgetpassword)
        val tvRegister = findViewById<TextView>(R.id.txt_signup)
        val imgTogglePassword = findViewById<ImageView>(R.id.img_showpassword)

        // Toggle hiển thị mật khẩu
        imgTogglePassword.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            edtPassword.inputType = if (isPasswordVisible)
                InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            else
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

            imgTogglePassword.setImageResource(R.drawable.eye)
            edtPassword.setSelection(edtPassword.text.length)
        }

        // Sự kiện khi nhấn nút Đăng Nhập
        btnLogin.setOnClickListener {
            val input = edtInput.text.toString().trim()
            val password = edtPassword.text.toString().trim()

            if (input.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (Patterns.EMAIL_ADDRESS.matcher(input).matches()) {
                // Nếu là email hợp lệ
                signInWithEmail(input, password)
            } else {
                val usersRef = FirebaseDatabase.getInstance().getReference("users")

                // Kiểm tra theo username
                usersRef.orderByChild("username").equalTo(input)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                for (userSnapshot in snapshot.children) {
                                    val email = userSnapshot.child("email").getValue(String::class.java)
                                    if (!email.isNullOrEmpty()) {
                                        signInWithEmail(email, password)
                                        return
                                    }
                                }
                            } else {
                                // Nếu không phải username thì kiểm tra theo số điện thoại
                                usersRef.orderByChild("phone").equalTo(input)
                                    .addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            if (snapshot.exists()) {
                                                for (userSnapshot in snapshot.children) {
                                                    val email = userSnapshot.child("email").getValue(String::class.java)
                                                    if (!email.isNullOrEmpty()) {
                                                        signInWithEmail(email, password)
                                                        return
                                                    }
                                                }
                                            } else {
                                                Toast.makeText(this@LoginActivity, "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show()
                                            }
                                        }

                                        override fun onCancelled(error: DatabaseError) {}
                                    })
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {}
                    })
            }
        }

        // Chuyển đến màn hình Quên mật khẩu
        tvForgotPassword.setOnClickListener {
            startActivity(Intent(this, ForgetPasswordActivity::class.java))
        }

        // Chuyển đến màn hình Đăng ký
        tvRegister.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }

    private fun signInWithEmail(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Lỗi: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
