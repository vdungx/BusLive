package com.example.buslive.adapter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.buslive.Model.Cabin
import com.example.buslive.R

class CabinAdapter(
    private val listCabin: List<Cabin>
) : RecyclerView.Adapter<CabinAdapter.CabinViewHolder>() {

    // Lấy danh sách cabin không trùng loại
    private val getFilteredList: List<Cabin>
        get() = listCabin
            .filter { !it.loai.isNullOrBlank() }
            .distinctBy { it.loai?.trim()?.lowercase() }

    inner class CabinViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtLoai = itemView.findViewById<TextView>(R.id.txtLoaiCabin)
        val txtMoTa = itemView.findViewById<TextView>(R.id.txtMoTaCabin)
        val txtGia = itemView.findViewById<TextView>(R.id.txtGiaCabin)
        val layoutCabin = itemView.findViewById<LinearLayout>(R.id.layoutCabinItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CabinViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cabin, parent, false)
        return CabinViewHolder(view)
    }

    override fun onBindViewHolder(holder: CabinViewHolder, position: Int) {
        val cabin = getFilteredList[position]
        holder.txtLoai.text = cabin.loai
        holder.txtMoTa.text = "Tầng ${cabin.tang}, ghế ${cabin.viTri}"
        holder.txtGia.text = "Giá: ${cabin.gia?.toInt() ?: 0} đ"

        val strokeDrawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = 24f
            setColor(Color.WHITE)
            setStroke(5, when (cabin.loai?.lowercase()) {
                "cabin đơn" -> Color.parseColor("#FF5722")
                "cabin đôi" -> Color.parseColor("#3F51B5")
                "cabin vip" -> Color.parseColor("#FFC107")
                else -> Color.GRAY
            })
        }
        holder.layoutCabin.background = strokeDrawable
    }

    override fun getItemCount(): Int = getFilteredList.size
}

