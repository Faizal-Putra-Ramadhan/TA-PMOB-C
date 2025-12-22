package com.faizalputraramadhan.adakonser

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.faizalputraramadhan.adakonser.adapters.AdminEventAdapter
import com.faizalputraramadhan.adakonser.database.DatabaseHelper
import com.faizalputraramadhan.adakonser.models.Event
import com.google.android.material.bottomnavigation.BottomNavigationView

class AdminDashboardActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var tvEventCount: TextView
    private lateinit var tvTicketCount: TextView
    private lateinit var tvRevenue: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var bottomNav: BottomNavigationView
    private val events = mutableListOf<Event>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        dbHelper = DatabaseHelper(this)

        // Setup toolbar if exists
        findViewById<Toolbar>(R.id.toolbar)?.let { toolbar ->
            setSupportActionBar(toolbar)
        }

        tvEventCount = findViewById(R.id.tvEventCount)
        tvTicketCount = findViewById(R.id.tvTicketCount)
        tvRevenue = findViewById(R.id.tvRevenue)
        recyclerView = findViewById(R.id.recyclerViewEvents)
        bottomNav = findViewById(R.id.bottomNavigation)

        setupRecyclerView()
        loadStatistics()
        loadEvents()
        setupBottomNavigation()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                showLogoutDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Apakah Anda yakin ingin logout dari Admin?")
            .setPositiveButton("Ya") { _, _ ->
                logout()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun logout() {
        val sharedPref = getSharedPreferences("AdaKonserPrefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            clear()
            apply()
        }

        Toast.makeText(this, "Logout berhasil", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun loadStatistics() {
        val totalEvents = dbHelper.getTotalEvents()
        val totalTickets = dbHelper.getTotalTicketsSold()
        val totalRevenue = dbHelper.getTotalRevenue()

        tvEventCount.text = totalEvents.toString()
        tvTicketCount.text = String.format("%,d", totalTickets)
        tvRevenue.text = "IDR ${String.format("%,.0f", totalRevenue)}"
    }

    private fun loadEvents() {
        events.clear()
        val cursor = dbHelper.getAllEvents()

        if (cursor.moveToFirst()) {
            do {
                val event = Event(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EVENT_ID)),
                    name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EVENT_NAME)),
                    description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EVENT_DESC)),
                    date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EVENT_DATE)),
                    time = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EVENT_TIME)),
                    location = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EVENT_LOCATION)),
                    artist = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EVENT_ARTIST))
                )
                events.add(event)
            } while (cursor.moveToNext())
        }
        cursor.close()

        val adapter = AdminEventAdapter(events,
            onEditClick = { event ->
                val intent = Intent(this, AdminAddEventActivity::class.java)
                intent.putExtra("EVENT_ID", event.id)
                startActivity(intent)
            },
            onManageTicketsClick = { event ->
                val intent = Intent(this, AdminManageTicketsActivity::class.java)
                intent.putExtra("EVENT_ID", event.id)
                startActivity(intent)
            }
        )
        recyclerView.adapter = adapter
    }

    private fun setupBottomNavigation() {
        bottomNav.selectedItemId = R.id.nav_home

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
                R.id.nav_tambah_event -> {
                    startActivity(Intent(this, AdminEventManagementActivity::class.java))
                    true
                }
                R.id.nav_promo -> {
                    startActivity(Intent(this, AdminPromoManagementActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadStatistics()
        loadEvents()
    }
}