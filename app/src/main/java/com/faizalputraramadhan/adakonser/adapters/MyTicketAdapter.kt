package com.faizalputraramadhan.adakonser.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.faizalputraramadhan.adakonser.R
import com.faizalputraramadhan.adakonser.models.Order
import com.google.android.material.button.MaterialButton

class MyTicketAdapter(
    private val orders: List<Order>,
    private val onTicketClick: (Order) -> Unit
) : RecyclerView.Adapter<MyTicketAdapter.TicketViewHolder>() {

    class TicketViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvEventName: TextView = view.findViewById(R.id.tvEventName)
        val tvEventDate: TextView = view.findViewById(R.id.tvEventDate)
        val btnView: MaterialButton = view.findViewById(R.id.btnView)
        val cardView: CardView = view.findViewById(R.id.cardTicket)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_my_ticket, parent, false)
        return TicketViewHolder(view)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val order = orders[position]

        holder.tvEventName.text = order.eventName
        holder.tvEventDate.text = "${order.eventDate} · ${order.eventTime}"

        holder.btnView.setOnClickListener {
            onTicketClick(order)
        }

        holder.cardView.setOnClickListener {
            onTicketClick(order)
        }
    }

    override fun getItemCount() = orders.size
}