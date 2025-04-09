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
        ChuyenXe("CX1", "Tuyen1", "2025-04-10", "06:00", "08:00", "Xe 1", "Giường nằm", 150000, "Hà Nội", "Hải Phòng", "Hà Nội", "Hải Phòng"),
        ChuyenXe("CX2", "Tuyen2", "2025-04-12", "07:00", "09:30", "Xe 2", "Ghế ngồi", 100000, "Sài Gòn", "Vũng Tàu", "Sài Gòn", "Vũng Tàu"),
        ChuyenXe("CX3", "Tuyen3", "2025-04-15", "08:00", "10:00", "Xe 3", "Giường nằm", 200000, "Đà Nẵng", "Huế", "Đà Nẵng", "Huế"),
        ChuyenXe("CX4", "Tuyen4", "2025-04-20", "09:00", "11:00", "Xe 4", "Ghế ngồi", 120000, "Hà Nội", "Quảng Ninh", "Hà Nội", "Quảng Ninh"),
        ChuyenXe("CX5", "Tuyen5", "2025-04-22", "10:00", "12:00", "Xe 5", "Giường nằm", 180000, "TPHCM", "Đà Lạt", "TPHCM", "Đà Lạt"),
        ChuyenXe("CX6", "Tuyen6", "2025-04-25", "11:00", "13:00", "Xe 6", "Ghế ngồi", 130000, "Vinh", "Hà Tĩnh", "Vinh", "Hà Tĩnh"),
        ChuyenXe("CX7", "Tuyen7", "2025-04-30", "12:00", "14:00", "Xe 7", "Giường nằm", 160000, "Hà Nội", "Ninh Bình", "Hà Nội", "Ninh Bình"),
        ChuyenXe("CX8", "Tuyen8", "2025-05-05", "13:00", "15:00", "Xe 8", "Ghế ngồi", 140000, "Huế", "Quảng Trị", "Huế", "Quảng Trị"),
        ChuyenXe("CX9", "Tuyen9", "2025-05-10", "14:00", "16:00", "Xe 9", "Giường nằm", 190000, "Đà Nẵng", "Quảng Nam", "Đà Nẵng", "Quảng Nam"),
        ChuyenXe("CX10", "Tuyen10", "2025-05-15", "15:00", "17:00", "Xe 10", "Ghế ngồi", 110000, "Sài Gòn", "Bình Dương", "Sài Gòn", "Bình Dương")
    )

    // Lặp qua danh sách và thêm từng chuyến lên Firebase
    for (chuyen in chuyenList) {
        val chuyenKey = "Chuyen${chuyen.idChuyen}" // Tạo key cho mỗi chuyến, ví dụ: ChuyenCX1, ChuyenCX2...
        myRef.child(chuyenKey).setValue(chuyen)
    }

    // Log thông báo thành công (tuỳ chọn)
    Log.d("Firebase", "Dữ liệu ChuyenXe đã được đẩy lên thành công!")
}
