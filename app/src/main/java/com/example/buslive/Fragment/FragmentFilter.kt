package com.example.buslive.Fragment

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.buslive.Activity.ChonChoActivity
import com.example.buslive.R
import com.example.buslive.adapter.ChuyenXeAdapter
import com.example.buslive.Model.ChuyenXe
import com.example.buslive.viewmodel.SearchViewModel
import com.google.firebase.database.*
import java.util.*

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
        adapter = ChuyenXeAdapter { selectedChuyenXe ->
            val intent = Intent(requireContext(), ChonChoActivity::class.java)
            intent.putExtra("maChuyen", selectedChuyenXe.idChuyen)
            intent.putExtra("tenNhaXe", selectedChuyenXe.tenNhaXe)
            intent.putExtra("giaVe", selectedChuyenXe.giaVe ?: 0)
            intent.putExtra("gioDi", selectedChuyenXe.gioDi)
            intent.putExtra("ngayKhoiHanh", selectedChuyenXe.ngayKhoiHanh)
            startActivity(intent)
        }


        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Firebase
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
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)

        edtDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(requireContext(), { _, y, m, d ->
                val selectedDate = String.format("%02d/%02d/%04d", d, m + 1, y)
                edtDate.setText(selectedDate)
            }, year, month, day)

            datePickerDialog.datePicker.minDate = calendar.timeInMillis
            datePickerDialog.show()
        }

        toolbar.setNavigationOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
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
                fetchAllChuyenXe()
                changeSearchContainer.visibility = View.GONE
            }

        }

        val tvFrom = view.findViewById<TextView>(R.id.tvdiemdi)
        val tvTo = view.findViewById<TextView>(R.id.tvdiemden)
        val tvDate = view.findViewById<TextView>(R.id.tvDate)

        searchViewModel.searchQuery.observe(viewLifecycleOwner) { query ->
            query?.let { (from, to, date) ->
                tvFrom.text = from
                tvTo.text = to
                tvDate.text = date
                val formattedDate = convertDateFormat(date)
                fetchTuyenAndChuyenXe(from, to, formattedDate)
            }
        }

        if (searchViewModel.searchQuery.value == null) {
            fetchAllChuyenXe()
        }
        return view
    }

    private fun fetchAllChuyenXe() {
        chuyenRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val chuyenList = mutableListOf<ChuyenXe>()

                for (chuyenSnap in snapshot.children) {
                    val chuyen = chuyenSnap.getValue(ChuyenXe::class.java)
                    val idTuyen = chuyenSnap.child("idTuyen").getValue(String::class.java)

                    if (chuyen != null && idTuyen != null) {
                        // Lấy thêm điểm đi, điểm đến từ tuyến đường
                        tuyenRef.child(idTuyen).addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(tuyenSnapshot: DataSnapshot) {
                                chuyen.diemDi = tuyenSnapshot.child("diemDi").getValue(String::class.java)
                                chuyen.diemDen = tuyenSnapshot.child("diemDen").getValue(String::class.java)
                                chuyenList.add(chuyen)

                                // Chỉ submit danh sách sau khi xử lý hết
                                if (chuyenList.size == snapshot.childrenCount.toInt()) {
                                    adapter.submitList(chuyenList)
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Bỏ qua nếu có lỗi khi lấy tuyến
                            }
                        })
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Lỗi tải danh sách chuyến xe", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchTuyenAndChuyenXe(from: String, to: String, date: String) {
        tuyenRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var matchedTuyenId: String? = null

                for (tuyenSnap in snapshot.children) {
                    val diemDi = tuyenSnap.child("diemDi").getValue(String::class.java) ?: ""
                    val diemDen = tuyenSnap.child("diemDen").getValue(String::class.java) ?: ""

                    if (diemDi.equals(from, ignoreCase = true) && diemDen.equals(to, ignoreCase = true)) {
                        matchedTuyenId = tuyenSnap.key // Fix: dùng key thay vì idTuyen trong node
                        break
                    }
                }

                if (matchedTuyenId != null) {
                    fetchChuyenXeByTuyenAndDate(matchedTuyenId, date)
                } else {
                    adapter.submitList(emptyList())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Lỗi kết nối Firebase", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchChuyenXeByTuyenAndDate(tuyenId: String, date: String) {
        tuyenRef.child(tuyenId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(tuyenSnapshot: DataSnapshot) {
                val diemDi = tuyenSnapshot.child("diemDi").getValue(String::class.java)
                val diemDen = tuyenSnapshot.child("diemDen").getValue(String::class.java)

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

    private fun convertDateFormat(date: String): String {
        val parts = date.split("/")
        return if (parts.size == 3) {
            "${parts[2]}-${parts[1]}-${parts[0]}"
        } else date
    }
}
