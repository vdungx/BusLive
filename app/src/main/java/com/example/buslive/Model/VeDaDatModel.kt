package com.example.buslive.Model

data class VeDaDatModel(
    val route: String,           // Hà Nội - Hải Phòng
    val company: String,         // Tên nhà xe
    val type: String,            // Giường nằm, Ghế ngồi
    val time: String,            // 10:00 - 2025-04-10
    val bookingTime: String,     // Thời gian đặt vé (nếu có)
    val maVe: String             // Để xử lý chi tiết
)
