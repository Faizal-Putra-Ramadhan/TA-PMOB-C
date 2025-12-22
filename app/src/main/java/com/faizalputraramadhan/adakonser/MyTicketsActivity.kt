package com.faizalputraramadhan.adakonser

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.faizalputraramadhan.adakonser.adapters.MyTicketAdapter
import com.faizalputraramadhan.adakonser.database.DatabaseHelper
import com.faizalputraramadhan.adakonser.models.Order
import com.google.android.material.bottomnavigation.BottomNavigationView

class MyTicketsActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var bottomNav: BottomNavigationView
    private val orders = mutableListOf<Order>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_tickets)

        dbHelper = DatabaseHelper(this)
        recyclerView = findViewById(R.id.recyclerViewMyTickets)
        bottomNav = findViewById(R.id.bottomNavigation)

        setupRecyclerView()
        loadOrders()
        setupBottomNavigation()
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun loadOrders() {
        orders.clear()
        val cursor = dbHelper.getAllOrders()

        if (cursor.moveToFirst()) {
            do {
                val order = Order(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_ID)),
                    eventId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_EVENT_ID)),
                    eventName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EVENT_NAME)),
                    eventDate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EVENT_DATE)),
                    eventTime = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EVENT_TIME)),
                    eventLocation = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EVENT_LOCATION)),
                    ticketType = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_TICKET_TYPE)),
                    seatNumber = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_SEAT_NUMBER)),
                    name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_NAME)),
                    email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_EMAIL)),
                    phone = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_PHONE)),
                    promoId = if (cursor.isNull(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_PROMO_ID))) null
                    else cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_PROMO_ID)),
                    total = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_TOTAL)),
                    orderDate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_DATE)),
                    qrCode = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_QR_CODE))
                )
                orders.add(order)
            } while (cursor.moveToNext())
        }
        cursor.close()

        val adapter = MyTicketAdapter(orders) { order ->
            val intent = Intent(this, TicketDetailActivity::class.java)
            intent.putExtra("ORDER_ID", order.id)
            startActivity(intent)
        }
        recyclerView.adapter = adapter
    }

    private fun setupBottomNavigation() {
        bottomNav.selectedItemId = R.id.nav_tickets

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_promo -> {
                    startActivity(Intent(this, PromoListActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_tickets -> true
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadOrders()
    }
}