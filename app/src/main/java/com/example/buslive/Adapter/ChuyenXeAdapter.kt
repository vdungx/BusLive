package com.example.buslive.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.buslive.R
import com.example.buslive.Model.ChuyenXe
import com.google.firebase.database.*

class ChuyenXeAdapter(
    private val onChonChoClick: (ChuyenXe) -> Unit
) : RecyclerView.Adapter<ChuyenXeAdapter.ChuyenXeViewHolder>() {

    private val listChuyenXe = mutableListOf<ChuyenXe>()

    fun submitList(data: List<ChuyenXe>) {
        listChuyenXe.clear()
        listChuyenXe.addAll(data)
        notifyDataSetChanged()
    }

    inner class ChuyenXeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTimeDeparture: TextView = itemView.findViewById(R.id.text_time_departure)
        val txtTimeArrival: TextView = itemView.findViewById(R.id.text_time_arrival)
        val txtLocationDeparture: TextView = itemView.findViewById(R.id.text_location_departure)
        val txtLocationArrival: TextView = itemView.findViewById(R.id.text_location_arrival)
        val imgBus: ImageView = itemView.findViewById(R.id.image_bus)
        val txtBusName: TextView = itemView.findViewById(R.id.text_bus_name)
        val txtBusType: TextView = itemView.findViewById(R.id.text_bus_type)
        val txtRating: TextView = itemView.findViewById(R.id.text_rating)
        val txtNoPrepayment: TextView = itemView.findViewById(R.id.text_no_prepayment)
        val txtConfirmNow: TextView = itemView.findViewById(R.id.text_confirm_now)
        val txtPrice: TextView = itemView.findViewById(R.id.text_price)
        val btnChoose: Button = itemView.findViewById(R.id.button_choose)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChuyenXeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_filter, parent, false)
        return ChuyenXeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChuyenXeViewHolder, position: Int) {
        val chuyen = listChuyenXe[position]

        holder.txtTimeDeparture.text = chuyen.gioDi ?: "--:--"
        holder.txtTimeArrival.text = chuyen.gioDen ?: "--:--"
        holder.txtLocationDeparture.text = chuyen.diemDon ?: "Chưa rõ điểm đón"
        holder.txtLocationArrival.text = chuyen.diemTra ?: "Chưa rõ điểm trả"

        holder.txtBusName.text = chuyen.tenNhaXe ?: "Chưa có tên xe"
        holder.txtBusType.text = chuyen.loaiXe ?: "Chưa rõ loại"

        holder.txtPrice.text = chuyen.giaVe?.let { "${it}đ" } ?: "Chưa rõ"

        // Ẩn các TextView chưa dùng
        holder.txtNoPrepayment.visibility = View.GONE
        holder.txtConfirmNow.visibility = View.GONE


        holder.btnChoose.setOnClickListener {
            onChonChoClick(chuyen)
        }
    }


    override fun getItemCount(): Int = listChuyenXe.size
}

