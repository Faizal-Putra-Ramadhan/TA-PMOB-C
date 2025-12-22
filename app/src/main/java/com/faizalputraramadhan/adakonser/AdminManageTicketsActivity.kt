package com.faizalputraramadhan.adakonser

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.faizalputraramadhan.adakonser.adapters.AdminTicketAdapter
import com.faizalputraramadhan.adakonser.database.DatabaseHelper
import com.faizalputraramadhan.adakonser.models.Ticket
import com.google.android.material.button.MaterialButton

class AdminManageTicketsActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private var eventId: Int = -1

    private lateinit var tvEventName: TextView
    private lateinit var tvEventDetails: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var etTicketType: EditText
    private lateinit var etTicketPrice: EditText
    private lateinit var etTicketStock: EditText
    private lateinit var btnAddTicket: MaterialButton
    private lateinit var btnSaveChanges: MaterialButton

    private val tickets = mutableListOf<Ticket>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_manage_tickets)

        dbHelper = DatabaseHelper(this)
        eventId = intent.getIntExtra("EVENT_ID", -1)

        if (eventId == -1) {
            finish()
            return
        }

        initViews()
        loadEventInfo()
        loadTickets()
        setupListeners()
    }

    private fun initViews() {
        tvEventName = findViewById(R.id.tvEventName)
        tvEventDetails = findViewById(R.id.tvEventDetails)
        recyclerView = findViewById(R.id.recyclerViewTickets)
        etTicketType = findViewById(R.id.etTicketType)
        etTicketPrice = findViewById(R.id.etTicketPrice)
        etTicketStock = findViewById(R.id.etTicketStock)
        btnAddTicket = findViewById(R.id.btnAddTicket)
        btnSaveChanges = findViewById(R.id.btnSaveChanges)

        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun loadEventInfo() {
        val cursor = dbHelper.getEventById(eventId)

        if (cursor.moveToFirst()) {
            val name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EVENT_NAME))
            val date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EVENT_DATE))
            val time = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EVENT_TIME))
            val location = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EVENT_LOCATION))

            tvEventName.text = name
            tvEventDetails.text = "$date, $time · $location"
        }
        cursor.close()
    }

    private fun loadTickets() {
        tickets.clear()
        val cursor = dbHelper.getTicketsByEventId(eventId)

        if (cursor.moveToFirst()) {
            do {
                val ticket = Ticket(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TICKET_ID)),
                    eventId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TICKET_EVENT_ID)),
                    type = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TICKET_TYPE)),
                    price = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TICKET_PRICE)),
                    stock = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TICKET_STOCK)),
                    available = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TICKET_AVAILABLE))
                )
                tickets.add(ticket)
            } while (cursor.moveToNext())
        }
        cursor.close()

        val adapter = AdminTicketAdapter(tickets,
            onEditClick = { ticket ->
                // Edit functionality
            },
            onDeleteClick = { ticket ->
                deleteTicket(ticket)
            }
        )
        recyclerView.adapter = adapter
    }

    private fun setupListeners() {
        btnAddTicket.setOnClickListener {
            addTicket()
        }

        btnSaveChanges.setOnClickListener {
            Toast.makeText(this, "Perubahan disimpan", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun addTicket() {
        val type = etTicketType.text.toString().trim()
        val priceStr = etTicketPrice.text.toString().trim()
        val stockStr = etTicketStock.text.toString().trim()

        if (type.isEmpty() || priceStr.isEmpty() || stockStr.isEmpty()) {
            Toast.makeText(this, "Mohon lengkapi semua field", Toast.LENGTH_SHORT).show()
            return
        }

        val price = priceStr.toDoubleOrNull()
        val stock = stockStr.toIntOrNull()

        if (price == null || stock == null) {
            Toast.makeText(this, "Format harga atau stok tidak valid", Toast.LENGTH_SHORT).show()
            return
        }

        val result = dbHelper.insertTicket(eventId, type, price, stock)

        if (result > 0) {
            Toast.makeText(this, "Tiket berhasil ditambahkan", Toast.LENGTH_SHORT).show()
            etTicketType.text.clear()
            etTicketPrice.text.clear()
            etTicketStock.text.clear()
            loadTickets()
        } else {
            Toast.makeText(this, "Gagal menambahkan tiket", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteTicket(ticket: Ticket) {
        val result = dbHelper.deleteTicket(ticket.id)

        if (result > 0) {
            Toast.makeText(this, "Tiket berhasil dihapus", Toast.LENGTH_SHORT).show()
            loadTickets()
        } else {
            Toast.makeText(this, "Gagal menghapus tiket", Toast.LENGTH_SHORT).show()
        }
    }
}