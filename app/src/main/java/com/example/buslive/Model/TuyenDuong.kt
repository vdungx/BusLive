package com.example.buslive.Model

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

data class TuyenDuong(
    val idTuyen: String,
    val diemDi: String,
    val diemDen: String
)

fun pushMultipleTuyenDuongData() {
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val myRef: DatabaseReference = database.getReference("TuyenDuong")

    // Tạo danh sách các tuyến đường
    val tuyenList = listOf(
        TuyenDuong("1", "Hà Nội", "Hải Phòng"),
        TuyenDuong("2", "Sài Gòn", "Vũng Tàu"),
        TuyenDuong("3", "Đà Nẵng", "Huế"),
        TuyenDuong("4", "Hà Nội", "Sapa"),
        TuyenDuong("5", "TP.HCM", "Long An"),
        TuyenDuong("6", "Cần Thơ", "Rạch Giá"),
        TuyenDuong("7", "Nha Trang", "Phan Thiết")
    )

    // Lặp qua danh sách và thêm từng tuyến lên Firebase
    for (tuyen in tuyenList) {
        val tuyenKey = "Tuyen${tuyen.idTuyen}" // Tạo key cho mỗi tuyến, ví dụ: Tuyen1, Tuyen2...
        myRef.child(tuyenKey).setValue(tuyen)
    }

    // Log thông báo thành công (tuỳ chọn)
    Log.d("Firebase", "Dữ liệu TuyenDuong đã được đẩy lên thành công!")
}