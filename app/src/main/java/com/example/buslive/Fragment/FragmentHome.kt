package com.example.buslive.Fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.buslive.R
import com.example.buslive.viewmodel.SearchViewModel
import java.util.Calendar

class FragmentHome : Fragment() {

    private lateinit var edtFrom: EditText
    private lateinit var edtTo: EditText
    private lateinit var edtDate: EditText
    private lateinit var btnSearch: Button

    private val searchViewModel: SearchViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        edtFrom = view.findViewById(R.id.edtFrom)
        edtTo = view.findViewById(R.id.edtTo)
        edtDate = view.findViewById(R.id.edtDate)
        btnSearch = view.findViewById(R.id.btnSearch)


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

        btnSearch.setOnClickListener {
            val from = edtFrom.text.toString()
            val to = edtTo.text.toString()
            val date = edtDate.text.toString()

            if (from.isBlank() || to.isBlank() || date.isBlank()) {
                Toast.makeText(requireContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            } else {
                searchViewModel.setSearchQuery(from, to, date)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, FragmentFilter())
                    .addToBackStack(null)
                    .commit()
            }
        }

        return view
    }
}
