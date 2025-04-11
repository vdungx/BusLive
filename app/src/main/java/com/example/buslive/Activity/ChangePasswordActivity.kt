package com.example.buslive.Activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.buslive.R
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var edtOldPassword: EditText
    private lateinit var edtNewPassword: EditText
    private lateinit var edtConfirmPassword: EditText
    private lateinit var btnChangePassword: Button

    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        edtOldPassword = findViewById(R.id.edtOldPassword)
        edtNewPassword = findViewById(R.id.edtNewPassword)
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword)
        btnChangePassword = findViewById(R.id.btnChangePassword)

        btnChangePassword.setOnClickListener {
            changePassword()
        }
    }

    private fun changePassword() {
        val user = auth.currentUser
        val oldPass = edtOldPassword.text.toString()
        val newPass = edtNewPassword.text.toString()
        val confirmPass = edtConfirmPassword.text.toString()

        if (oldPass.isBlank() || newPass.isBlank() || confirmPass.isBlank()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            return
        }

        if (newPass != confirmPass) {
            Toast.makeText(this, "Mật khẩu mới không khớp", Toast.LENGTH_SHORT).show()
            return
        }

        user?.email?.let { email ->
            val credential = EmailAuthProvider.getCredential(email, oldPass)
            user.reauthenticate(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        user.updatePassword(newPass)
                            .addOnCompleteListener { updateTask ->
                                if (updateTask.isSuccessful) {
                                    Toast.makeText(this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show()
                                    finish()
                                } else {
                                    Toast.makeText(this, "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        Toast.makeText(this, "Mật khẩu cũ không đúng", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
