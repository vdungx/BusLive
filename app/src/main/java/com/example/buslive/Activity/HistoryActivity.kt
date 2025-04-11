package com.example.buslive.Activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.buslive.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HistoryActivity : AppCompatActivity() {
    private lateinit var textViewDiemDiDen: TextView
    private lateinit var textViewNhaXe: TextView
    private lateinit var textViewThoiGianDatVe: TextView
    private lateinit var textViewDiemDi: TextView
    private lateinit var textViewDiemDen: TextView
    private lateinit var textViewThoiGianKhoiHanh: TextView
    private lateinit var textViewThoiGianDuKien: TextView
    private lateinit var textViewGiaVe: TextView
    private lateinit var textViewTrangThai: TextView
    private lateinit var textViewTenNhaXeLienHe: TextView
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        // Khai báo các TextView
        textViewDiemDiDen = findViewById(R.id.textViewDiemDiDen)
        textViewNhaXe = findViewById(R.id.textViewNhaXe)
        textViewThoiGianDatVe = findViewById(R.id.textViewThoiGianDatVe)
        textViewDiemDi = findViewById(R.id.textViewDiemDi)
        textViewDiemDen = findViewById(R.id.textViewDiemDen)
        textViewThoiGianKhoiHanh = findViewById(R.id.textViewThoiGianKhoiHanh)
        textViewThoiGianDuKien = findViewById(R.id.textViewThoiGianDuKien)
        textViewGiaVe = findViewById(R.id.textViewGiaVe)
        textViewTrangThai = findViewById(R.id.textViewTrangThai)
        textViewTenNhaXeLienHe = findViewById(R.id.textViewTenNhaXeLienHe)

        // Nhận dữ liệu từ Intent
        val maVe = intent.getStringExtra("MA_VE")

        // Khởi tạo Firebase Database
        databaseReference = FirebaseDatabase.getInstance().reference

        // Lấy thông tin vé xe từ Firebase
        maVe?.let {
            databaseReference.child("VeXe").child(it).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val maCabin = dataSnapshot.child("maCabin").getValue(String::class.java)
                        val thoiGianDatVe = dataSnapshot.child("thoiGianDatVe").getValue(String::class.java)
                        val trangThai = dataSnapshot.child("trangThai").getValue(String::class.java)

                        // Lấy thông tin từ Cabin
                        maCabin?.let { cabinId ->
                            databaseReference.child("Cabin").child(cabinId).addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(cabinSnapshot: DataSnapshot) {
                                    if (cabinSnapshot.exists()) {
                                        val maChuyen = cabinSnapshot.child("maChuyen").getValue(String::class.java)

                                        // Lấy thông tin chuyến xe
                                        maChuyen?.let { tripId ->
                                            databaseReference.child("ChuyenXe").child("Chuyen$tripId").addListenerForSingleValueEvent(object : ValueEventListener {
                                                override fun onDataChange(chuyenSnapshot: DataSnapshot) {
                                                    if (chuyenSnapshot.exists()) {
                                                        textViewDiemDiDen.text = "${chuyenSnapshot.child("diemDi").getValue(String::class.java)} - ${chuyenSnapshot.child("diemDen").getValue(String::class.java)}"
                                                        textViewNhaXe.text = chuyenSnapshot.child("tenNhaXe").getValue(String::class.java) ?: "Không có thông tin"
                                                        textViewThoiGianDatVe.text = "Thời gian đặt vé: $thoiGianDatVe" ?: "Không có thông tin"
                                                        textViewDiemDi.text = "Điểm đi: ${chuyenSnapshot.child("diemDi").getValue(String::class.java) ?: "Không có thông tin"}"
                                                        textViewDiemDen.text = "Điểm đến: ${chuyenSnapshot.child("diemDen").getValue(String::class.java) ?: "Không có thông tin"}"
                                                        textViewThoiGianKhoiHanh.text = "Thời gian khởi hành: ${chuyenSnapshot.child("gioDi").getValue(String::class.java)} - ${chuyenSnapshot.child("ngayKhoiHanh").getValue(String::class.java) ?: "Không có thông tin"}"
                                                        textViewThoiGianDuKien.text = "Thời gian dự kiến: ${chuyenSnapshot.child("gioDen").getValue(String::class.java) ?: "Không có thông tin"}"
                                                        textViewGiaVe.text = "Giá vé: ${cabinSnapshot.child("gia").getValue(Long::class.java)?.toString() ?: "Không có thông tin"}"
                                                        textViewTrangThai.text = "Trạng thái: $trangThai"
                                                        textViewTenNhaXeLienHe.text = "Nhà xe: ${chuyenSnapshot.child("tenNhaXe").getValue(String::class.java) ?: "Không có thông tin"}"
                                                    }
                                                }

                                                override fun onCancelled(databaseError: DatabaseError) {
                                                    // Xử lý lỗi nếu cần
                                                }
                                            })
                                        }
                                    }
                                }

                                override fun onCancelled(databaseError: DatabaseError) {
                                    // Xử lý lỗi nếu cần
                                }
                            })
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Xử lý lỗi nếu cần
                }
            })
        }

        val btnBack = findViewById<ImageView>(R.id.btn_backve)
        btnBack.setOnClickListener {
            finish() // Quay lại
        }

    }
}