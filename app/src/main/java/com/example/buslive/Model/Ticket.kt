package com.example.buslive.Model

data class Ticket(
    val route: String,
    val company: String,
    val type: String,
    val time: String,
    val bookingTime: String
)
