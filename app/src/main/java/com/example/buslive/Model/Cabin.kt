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
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("Cabin").child(maChuyen)

    for (tang in 1..2) {
        for (i in 1..8) {
            val viTri = if (tang == 1) "A0$i" else "B0$i"

            val cabin = Cabin(
                maCabin = (tang - 1) * 8 + i,
                maChuyen = maChuyen,
                loai = if (i % 2 == 0) "Cabin Đôi" else "Cabin Đơn",
                tang = tang,
                viTri = viTri,
                gia = if (tang == 1) 200000.0 else 250000.0,
                trangThai = "Trong"
            )

            val cabinKey = "Cabin${cabin.maCabin}"
            myRef.child(cabinKey).setValue(cabin)
        }
    }

    Log.d("Firebase", "Đã thêm cabin cho chuyến $maChuyen")
}