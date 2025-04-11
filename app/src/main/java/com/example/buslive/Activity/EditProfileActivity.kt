package com.example.buslive.Activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.buslive.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class EditProfileActivity : AppCompatActivity() {

    private lateinit var edtFullName: EditText
    private lateinit var edtUsername: EditText
    private lateinit var edtPhone: EditText
    private lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        edtFullName = findViewById(R.id.edtFullName)
        edtUsername = findViewById(R.id.edtUsername)
        edtPhone = findViewById(R.id.edtPhone)
        btnSave = findViewById(R.id.btnSave)

        val uid = FirebaseAuth.getInstance().currentUser?.uid

        if (uid != null) {
            val userRef = FirebaseDatabase.getInstance().getReference("users").child(uid)
            userRef.get().addOnSuccessListener {
                edtFullName.setText(it.child("fullName").value?.toString() ?: "")
                edtUsername.setText(it.child("username").value?.toString() ?: "")
                edtPhone.setText(it.child("phone").value?.toString() ?: "")
            }
        }

        btnSave.setOnClickListener {
            val fullName = edtFullName.text.toString()
            val username = edtUsername.text.toString()
            val sdt = edtPhone.text.toString()

            if (uid != null) {
                val updates = mapOf(
                    "fullName" to fullName,
                    "username" to username,
                    "phone" to sdt
                )

                FirebaseDatabase.getInstance().getReference("users").child(uid)
                    .updateChildren(updates)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Lỗi: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}
