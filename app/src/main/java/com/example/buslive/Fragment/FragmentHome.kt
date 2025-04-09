package com.example.buslive.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.buslive.R

class FragmentHome : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    // Ánh xạ view
//        bottomNavigationView = findViewById(R.id.bottomNavigationView)
//        edtFrom = findViewById(R.id.edtFrom)
//        edtTo = findViewById(R.id.edtTo)
//        edtDate = findViewById(R.id.edtDate)
//        btnSearch = findViewById(R.id.btnSearch)

    // Bắt sự kiện tìm kiếm
//        btnSearch.setOnClickListener {
//            val from = edtFrom.text.toString()
//            val to = edtTo.text.toString()
//            val date = edtDate.text.toString()
//
//            if (from.isBlank() || to.isBlank() || date.isBlank()) {
//                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
//            } else {
//                // Gửi dữ liệu sang màn kết quả
//                val intent = Intent(this, ResultActivity::class.java).apply {
//                    putExtra("FROM", from)
//                    putExtra("TO", to)
//                    putExtra("DATE", date)
//                }
//                startActivity(intent)
//            }
//        }

//        edtDate.setOnClickListener {
//            val calendar = Calendar.getInstance()
//            val year = calendar.get(Calendar.YEAR)
//            val month = calendar.get(Calendar.MONTH)
//            val day = calendar.get(Calendar.DAY_OF_MONTH)
//
//            val datePickerDialog = DatePickerDialog(this,
//                { _, selectedYear, selectedMonth, selectedDay ->
//                    val formattedDate = "%02d/%02d/%04d".format(selectedDay, selectedMonth + 1, selectedYear)
//                    edtDate.setText(formattedDate)
//                }, year, month, day)
//
//            datePickerDialog.show()
//        }

    // Xử lý BottomNavigation
    //        bottomNavigationView.setOnItemSelectedListener { item ->
    //            when (item.itemId) {
    //                R.id.navigation_home -> {
    //                    startActivity(Intent(this, HomeActivity::class.java))
    //                    true
    //                }
    //                R.id.navigation_location -> {
    //                    startActivity(Intent(this, ResultActivity::class.java))
    //                    true
    //                }
    //                R.id.navigation_history -> {
    //                    startActivity(Intent(this, HistoryActivity::class.java))
    //                    true
    //                }
    //                R.id.navigation_account -> {
    //                    startActivity(Intent(this, AccountActivity::class.java))
    //                    true
    //                }
    //                else -> false
    //            }
    //        }
}