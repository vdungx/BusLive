package com.example.buslive.Adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.buslive.Model.Cabin
import com.example.buslive.R

class SeatAdapter(
    private val context: Context,
    private val cabinList: List<Cabin>
) : RecyclerView.Adapter<SeatAdapter.SeatViewHolder>() {

    inner class SeatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val seatBackground: ImageView = itemView.findViewById(R.id.seatBackground)
        val seatTextView: TextView = itemView.findViewById(R.id.seatTextView)
        val layout: LinearLayout = itemView.findViewById(R.id.layoutSeatItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeatViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_seat, parent, false)
        return SeatViewHolder(view)
    }

    override fun onBindViewHolder(holder: SeatViewHolder, position: Int) {
        val cabin = cabinList[position]

        // Bind the seat number and other relevant info from Cabin model
        holder.seatTextView.text = cabin.viTri

        // Tùy vào trạng thái ghế, chọn icon và màu sắc viền
        when (cabin.trangThai) {
            "DaDat" -> {
                holder.seatBackground.setImageResource(R.drawable.ic_selected_seat) // Ghế đang chọn
                holder.layout.background = createSeatBackground(Color.parseColor("#FF5722"))
            }
            "KhongBan" -> {
                holder.seatBackground.setImageResource(R.drawable.ic_unavailable_seat) // Ghế không bán
                holder.layout.background = createSeatBackground(Color.GRAY)
            }
            else -> {
                // Ghế trống
                val borderColor = getSeatColor(cabin.loai)
                holder.seatBackground.setImageResource(R.drawable.ic_available_seat) // Ghế trống
                holder.layout.background = createSeatBackground(borderColor)
            }
        }
    }

    override fun getItemCount(): Int = cabinList.size

    // Utility function to get seat color based on type
    private fun getSeatColor(type: String?): Int {
        return when (type?.lowercase()) {
            "cabin đơn" -> Color.parseColor("#FF5722") // Cam
            "cabin đôi nhỏ" -> Color.parseColor("#3F51B5") // Xanh đậm
            "cabin đôi lớn" -> Color.parseColor("#FFC107") // Vàng
            else -> Color.GRAY // Mặc định nếu không xác định
        }
    }

    // Create seat background with color and rounded edges
    private fun createSeatBackground(borderColor: Int): GradientDrawable {
        return GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = 12f
            setColor(Color.WHITE) // Nền trắng
            setStroke(4, borderColor) // Màu viền
        }
    }
}
