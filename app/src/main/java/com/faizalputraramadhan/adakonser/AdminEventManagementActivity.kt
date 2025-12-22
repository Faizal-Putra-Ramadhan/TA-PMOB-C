package com.faizalputraramadhan.adakonser

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.bottomnavigation.BottomNavigationView

class AdminEventManagementActivity : AppCompatActivity() {
    private lateinit var cardAddEvent: CardView
    private lateinit var cardManageEvent: CardView
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_event_management)

        cardAddEvent = findViewById(R.id.cardAddEvent)
        cardManageEvent = findViewById(R.id.cardManageEvent)
        bottomNav = findViewById(R.id.bottomNavigation)

        setupListeners()
        setupBottomNavigation()
    }

    private fun setupListeners() {
        cardAddEvent.setOnClickListener {
            startActivity(Intent(this, AdminAddEventActivity::class.java))
        }

        cardManageEvent.setOnClickListener {
            startActivity(Intent(this, AdminDashboardActivity::class.java))
        }
    }

    private fun setupBottomNavigation() {
        bottomNav.selectedItemId = R.id.nav_tambah_event

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, AdminDashboardActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_tambah_event -> true
                R.id.nav_promo -> {
                    startActivity(Intent(this, AdminPromoManagementActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }
}