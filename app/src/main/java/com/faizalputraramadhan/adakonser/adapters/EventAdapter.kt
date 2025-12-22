package com.faizalputraramadhan.adakonser.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.faizalputraramadhan.adakonser.R
import com.faizalputraramadhan.adakonser.models.Event
import com.google.android.material.button.MaterialButton

class EventAdapter(
    private val events: List<Event>,
    private val onEventClick: (Event) -> Unit
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvEventName: TextView = view.findViewById(R.id.tvEventName)
        val tvEventDate: TextView = view.findViewById(R.id.tvEventDate)
        val tvEventLocation: TextView = view.findViewById(R.id.tvEventLocation)
        val btnOrder: MaterialButton = view.findViewById(R.id.btnOrder)
        val cardView: CardView = view.findViewById(R.id.cardEvent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]

        holder.tvEventName.text = event.name
        holder.tvEventDate.text = "${event.date} · ${event.time}"
        holder.tvEventLocation.text = event.location

        holder.btnOrder.setOnClickListener {
            onEventClick(event)
        }

        holder.cardView.setOnClickListener {
            onEventClick(event)
        }
    }

    override fun getItemCount() = events.size
}