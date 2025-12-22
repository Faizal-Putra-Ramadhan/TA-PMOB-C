package com.faizalputraramadhan.adakonser


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.faizalputraramadhan.adakonser.adapters.EventAdapter
import com.faizalputraramadhan.adakonser.adapters.PromoAdapter
import com.faizalputraramadhan.adakonser.database.DatabaseHelper
import com.faizalputraramadhan.adakonser.models.Event
import com.faizalputraramadhan.adakonser.models.Promo
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var eventRecyclerView: RecyclerView
    private lateinit var promoRecyclerView: RecyclerView
    private lateinit var bottomNav: BottomNavigationView

    private val events = mutableListOf<Event>()
    private val promos = mutableListOf<Promo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)

        // Setup toolbar if exists
        findViewById<Toolbar>(R.id.toolbar)?.let { toolbar ->
            setSupportActionBar(toolbar)
        }

        eventRecyclerView = findViewById(R.id.recyclerViewEvents)
        promoRecyclerView = findViewById(R.id.recyclerViewPromos)
        bottomNav = findViewById(R.id.bottomNavigation)

        setupRecyclerViews()
        loadEvents()
        loadPromos()
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
            .setMessage("Apakah Anda yakin ingin logout?")
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

    private fun setupRecyclerViews() {
        eventRecyclerView.layoutManager = LinearLayoutManager(this)
        promoRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
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

        val adapter = EventAdapter(events) { event ->
            val intent = Intent(this, BuyTicketActivity::class.java)
            intent.putExtra("EVENT_ID", event.id)
            startActivity(intent)
        }
        eventRecyclerView.adapter = adapter
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

        val adapter = PromoAdapter(promos) { promo ->
            Toast.makeText(this, "Klaim promo di halaman pembelian tiket", Toast.LENGTH_SHORT).show()
        }
        promoRecyclerView.adapter = adapter
    }

    private fun setupBottomNavigation() {
        bottomNav.selectedItemId = R.id.nav_home

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
                R.id.nav_tambah_event -> {
                    // For admin access
                    startActivity(Intent(this, AdminDashboardActivity::class.java))
                    true
                }
                R.id.nav_promo -> {
                    startActivity(Intent(this, PromoListActivity::class.java))
                    true
                }
                R.id.nav_tickets -> {
                    startActivity(Intent(this, MyTicketsActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadEvents()
        loadPromos()
    }
}