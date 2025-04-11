package com.example.buslive.Respository

import com.example.buslive.Model.TicketModal
import com.google.firebase.database.*

object TicketRepository {

    fun loadUserTickets(userId: String, onDone: (List<TicketModal>) -> Unit) {
        val db = FirebaseDatabase.getInstance().reference

        db.child("VeXe").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val veList = snapshot.children.filter {
                    it.child("maKH").value?.toString() == userId
                }

                if (veList.isEmpty()) {
                    onDone(emptyList())
                    return
                }

                db.child("Cabin").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(cabinSnapshot: DataSnapshot) {
                        db.child("ChuyenXe").addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(chuyenSnapshot: DataSnapshot) {
                                val result = mutableListOf<TicketModal>()

                                for (ve in veList) {
                                    val maCabin = ve.child("maCabin").value?.toString() ?: continue
                                    val maVe = ve.key ?: continue
                                    val maChuyen = ve.child("maChuyen").value?.toString() ?: continue
                                    val thoiGianDatVe = ve.child("thoiGianDatVe").value?.toString() ?: ""

                                    // Cabin/maChuyen/maCabin
                                    val cabinNode = cabinSnapshot.child(maChuyen).child(maCabin)
                                    val gia = cabinNode.child("gia").value?.toString()?.toIntOrNull() ?: continue

                                    // ChuyenXe/Chuyen{maChuyen}
                                    val chuyen = chuyenSnapshot.child("Chuyen$maChuyen")
                                    if (!chuyen.exists()) continue

                                    val diemDi = chuyen.child("diemDi").value?.toString() ?: continue
                                    val diemDen = chuyen.child("diemDen").value?.toString() ?: continue
                                    val tenNhaXe = chuyen.child("tenNhaXe").value?.toString() ?: continue
                                    val loaiXe = chuyen.child("loaiXe").value?.toString() ?: continue
                                    val gioDi = chuyen.child("gioDi").value?.toString() ?: continue
                                    val trangThai = ve.child("trangThai").value?.toString() ?: continue
                                    val ngay = chuyen.child("ngayKhoiHanh").value?.toString() ?: continue

                                    result.add(
                                        TicketModal(
                                            diemDi = diemDi,
                                            diemDen = diemDen,
                                            tenNhaXe = tenNhaXe,
                                            loaiXe = loaiXe,
                                            gioKhoiHanh = gioDi,
                                            ngayKhoiHanh = ngay,
                                            thoiGianDatVe = thoiGianDatVe,
                                            gia = gia.toString(),
                                            trangThai = trangThai,
                                            maVe = maVe
                                        )
                                    )
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
