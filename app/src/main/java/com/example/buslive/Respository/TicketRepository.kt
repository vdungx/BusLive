package com.example.buslive.Respository

import com.example.buslive.Model.VeDaDatModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

object TicketRepository {

    fun loadUserTickets(userId: String, onDone: (List<VeDaDatModel>) -> Unit) {
        val db = FirebaseDatabase.getInstance().reference

        db.child("VeXe").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val veList = mutableListOf<DataSnapshot>()

                // 1. Lọc vé theo user
                for (ve in snapshot.children) {
                    val maKH = ve.child("maKH").value?.toString()
                    if (maKH == userId) veList.add(ve)
                }

                if (veList.isEmpty()) {
                    onDone(emptyList())
                    return
                }

                // 2. Lấy Cabin và ChuyenXe
                db.child("Cabin").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(cabinSnapshot: DataSnapshot) {
                        db.child("ChuyenXe").addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(chuyenSnapshot: DataSnapshot) {
                                val result = mutableListOf<VeDaDatModel>()

                                for (ve in veList) {
                                    val maCabin = ve.child("maCabin").value?.toString() ?: continue
                                    val maVe = ve.child("maVe").value?.toString() ?: continue

                                    val cabin = cabinSnapshot.child(maCabin)
                                    val maChuyen = cabin.child("maChuyen").value?.toString() ?: continue

                                    val chuyen = chuyenSnapshot.child("Chuyen$maChuyen")
                                    if (chuyen.exists()) {
                                        val diemDi = chuyen.child("diemDi").value?.toString() ?: continue
                                        val diemDen = chuyen.child("diemDen").value?.toString() ?: continue
                                        val tenNhaXe = chuyen.child("tenNhaXe").value?.toString() ?: continue
                                        val loaiXe = chuyen.child("loaiXe").value?.toString() ?: continue
                                        val gioDi = chuyen.child("gioDi").value?.toString() ?: continue
                                        val ngay = chuyen.child("ngayKhoiHanh").value?.toString() ?: continue
                                        val thoiGianDatVe = ve.child("thoiGianDatVe").value?.toString()

                                        result.add(
                                            VeDaDatModel(
                                                route = "$diemDi - $diemDen",
                                                company = tenNhaXe,
                                                type = loaiXe,
                                                time = "$gioDi - $ngay",
                                                bookingTime = thoiGianDatVe ?: "",
                                                maVe = maVe
                                            )
                                        )
                                    }
                                }

                                onDone(result)
                            }

                            override fun onCancelled(error: DatabaseError) {
                                onDone(emptyList())
                            }
                        })
                    }

                    override fun onCancelled(error: DatabaseError) {
                        onDone(emptyList())
                    }
                })
            }

            override fun onCancelled(error: DatabaseError) {
                onDone(emptyList())
            }
        })
    }
}