package com.example.buslive.Model

data class User (
    val userId: String = "",
    val fullName: String = "",
    val username: String = "",
    val phone: String = "",
    val email: String = "",
    val role: String = "customer",
    val isApproved: Boolean = true
)