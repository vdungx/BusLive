package com.example.buslive.Activity

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.buslive.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.Locale

class ResultActivity : AppCompatActivity() {

    private lateinit var tvDiemDi: TextView
    private lateinit var tvDiemDen: TextView
    private lateinit var tvDate: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        // Ánh xạ view
        tvDiemDi = findViewById(R.id.tvdiemdi)
        tvDiemDen = findViewById(R.id.tvdiemden)
        tvDate = findViewById(R.id.tvDate)
        recyclerView = findViewById(R.id.recyclerView)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        // Nhận dữ liệu từ HomeActivity
        val departure = intent.getStringExtra("departure") ?: ""
        val destination = intent.getStringExtra("destination") ?: ""
        val date = intent.getStringExtra("date") ?: ""

        // Gán dữ liệu lên giao diện
        tvDiemDi.text = departure
        tvDiemDen.text = destination
        tvDate.text = formatDate(date)

        // Hiển thị mục đang chọn là "Lọc"
        bottomNavigationView.selectedItemId = R.id.navigation_filter

        //        bottomNavigationView.setOnItemSelectedListener { item: MenuItem ->
        //            when (item.itemId) {
        //                R.id.navigation_home -> {
        //                    // TODO: Chuyển về HomeActivity nếu muốn
        //                    true
        //                }
        //                R.id.navigation_location -> {
        //                    // Đang ở ResultActivity
        //                    true
        //                }
        //                R.id.navigation_history -> {
        //                    // TODO: Mở lịch sử
        //                    true
        //                }
        //                R.id.navigation_account -> {
        //                    // TODO: Mở trang tài khoản
        //                    true
        //                }
        //                else -> false
        //            }
        //        }

        // TODO: Thiết lập RecyclerView hiển thị chuyến xe tại đây nếu có dữ liệu thật
        // loadTripResults(departure, destination, date)
    }

    private fun formatDate(dateStr: String?): String {
        if (dateStr.isNullOrEmpty()) return ""
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("EEE, dd/MM/yyyy", Locale("vi"))
            val date = inputFormat.parse(dateStr)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            dateStr
        }
    }
}