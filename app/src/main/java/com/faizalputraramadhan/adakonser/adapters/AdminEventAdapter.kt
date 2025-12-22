package com.faizalputraramadhan.adakonser.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.faizalputraramadhan.adakonser.R
import com.faizalputraramadhan.adakonser.models.Event
import com.google.android.material.button.MaterialButton

class AdminEventAdapter(
    private val events: List<Event>,
    private val onEditClick: (Event) -> Unit,
    private val onManageTicketsClick: (Event) -> Unit
) : RecyclerView.Adapter<AdminEventAdapter.EventViewHolder>() {

    class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvEventName: TextView = view.findViewById(R.id.tvEventName)
        val tvEventDate: TextView = view.findViewById(R.id.tvEventDate)
        val tvEventLocation: TextView = view.findViewById(R.id.tvEventLocation)
        val btnEdit: ImageButton = view.findViewById(R.id.btnEdit)
        val btnManageTickets: MaterialButton = view.findViewById(R.id.btnManageTickets)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_admin_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]

        holder.tvEventName.text = event.name
        holder.tvEventDate.text = "${event.date} · ${event.time}"
        holder.tvEventLocation.text = event.location

        holder.btnEdit.setOnClickListener {
            onEditClick(event)
        }

        holder.btnManageTickets.setOnClickListener {
            onManageTicketsClick(event)
        }
    }

    override fun getItemCount() = events.size
}