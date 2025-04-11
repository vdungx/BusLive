package com.example.buslive.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.buslive.Fragment.FragmentAccount
import com.example.buslive.Fragment.FragmentHistory
import com.example.buslive.Fragment.FragmentHome
import com.example.buslive.Model.ChuyenXe
import com.example.buslive.R
import com.example.buslive.adapter.ChuyenXeAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*

class ResultActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var chuyenXeAdapter: ChuyenXeAdapter
    private val chuyenXeList = ArrayList<ChuyenXe>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    loadFragment(FragmentHome())
                    true
                }
                R.id.navigation_filter -> {
                    val intent = Intent(this, ResultActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_history -> {
                    loadFragment(FragmentHistory())
                    true
                }
                R.id.navigation_account -> {
                    loadFragment(FragmentAccount())
                    true
                }
                else -> false
            }
        }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        chuyenXeAdapter = ChuyenXeAdapter { chuyenXe ->
            val intent = Intent(this, ChonChoActivity::class.java)
            intent.putExtra("maChuyen", chuyenXe.idChuyen)
            startActivity(intent)
        }
        recyclerView.adapter = chuyenXeAdapter

        // Đọc dữ liệu từ Firebase
        val dbRef = FirebaseDatabase.getInstance().getReference("ChuyenXe")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val currentList = ArrayList<ChuyenXe>()
                val currentDate = getCurrentDate() // ví dụ: "2025-04-11"

                for (data in snapshot.children) {
                    val chuyen = data.getValue(ChuyenXe::class.java)
                    val ngayKhoiHanh = chuyen?.ngayKhoiHanh
                    if (chuyen != null && ngayKhoiHanh != null && ngayKhoiHanh >= currentDate) {
                        currentList.add(chuyen)
                    }
                }

                if (currentList.isEmpty()) {
                    Toast.makeText(
                        this@ResultActivity,
                        "Không có chuyến xe nào sắp tới",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                chuyenXeAdapter.submitList(currentList)
            }


            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ResultActivity, "Lỗi: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun getCurrentDate(): String {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        return sdf.format(java.util.Date())
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
