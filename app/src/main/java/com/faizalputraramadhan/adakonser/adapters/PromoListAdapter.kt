package com.faizalputraramadhan.adakonser.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.faizalputraramadhan.adakonser.R
import com.faizalputraramadhan.adakonser.models.Promo
import com.google.android.material.button.MaterialButton

class PromoListAdapter(
    private val promos: List<Promo>,
    private val onClaimClick: (Promo) -> Unit
) : RecyclerView.Adapter<PromoListAdapter.PromoViewHolder>() {

    class PromoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvPromoName: TextView = view.findViewById(R.id.tvPromoName)
        val tvPromoTerms: TextView = view.findViewById(R.id.tvPromoTerms)
        val btnClaim: MaterialButton = view.findViewById(R.id.btnClaim)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_promo_list, parent, false)
        return PromoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PromoViewHolder, position: Int) {
        val promo = promos[position]

        holder.tvPromoName.text = promo.name
        holder.tvPromoTerms.text = promo.terms

        holder.btnClaim.setOnClickListener {
            onClaimClick(promo)
        }
    }

    override fun getItemCount() = promos.size
}