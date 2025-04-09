package com.example.buslive.Activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.buslive.Adapter.CabinAdapter
import com.example.buslive.Model.Cabin
import com.example.buslive.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChonChoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chon_cho)

        // Lấy dữ liệu chuyến xe được truyền qua Intent
        val tenNhaXe = intent.getStringExtra("tenNhaXe")
        val gioDi = intent.getStringExtra("gioDi")
        val ngayKhoiHanh = intent.getStringExtra("ngayKhoiHanh")

        // Ánh xạ View từ XML
        val titleTextView = findViewById<TextView>(R.id.titleTextView)
        val dateTimeTextView = findViewById<TextView>(R.id.dateTimeTextView)
        val backButton = findViewById<ImageView>(R.id.backButton)

        // Gán dữ liệu lên view
        titleTextView.text = tenNhaXe ?: "Chuyến xe"
        dateTimeTextView.text = "${gioDi ?: "--:--"} , ${ngayKhoiHanh ?: "Ngày chưa rõ"}"

        // Xử lý nút quay lại
        backButton.setOnClickListener {
            onBackPressed()
        }

        val recyclerView = findViewById<RecyclerView>(R.id.cabinOptionsRecyclerView)
        val cabinList = mutableListOf<Cabin>()
        val adapter = CabinAdapter(cabinList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val database = FirebaseDatabase.getInstance().getReference("Cabin")

        val maChuyen = intent.getStringExtra("maChuyen") ?: return

        database.orderByChild("maChuyen").equalTo(maChuyen.toDouble()) // Firebase lưu số là Double
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    cabinList.clear()
                    for (data in snapshot.children) {
                        val cabin = data.getValue(Cabin::class.java)
                        if (cabin != null) {
                            cabinList.add(cabin)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ChonChoActivity, "Lỗi tải cabin: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
