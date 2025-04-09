package com.example.buslive.Model

data class ChuyenXe(
    var idChuyen: String? = null,
    var idTuyen: String? = null,
    var ngayKhoiHanh: String? = null,
    var gioDi: String? = null,
    var gioDen: String? = null,
    var tenNhaXe: String? = null,
    var loaiXe: String? = null,
    var giaVe: Int? = null,
    // Thêm 2 thuộc tính để hiển thị
    var diemDi: String? = null,
    var diemDen: String? = null
)