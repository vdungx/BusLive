package com.example.buslive.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.buslive.Model.Cabin
import com.example.buslive.R

class SeatAdapter(
    private val context: Context,
    private val cabinList: List<Cabin>
) : RecyclerView.Adapter<SeatAdapter.SeatViewHolder>() {

    private var selectedPosition = RecyclerView.NO_POSITION
    var onSeatClick: ((Cabin) -> Unit)? = null

    inner class SeatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val seatBackground: ImageView = itemView.findViewById(R.id.seatBackground)
        val seatTextView: TextView = itemView.findViewById(R.id.seatTextView)
        val layout: LinearLayout = itemView.findViewById(R.id.layoutSeatItem)

        init {
            layout.setOnClickListener {
                val cabin = cabinList[adapterPosition]
                if (cabin.trangThai == "Trong") {
                    val previous = selectedPosition
                    selectedPosition = adapterPosition
                    notifyItemChanged(previous)
                    notifyItemChanged(selectedPosition)
                    onSeatClick?.invoke(cabin)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeatViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_seat, parent, false)
        return SeatViewHolder(view)
    }

    override fun onBindViewHolder(holder: SeatViewHolder, position: Int) {
        val cabin = cabinList[position]
        holder.seatTextView.text = cabin.viTri

        if (cabin.trangThai == "DaDat") {
            holder.seatBackground.setImageResource(R.drawable.ic_selected_seat)
            holder.layout.background = createSeatBackground(Color.parseColor("#FF5722"))
        } else if (cabin.trangThai == "KhongBan") {
            holder.seatBackground.setImageResource(R.drawable.ic_unavailable_seat)
            holder.layout.background = createSeatBackground(Color.GRAY)
        } else {
            val borderColor = if (selectedPosition == position)
                Color.parseColor("#4CAF50") // Màu viền khi chọn
            else
                getSeatColor(cabin.loai)

            holder.seatBackground.setImageResource(R.drawable.ic_available_seat)
            holder.layout.background = createSeatBackground(borderColor)
        }
    }

    override fun getItemCount(): Int = cabinList.size

    fun clearSelection() {
        val previous = selectedPosition
        selectedPosition = RecyclerView.NO_POSITION
        notifyItemChanged(previous)
    }

    private fun getSeatColor(type: String?): Int {
        return when (type?.lowercase()) {
            "cabin đơn" -> Color.parseColor("#FF5722") // Cam
            "cabin đôi" -> Color.parseColor("#3F51B5") // Xanh đậm
            else -> Color.GRAY
        }
    }

    private fun createSeatBackground(borderColor: Int): GradientDrawable {
        return GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = 12f
            setColor(Color.WHITE)
            setStroke(4, borderColor)
        }
    }
}
