package com.example.buslive.Model
import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.example.buslive.Model.ChuyenXe

data class ChuyenXe(
    var idChuyen: String? = null,
    var idTuyen: String? = null,
    var ngayKhoiHanh: String? = null,
    var gioDi: String? = null,
    var gioDen: String? = null,
    var tenNhaXe: String? = null,
    var loaiXe: String? = null,
    var giaVe: Int? = null,
    val diemDon: String = "",
    val diemTra: String = "",
    var diemDi: String? = null,
    var diemDen: String? = null
)


fun pushMultipleChuyenXeData() {
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val myRef: DatabaseReference = database.getReference("ChuyenXe")

    // Tạo danh sách 10 chuyến xe
    val chuyenList = listOf(
        ChuyenXe(
            "CX11", "Tuyen1", "2025-04-12", "09:00", "11:00", "Xe 3", "Giường nằm", 150000,
            "Hà Nội", "Hải Phòng", "Hà Nội", "Hải Phòng"
        ),
                ChuyenXe(
                "CX12", "Tuyen1", "2025-04-12", "13:00", "15:00", "Xe 4", "Limousine", 180000,
        "Hà Nội", "Hải Phòng", "Hà Nội", "Hải Phòng"
    )
    )

    // Lặp qua danh sách và thêm từng chuyến lên Firebase
    for (chuyen in chuyenList) {
        val chuyenKey = "Chuyen${chuyen.idChuyen}" // Tạo key cho mỗi chuyến, ví dụ: ChuyenCX1, ChuyenCX2...
        myRef.child(chuyenKey).setValue(chuyen)
    }

    // Log thông báo thành công (tuỳ chọn)
    Log.d("Firebase", "Dữ liệu ChuyenXe đã được đẩy lên thành công!")
}
