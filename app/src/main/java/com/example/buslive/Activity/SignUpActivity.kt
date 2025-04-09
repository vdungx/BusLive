package com.example.buslive.Activity

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.buslive.Model.User
import com.example.buslive.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private var isPasswordVisible = false
    private var isPasswordVisible1 = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("users")

        val checkboxAgree = findViewById<CheckBox>(R.id.checksignup)
        val btnSignUp: Button = findViewById(R.id.btn_signup)

        btnSignUp.setOnClickListener {
            if (!checkboxAgree.isChecked) {
                Toast.makeText(this, "Bạn phải chấp nhận điều khoản để tiếp tục", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            registerUser()
        }

        val imgToggle = findViewById<ImageView>(R.id.img_showpassword)
        val imgToggle1 = findViewById<ImageView>(R.id.img_showpassword1)
        val password = findViewById<EditText>(R.id.edt_password)
        val confirmPassword = findViewById<EditText>(R.id.edt_confirmpassword)
        imgToggle.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                // Hiện mật khẩu
                password.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                imgToggle.setImageResource(R.drawable.eye)
            } else {
                // Ẩn mật khẩu
                password.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                imgToggle.setImageResource(R.drawable.eye)
            }
            // Giữ con trỏ ở cuối văn bản
            password.setSelection(password.text.length)
        }

        imgToggle1.setOnClickListener {
            isPasswordVisible1 = !isPasswordVisible1
            if (isPasswordVisible1) {
                // Hiện mật khẩu
                confirmPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                imgToggle.setImageResource(R.drawable.eye)
            } else {
                // Ẩn mật khẩu
                confirmPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                imgToggle.setImageResource(R.drawable.eye)
            }
            // Giữ con trỏ ở cuối văn bản
            confirmPassword.setSelection(confirmPassword.text.length)
        }

        val tvbacktologin = findViewById<ImageView>(R.id.btn_back)
        tvbacktologin.setOnClickListener {
            // Chuyển sang màn hình đăng ký
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun registerUser() {
        val fullName = findViewById<EditText>(R.id.edt_fullname).text.toString().trim()
        val username = findViewById<EditText>(R.id.edt_username).text.toString().trim()
        val phone = findViewById<EditText>(R.id.edt_phone).text.toString().trim()
        val email = findViewById<EditText>(R.id.edt_email).text.toString().trim()
        val password = findViewById<EditText>(R.id.edt_password).text.toString().trim()
        val confirmPassword = findViewById<EditText>(R.id.edt_confirmpassword).text.toString().trim()

        if (fullName.isEmpty() || username.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 6) {
            Toast.makeText(this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: ""
                    val user = User(userId, fullName, username, phone, email)

                    database.child(userId).setValue(user)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Lỗi lưu dữ liệu", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "Đăng ký thất bại: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}