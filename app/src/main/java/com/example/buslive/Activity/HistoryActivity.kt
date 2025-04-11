package com.example.buslive.Activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.buslive.R
import com.google.firebase.database.*

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
    private lateinit var textViewChoNgoi: TextView
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        // Ánh xạ view
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
        textViewChoNgoi = findViewById(R.id.textViewChoNgoi)

        val maVe = intent.getStringExtra("MA_VE")
        databaseReference = FirebaseDatabase.getInstance().reference

        maVe?.let {
            databaseReference.child("VeXe").child(it).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val maCabin = dataSnapshot.child("maCabin").getValue(String::class.java)
                        val maChuyen = dataSnapshot.child("maChuyen").getValue(String::class.java)
                        val thoiGianDatVe = dataSnapshot.child("thoiGianDatVe").getValue(String::class.java)
                        val trangThai = dataSnapshot.child("trangThai").getValue(String::class.java)

                        if (!maCabin.isNullOrEmpty() && !maChuyen.isNullOrEmpty()) {
                            val cabinPath = "Cabin/$maChuyen/$maCabin"
                            databaseReference.child(cabinPath).addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(cabinSnapshot: DataSnapshot) {
                                    if (cabinSnapshot.exists()) {
                                        val gia = cabinSnapshot.child("gia").getValue(Long::class.java)
                                        val maChuyenXe = cabinSnapshot.child("maChuyen").getValue(String::class.java)
                                        val viTri = cabinSnapshot.child("viTri").value?.toString() ?: "Không rõ"
                                        val tang = cabinSnapshot.child("tang").value?.toString() ?: "?"


                                        // Hiển thị chỗ ngồi
                                        textViewChoNgoi.text = "Chỗ ngồi: Tầng $tang - $viTri"

                                        if (!maChuyenXe.isNullOrEmpty()) {
                                            val chuyenXePath = "ChuyenXe/Chuyen$maChuyenXe"
                                            databaseReference.child(chuyenXePath).addListenerForSingleValueEvent(object : ValueEventListener {
                                                override fun onDataChange(chuyenSnapshot: DataSnapshot) {
                                                    if (chuyenSnapshot.exists()) {
                                                        val diemDi = chuyenSnapshot.child("diemDi").getValue(String::class.java) ?: "Không rõ"
                                                        val diemDen = chuyenSnapshot.child("diemDen").getValue(String::class.java) ?: "Không rõ"
                                                        val tenNhaXe = chuyenSnapshot.child("tenNhaXe").getValue(String::class.java) ?: "Không rõ"
                                                        val gioDi = chuyenSnapshot.child("gioDi").getValue(String::class.java) ?: ""
                                                        val ngayKhoiHanh = chuyenSnapshot.child("ngayKhoiHanh").getValue(String::class.java) ?: ""
                                                        val gioDen = chuyenSnapshot.child("gioDen").getValue(String::class.java) ?: ""

                                                        textViewDiemDiDen.text = "$diemDi - $diemDen"
                                                        textViewNhaXe.text = tenNhaXe
                                                        textViewThoiGianDatVe.text = "Thời gian đặt vé: $thoiGianDatVe"
                                                        textViewDiemDi.text = "Điểm đi: $diemDi"
                                                        textViewDiemDen.text = "Điểm đến: $diemDen"
                                                        textViewThoiGianKhoiHanh.text = "Thời gian khởi hành: $gioDi - $ngayKhoiHanh"
                                                        textViewThoiGianDuKien.text = "Thời gian dự kiến: $gioDen"
                                                        textViewGiaVe.text = "Giá vé: ${gia ?: 0}"
                                                        textViewTrangThai.text = "Trạng thái: $trangThai"
                                                        textViewTenNhaXeLienHe.text = "Nhà xe: $tenNhaXe"
                                                    }
                                                }

                                                override fun onCancelled(error: DatabaseError) {}
                                            })
                                        }
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {}
                            })
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        }

        findViewById<ImageView>(R.id.btn_backve).setOnClickListener {
            finish()
        }
    }
}
