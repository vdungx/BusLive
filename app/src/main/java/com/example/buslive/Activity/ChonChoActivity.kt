package com.example.buslive.Activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.buslive.Model.Cabin
import com.example.buslive.R
import com.example.buslive.adapter.CabinAdapter
import com.example.buslive.adapter.SeatAdapter
import com.google.firebase.database.*

class ChonChoActivity : AppCompatActivity() {

    private lateinit var lowerFloorRecyclerView: RecyclerView
    private lateinit var upperFloorRecyclerView: RecyclerView
    private lateinit var btnContinue: android.widget.Button

    private var selectedCabin: Cabin? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chon_cho)

        // Nhận dữ liệu từ intent
        val tenNhaXe = intent.getStringExtra("tenNhaXe")
        val gioDi = intent.getStringExtra("gioDi")
        val ngayKhoiHanh = intent.getStringExtra("ngayKhoiHanh")
        val maChuyen = intent.getStringExtra("maChuyen") ?: return

        // Gán dữ liệu lên giao diện
        val titleTextView = findViewById<TextView>(R.id.titleTextView)
        val dateTimeTextView = findViewById<TextView>(R.id.dateTimeTextView)
        val backButton = findViewById<ImageView>(R.id.backButton)

        titleTextView.text = tenNhaXe ?: "Chuyến xe"
        dateTimeTextView.text = "${gioDi ?: "--:--"} , ${ngayKhoiHanh ?: "Ngày chưa rõ"}"

        backButton.setOnClickListener { onBackPressed() }

        // RecyclerView chọn loại cabin (đơn/đôi)
        val cabinOptionsRecyclerView = findViewById<RecyclerView>(R.id.cabinOptionsRecyclerView)
        val cabinList = mutableListOf<Cabin>()
        val cabinAdapter = CabinAdapter(cabinList)
        cabinOptionsRecyclerView.layoutManager = LinearLayoutManager(this)
        cabinOptionsRecyclerView.adapter = cabinAdapter

        // RecyclerView cho ghế tầng dưới và tầng trên
        lowerFloorRecyclerView = findViewById(R.id.lowerFloorSeatsRecyclerView)
        upperFloorRecyclerView = findViewById(R.id.upperFloorSeatsRecyclerView)
        lowerFloorRecyclerView.layoutManager = GridLayoutManager(this, 2)
        upperFloorRecyclerView.layoutManager = GridLayoutManager(this, 2)

        // Lấy dữ liệu cabin từ Firebase
        val database = FirebaseDatabase.getInstance().getReference("Cabin").child(maChuyen)
        database.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val lowerCabins = mutableListOf<Cabin>()
                    val upperCabins = mutableListOf<Cabin>()
                    cabinList.clear()

                    for (data in snapshot.children) {
                        val cabin = data.getValue(Cabin::class.java)
                        if (cabin != null) {
                            cabinList.add(cabin)
                            if (cabin.tang == 1) lowerCabins.add(cabin)
                            else if (cabin.tang == 2) upperCabins.add(cabin)
                        }
                    }

                    cabinAdapter.notifyDataSetChanged()

                    val lowerSeatAdapter = SeatAdapter(this@ChonChoActivity, lowerCabins)
                    val upperSeatAdapter = SeatAdapter(this@ChonChoActivity, upperCabins)

                    // Callback khi chọn ghế tầng dưới
                    lowerSeatAdapter.onSeatClick = { cabin ->
                        selectedCabin = cabin
                        upperSeatAdapter.clearSelection()
                        btnContinue.visibility = View.VISIBLE
                        Toast.makeText(this@ChonChoActivity, "Chọn ghế: ${cabin.viTri}", Toast.LENGTH_SHORT).show()
                    }

                    // Callback khi chọn ghế tầng trên
                    upperSeatAdapter.onSeatClick = { cabin ->
                        selectedCabin = cabin
                        lowerSeatAdapter.clearSelection()
                        btnContinue.visibility = View.VISIBLE
                        Toast.makeText(this@ChonChoActivity, "Chọn ghế: ${cabin.viTri}", Toast.LENGTH_SHORT).show()
                    }

                    lowerFloorRecyclerView.adapter = lowerSeatAdapter
                    upperFloorRecyclerView.adapter = upperSeatAdapter
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ChonChoActivity, "Lỗi tải dữ liệu: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })

        // Xử lý nút Tiếp tục
        btnContinue = findViewById(R.id.btnContinue)
        btnContinue.visibility = View.GONE

        btnContinue.setOnClickListener {
            selectedCabin?.let {
                // Tạo Intent để chuyển sang màn hình thanh toán
                val thanhToanIntent = Intent(this, ThanhToanActivity::class.java)

                // Gửi thông tin chuyến đi
                thanhToanIntent.putExtra("tenNhaXe", tenNhaXe)
                thanhToanIntent.putExtra("gioDi", gioDi)
                thanhToanIntent.putExtra("ngayKhoiHanh", ngayKhoiHanh)
                thanhToanIntent.putExtra("maChuyen", maChuyen)

                // Gửi thông tin cabin đã chọn
                thanhToanIntent.putExtra("viTri", it.viTri)
                thanhToanIntent.putExtra("gia", it.gia)
                thanhToanIntent.putExtra("tang", it.tang)

                startActivity(thanhToanIntent)
            } ?: Toast.makeText(this, "Vui lòng chọn một ghế", Toast.LENGTH_SHORT).show()
        }

    }
}
