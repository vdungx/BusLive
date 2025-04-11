package com.example.buslive.Activity

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.widget.*
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
    private var isConfirmPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("users")

        val edtFullName = findViewById<EditText>(R.id.edt_fullname)
        val edtUsername = findViewById<EditText>(R.id.edt_username)
        val edtPhone = findViewById<EditText>(R.id.edt_phone)
        val edtEmail = findViewById<EditText>(R.id.edt_email)
        val edtPassword = findViewById<EditText>(R.id.edt_password)
        val edtConfirmPassword = findViewById<EditText>(R.id.edt_confirmpassword)
        val chkAgree = findViewById<CheckBox>(R.id.checksignup)
        val btnSignUp = findViewById<Button>(R.id.btn_signup)
        val imgTogglePassword = findViewById<ImageView>(R.id.img_showpassword)
        val imgToggleConfirmPassword = findViewById<ImageView>(R.id.img_showpassword1)
        val btnBack = findViewById<ImageView>(R.id.btn_back)

        // Hiện/ẩn mật khẩu
        imgTogglePassword.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            togglePasswordVisibility(edtPassword, imgTogglePassword, isPasswordVisible)
        }

        imgToggleConfirmPassword.setOnClickListener {
            isConfirmPasswordVisible = !isConfirmPasswordVisible
            togglePasswordVisibility(edtConfirmPassword, imgToggleConfirmPassword, isConfirmPasswordVisible)
        }

        // Xử lý nút đăng ký
        btnSignUp.setOnClickListener {
            if (!chkAgree.isChecked) {
                Toast.makeText(this, "Bạn phải chấp nhận điều khoản để tiếp tục", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val fullName = edtFullName.text.toString().trim()
            val username = edtUsername.text.toString().trim()
            val phone = edtPhone.text.toString().trim()
            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString().trim()
            val confirmPassword = edtConfirmPassword.text.toString().trim()

            if (fullName.isEmpty() || username.isEmpty() || phone.isEmpty() ||
                email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()
            ) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Đăng ký tài khoản với Firebase
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

        // Quay lại trang đăng nhập
        btnBack.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    // Hàm toggle ẩn/hiện mật khẩu
    private fun togglePasswordVisibility(editText: EditText, icon: ImageView, isVisible: Boolean) {
        if (isVisible) {
            editText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
        icon.setImageResource(R.drawable.eye)
        editText.setSelection(editText.text.length)
    }
}
