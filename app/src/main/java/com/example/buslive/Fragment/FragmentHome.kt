package com.example.buslive.Fragment

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.buslive.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.Calendar

class FragmentHome : Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val edtFrom = view.findViewById<EditText>(R.id.edtFrom)
        val edtTo = view.findViewById<EditText>(R.id.edtTo)
        val edtDate = view.findViewById<EditText>(R.id.edtDate)
        val btnSearch = view.findViewById<Button>(R.id.btnSearch)
        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        // Bắt sự kiện DatePicker
        edtDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(requireContext(),
                { _, year, month, day ->
                    val formatted = "%02d/%02d/%04d".format(day, month + 1, year)
                    edtDate.setText(formatted)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

        // Bắt sự kiện nút tìm kiếm
        btnSearch.setOnClickListener {
            val from = edtFrom.text.toString()
            val to = edtTo.text.toString()
            val date = edtDate.text.toString()

            if (from.isBlank() || to.isBlank() || date.isBlank()) {
                Toast.makeText(requireContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(requireContext(), ResultActivity::class.java).apply {
                    putExtra("FROM", from)
                    putExtra("TO", to)
                    putExtra("DATE", date)
                }
                startActivity(intent)
            }
        }
    }
}