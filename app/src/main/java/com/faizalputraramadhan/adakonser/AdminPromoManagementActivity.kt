package com.faizalputraramadhan.adakonser

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.faizalputraramadhan.adakonser.adapters.AdminPromoAdapter
import com.faizalputraramadhan.adakonser.database.DatabaseHelper
import com.faizalputraramadhan.adakonser.models.Promo
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import android.content.Intent

class AdminPromoManagementActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var etPromoName: EditText
    private lateinit var etPromoDiscount: EditText
    private lateinit var etPromoStartDate: EditText
    private lateinit var etPromoEndDate: EditText
    private lateinit var etPromoTerms: EditText
    private lateinit var btnAddPromo: MaterialButton
    private lateinit var bottomNav: BottomNavigationView

    private val promos = mutableListOf<Promo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_promo_management)

        dbHelper = DatabaseHelper(this)

        initViews()
        setupRecyclerView()
        loadPromos()
        setupListeners()
        setupBottomNavigation()
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerViewPromos)
        etPromoName = findViewById(R.id.etPromoName)
        etPromoDiscount = findViewById(R.id.etPromoDiscount)
        etPromoStartDate = findViewById(R.id.etPromoStartDate)
        etPromoEndDate = findViewById(R.id.etPromoEndDate)
        etPromoTerms = findViewById(R.id.etPromoTerms)
        btnAddPromo = findViewById(R.id.btnAddPromo)
        bottomNav = findViewById(R.id.bottomNavigation)
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

        val adapter = AdminPromoAdapter(promos,
            onEditClick = { promo ->
                // Edit functionality
                editPromo(promo)
            },
            onDeleteClick = { promo ->
                deletePromo(promo)
            }
        )
        recyclerView.adapter = adapter
    }

    private fun setupListeners() {
        btnAddPromo.setOnClickListener {
            addPromo()
        }
    }

    private fun addPromo() {
        val name = etPromoName.text.toString().trim()
        val discountStr = etPromoDiscount.text.toString().trim()
        val startDate = etPromoStartDate.text.toString().trim()
        val endDate = etPromoEndDate.text.toString().trim()
        val terms = etPromoTerms.text.toString().trim()

        if (name.isEmpty() || discountStr.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
            Toast.makeText(this, "Mohon lengkapi data yang wajib diisi", Toast.LENGTH_SHORT).show()
            return
        }

        val discount = discountStr.toIntOrNull()

        if (discount == null || discount < 0 || discount > 100) {
            Toast.makeText(this, "Diskon harus antara 0-100", Toast.LENGTH_SHORT).show()
            return
        }

        val result = dbHelper.insertPromo(name, discount, startDate, endDate, terms)

        if (result > 0) {
            Toast.makeText(this, "Promo berhasil ditambahkan", Toast.LENGTH_SHORT).show()
            clearFields()
            loadPromos()
        } else {
            Toast.makeText(this, "Gagal menambahkan promo", Toast.LENGTH_SHORT).show()
        }
    }

    private fun editPromo(promo: Promo) {
        etPromoName.setText(promo.name)
        etPromoDiscount.setText(promo.discount.toString())
        etPromoStartDate.setText(promo.startDate)
        etPromoEndDate.setText(promo.endDate)
        etPromoTerms.setText(promo.terms)

        btnAddPromo.text = "Update Promo"
    }

    private fun deletePromo(promo: Promo) {
        val result = dbHelper.deletePromo(promo.id)

        if (result > 0) {
            Toast.makeText(this, "Promo berhasil dihapus", Toast.LENGTH_SHORT).show()
            loadPromos()
        } else {
            Toast.makeText(this, "Gagal menghapus promo", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearFields() {
        etPromoName.text.clear()
        etPromoDiscount.text.clear()
        etPromoStartDate.text.clear()
        etPromoEndDate.text.clear()
        etPromoTerms.text.clear()
        btnAddPromo.text = "Buat Promo"
    }

    private fun setupBottomNavigation() {
        bottomNav.selectedItemId = R.id.nav_promo

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, AdminDashboardActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_tambah_event -> {
                    startActivity(Intent(this, AdminEventManagementActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_promo -> true
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadPromos()
    }
}