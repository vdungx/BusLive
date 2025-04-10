package com.example.buslive.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.buslive.Model.Cabin
import com.example.buslive.Model.Ticket
import com.example.buslive.R

class TicketAdapter(private val context: Context, private val ticketList: List<Ticket>, private val cabins: Map<String, Cabin>) : RecyclerView.Adapter<TicketAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_ticket, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ticket = ticketList[position]
        val cabin = cabins[ticket.maCabin]

        holder.routeTextView.text = "${cabin?.viTri} - ${cabin?.maChuyen}"
        holder.bookingTimeTextView.text = ticket.maVe // Cần điều chỉnh nếu có thông tin khác
        holder.companyTextView.text = cabin?.loai // Hoặc thông tin khác từ cabin
        holder.typeTextView.text = cabin?.loai

        holder.detailButton.setOnClickListener {
            // Xử lý sự kiện khi nhấn nút chi tiết
        }
    }

    override fun getItemCount(): Int {
        return ticketList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val routeTextView: TextView = itemView.findViewById(R.id.routeTextView)
        val bookingTimeTextView: TextView = itemView.findViewById(R.id.bookingTimeTextView)
        val companyTextView: TextView = itemView.findViewById(R.id.companyTextView)
        val typeTextView: TextView = itemView.findViewById(R.id.typeTextView)
        val detailButton: Button = itemView.findViewById(R.id.detailButton1)
    }
}