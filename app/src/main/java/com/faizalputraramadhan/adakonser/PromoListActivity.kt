package com.faizalputraramadhan.adakonser

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.faizalputraramadhan.adakonser.adapters.PromoListAdapter
import com.faizalputraramadhan.adakonser.database.DatabaseHelper
import com.faizalputraramadhan.adakonser.models.Promo
import com.google.android.material.bottomnavigation.BottomNavigationView

class PromoListActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var bottomNav: BottomNavigationView
    private val promos = mutableListOf<Promo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_promo_list)

        dbHelper = DatabaseHelper(this)
        recyclerView = findViewById(R.id.recyclerViewPromos)
        bottomNav = findViewById(R.id.bottomNavigation)

        setupRecyclerView()
        loadPromos()
        setupBottomNavigation()
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun loadPromos() {
        promos.clear()
        val cursor = dbHelper.getAllPromos()

        if (cursor.moveToFirst()) {
            do {
                val promo = Promo(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PROMO_ID)),
                    name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PROMO_NAME)),
                    discount = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PROMO_DISCOUNT)),
                    startDate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PROMO_START_DATE)),
                    endDate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PROMO_END_DATE)),
                    terms = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PROMO_TERMS))
                )
                promos.add(promo)
            } while (cursor.moveToNext())
        }
        cursor.close()

        val adapter = PromoListAdapter(promos) { promo ->
            Toast.makeText(this, "Promo ${promo.name} diklaim! Gunakan saat pembelian tiket", Toast.LENGTH_SHORT).show()
        }
        recyclerView.adapter = adapter
    }

    private fun setupBottomNavigation() {
        bottomNav.selectedItemId = R.id.nav_promo

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_promo -> true
                R.id.nav_tickets -> {
                    startActivity(Intent(this, MyTicketsActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }
}