package com.example.buslive.Fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.buslive.R
import com.example.buslive.adapter.ChuyenXeAdapter
import com.example.buslive.Model.ChuyenXe
import com.example.buslive.Model.TuyenDuong
import com.example.buslive.viewmodel.SearchViewModel
import com.google.firebase.database.*
import java.util.Calendar

class FragmentFilter : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChuyenXeAdapter
    private val searchViewModel: SearchViewModel by activityViewModels()

    private lateinit var database: FirebaseDatabase
    private lateinit var tuyenRef: DatabaseReference
    private lateinit var chuyenRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_filter, container, false)

        // Setup RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ChuyenXeAdapter()
        recyclerView.adapter = adapter

        // Firebase reference
        database = FirebaseDatabase.getInstance()
        tuyenRef = database.getReference("TuyenDuong")
        chuyenRef = database.getReference("ChuyenXe")

        val edtFrom = view.findViewById<EditText>(R.id.edtFrom)
        val edtTo = view.findViewById<EditText>(R.id.edtTo)
        val edtDate = view.findViewById<EditText>(R.id.edtDate)
        val btnSearch = view.findViewById<Button>(R.id.btnSearch)
        val textClose = view.findViewById<TextView>(R.id.text_close)
        val changeSearchContainer = view.findViewById<FrameLayout>(R.id.change_search_container)
        val tvChange = view.findViewById<TextView>(R.id.tvChange)

        edtDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                // Format lại ngày nếu cần (ví dụ: 09/04/2025)
                val selectedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear)
                edtDate.setText(selectedDate)
            }, year, month, day)

            // Không cho chọn ngày trước hôm nay (nếu muốn)
            datePickerDialog.datePicker.minDate = calendar.timeInMillis

            datePickerDialog.show()
        }

        tvChange.setOnClickListener {
            changeSearchContainer.visibility = View.VISIBLE
        }

        textClose.setOnClickListener {
            changeSearchContainer.visibility = View.GONE
        }

        btnSearch.setOnClickListener {
            val from = edtFrom.text.toString().trim()
            val to = edtTo.text.toString().trim()
            val date = edtDate.text.toString().trim()

            if (from.isNotEmpty() && to.isNotEmpty() && date.isNotEmpty()) {
                searchViewModel.setSearchQuery(from, to, date)
                changeSearchContainer.visibility = View.GONE
            } else {
                Toast.makeText(requireContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            }
        }

        // UI elements
        val tvFrom = view.findViewById<TextView>(R.id.tvdiemdi)
        val tvTo = view.findViewById<TextView>(R.id.tvdiemden)
        val tvDate = view.findViewById<TextView>(R.id.tvDate)

        // Observe search query
        searchViewModel.searchQuery.observe(viewLifecycleOwner) { (from, to, date) ->
            tvFrom.text = from
            tvTo.text = to
            tvDate.text = date
            fetchTuyenAndChuyenXe(from, to, date)
        }

        return view
    }


    private fun fetchTuyenAndChuyenXe(from: String, to: String, date: String) {
        tuyenRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var matchedTuyenId: String? = null

                for (tuyenSnap in snapshot.children) {
                    val diemDi = tuyenSnap.child("diemDi").getValue(String::class.java)
                    val diemDen = tuyenSnap.child("diemDen").getValue(String::class.java)

                    if (diemDi.equals(from, ignoreCase = true) && diemDen.equals(to, ignoreCase = true)) {
                        matchedTuyenId = tuyenSnap.child("idTuyen").getValue(String::class.java)
                        break
                    }
                }

                if (matchedTuyenId != null) {
                    fetchChuyenXeByTuyenAndDate(matchedTuyenId, date)
                } else {
                    adapter.submitList(emptyList()) // không có tuyến
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Lỗi kết nối Firebase", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchChuyenXeByTuyenAndDate(tuyenId: String, date: String) {
        // Lấy dữ liệu tuyến trước
        tuyenRef.child(tuyenId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(tuyenSnapshot: DataSnapshot) {
                val diemDi = tuyenSnapshot.child("diemDi").getValue(String::class.java)
                val diemDen = tuyenSnapshot.child("diemDen").getValue(String::class.java)

                // Sau đó mới fetch chuyến xe
                chuyenRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val resultList = mutableListOf<ChuyenXe>()
                        for (chuyenSnap in snapshot.children) {
                            val idTuyen = chuyenSnap.child("idTuyen").getValue(String::class.java)
                            val ngay = chuyenSnap.child("ngayKhoiHanh").getValue(String::class.java)

                            if (idTuyen == tuyenId && ngay == date) {
                                val chuyen = chuyenSnap.getValue(ChuyenXe::class.java)
                                chuyen?.apply {
                                    this.diemDi = diemDi
                                    this.diemDen = diemDen
                                    resultList.add(this)
                                }
                            }
                        }
                        adapter.submitList(resultList)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(requireContext(), "Lỗi tải chuyến xe", Toast.LENGTH_SHORT).show()
                    }
                })
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Lỗi tải tuyến đường", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
