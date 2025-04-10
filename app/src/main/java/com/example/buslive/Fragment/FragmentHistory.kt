package com.example.buslive.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.buslive.Activity.HistoryActivity
import com.example.buslive.R

class FragmentHistory : Fragment() {
    private lateinit var textViewDetails: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)

        // Giả sử bạn có một nút để khởi động HistoryActivity
        val buttonDetail = view.findViewById<Button>(R.id.detailButton1) // Thay ID bằng ID thực tế của nút
        buttonDetail.setOnClickListener {
            val intent = Intent(requireContext(), HistoryActivity::class.java)
            intent.putExtra("MA_VE", "1") // Thay "1" bằng mã vé thực tế
            startActivity(intent)
        }

        return view
    }
}