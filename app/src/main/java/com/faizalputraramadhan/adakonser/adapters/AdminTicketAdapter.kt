package com.faizalputraramadhan.adakonser.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.faizalputraramadhan.adakonser.R
import com.faizalputraramadhan.adakonser.models.Ticket

class AdminTicketAdapter(
    private val tickets: List<Ticket>,
    private val onEditClick: (Ticket) -> Unit,
    private val onDeleteClick: (Ticket) -> Unit
) : RecyclerView.Adapter<AdminTicketAdapter.TicketViewHolder>() {

    class TicketViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTicketType: TextView = view.findViewById(R.id.tvTicketType)
        val tvTicketPrice: TextView = view.findViewById(R.id.tvTicketPrice)
        val tvTicketStock: TextView = view.findViewById(R.id.tvTicketStock)
        val btnEdit: ImageButton = view.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_admin_ticket, parent, false)
        return TicketViewHolder(view)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val ticket = tickets[position]

        holder.tvTicketType.text = ticket.type
        holder.tvTicketPrice.text = "IDR ${String.format("%,.0f", ticket.price)}"
        holder.tvTicketStock.text = "${ticket.available}/${ticket.stock} Tersisa"

        holder.btnEdit.setOnClickListener {
            onEditClick(ticket)
        }

        holder.btnDelete.setOnClickListener {
            onDeleteClick(ticket)
        }
    }

    override fun getItemCount() = tickets.size
}