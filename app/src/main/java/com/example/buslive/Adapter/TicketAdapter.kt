package com.example.buslive.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.buslive.Activity.HistoryActivity
import com.example.buslive.Model.TicketModal
import com.example.buslive.R

class TicketAdapter(private val tickets: List<TicketModal>, private val context: Context) :
    RecyclerView.Adapter<TicketAdapter.TicketViewHolder>() {

    class TicketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val routeTextView: TextView = itemView.findViewById(R.id.routeTextView)
        val companyTextView: TextView = itemView.findViewById(R.id.companyTextView)
        val typeTextView: TextView = itemView.findViewById(R.id.typeTextView)
        val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)
        val bookingTimeTextView: TextView = itemView.findViewById(R.id.bookingTimeTextView)
        val detailButton: Button = itemView.findViewById(R.id.detailButton1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ticket, parent, false)
        return TicketViewHolder(view)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val ticket = tickets[position]
        holder.routeTextView.text = "${ticket.diemDi} - ${ticket.diemDen}"
        holder.companyTextView.text = ticket.tenNhaXe
        holder.typeTextView.text = ticket.loaiXe
        holder.timeTextView.text = "${ticket.gioKhoiHanh} - ${ticket.ngayKhoiHanh}"
        holder.bookingTimeTextView.text = ticket.thoiGianDatVe

        holder.detailButton.setOnClickListener {
            val intent = Intent(context, HistoryActivity::class.java)
            intent.putExtra("MA_VE", ticket.maVe)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = tickets.size
}
