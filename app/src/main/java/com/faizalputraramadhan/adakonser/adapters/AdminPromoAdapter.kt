package com.faizalputraramadhan.adakonser.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.faizalputraramadhan.adakonser.R
import com.faizalputraramadhan.adakonser.models.Promo

class AdminPromoAdapter(
    private val promos: List<Promo>,
    private val onEditClick: (Promo) -> Unit,
    private val onDeleteClick: (Promo) -> Unit
) : RecyclerView.Adapter<AdminPromoAdapter.PromoViewHolder>() {

    class PromoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvPromoName: TextView = view.findViewById(R.id.tvPromoName)
        val tvPromoDiscount: TextView = view.findViewById(R.id.tvPromoDiscount)
        val tvPromoTerms: TextView = view.findViewById(R.id.tvPromoTerms)
        val btnEdit: ImageButton = view.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_admin_promo, parent, false)
        return PromoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PromoViewHolder, position: Int) {
        val promo = promos[position]

        holder.tvPromoName.text = promo.name
        holder.tvPromoDiscount.text = "${promo.discount}% OFF"
        holder.tvPromoTerms.text = promo.terms

        holder.btnEdit.setOnClickListener {
            onEditClick(promo)
        }

        holder.btnDelete.setOnClickListener {
            onDeleteClick(promo)
        }
    }

    override fun getItemCount() = promos.size
}