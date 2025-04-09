package com.example.buslive.Model
import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.example.buslive.Model.Cabin

data class Cabin(
    val maCabin: Int? = null,
    val maChuyen: String? = null,
    val loai: String? = null,
    val tang: Int? = null,
    val viTri: String? = null,
    val gia: Double? = null,
    val trangThai: String? = null
)


fun addCabinsForTrip(maChuyen: String) {
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val myRef: DatabaseReference = database.getReference("Cabin")

    // Tạo danh sách cabin cho chuyến xe
    val cabinList = mutableListOf<Cabin>()

    // Chia ra 2 tầng, mỗi tầng 8 cabin
    for (tang in 1..2) {
        for (i in 1..8) {
            val viTri = if (tang == 1) {
                "A0$i" // Ví dụ: A01, A02, A03, ..., A08 cho tầng 1
            } else {
                "B0$i" // Ví dụ: B01, B02, B03, ..., B08 cho tầng 2
            }

            // Tạo cabin cho chuyến xe
            val cabin = Cabin(
                maCabin = (tang - 1) * 8 + i, // Mã cabin từ 1 đến 16
                maChuyen = maChuyen, // Mã chuyến
                loai = if (i % 2 == 0) "Cabin Đôi" else "Cabin Đơn", // Ví dụ: Đơn cho các số lẻ, Đôi cho các số chẵn
                tang = tang,
                viTri = viTri,
                gia = if (tang == 1) 200000.0 else 250000.0, // Giá cho tầng 1 và tầng 2
                trangThai = "Trong" // Trạng thái ghế còn trống
            )

            // Thêm vào danh sách cabin
            cabinList.add(cabin)
        }
    }

    // Thêm các cabin vào Firebase
    for (cabin in cabinList) {
        val cabinKey = "Cabin${cabin.maCabin}" // Tạo key cho mỗi cabin
        myRef.child(cabinKey).setValue(cabin)
    }

    // Log thông báo thành công (tuỳ chọn)
    Log.d("Firebase", "Dữ liệu Cabin cho chuyến $maChuyen đã được đẩy lên thành công!")
}
