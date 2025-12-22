package com.faizalputraramadhan.adakonser

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.faizalputraramadhan.adakonser.database.DatabaseHelper
import com.faizalputraramadhan.adakonser.models.Event
import com.faizalputraramadhan.adakonser.models.Promo
import com.faizalputraramadhan.adakonser.models.Ticket
import com.google.android.material.button.MaterialButton
import java.util.*

class BuyTicketActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var event: Event
    private var selectedPromo: Promo? = null
    private var selectedTicket: Ticket? = null
    private var selectedSeat: String = ""

    private lateinit var tvEventName: TextView
    private lateinit var tvEventDetails: TextView
    private lateinit var tvEventLocation: TextView
    private lateinit var spinnerPromo: Spinner
    private lateinit var gridSeats: GridLayout
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPhone: EditText
    private lateinit var tvTicketType: TextView
    private lateinit var tvSeat: TextView
    private lateinit var tvPromo: TextView
    private lateinit var tvTotal: TextView
    private lateinit var btnOrder: MaterialButton

    private val tickets = mutableListOf<Ticket>()
    private val promos = mutableListOf<Promo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy_ticket)

        dbHelper = DatabaseHelper(this)

        val eventId = intent.getIntExtra("EVENT_ID", -1)
        if (eventId == -1) {
            finish()
            return
        }

        initViews()
        loadEvent(eventId)
        loadTickets(eventId)
        loadPromos()
        setupListeners()
    }

    private fun initViews() {
        tvEventName = findViewById(R.id.tvEventName)
        tvEventDetails = findViewById(R.id.tvEventDetails)
        tvEventLocation = findViewById(R.id.tvEventLocation)
        spinnerPromo = findViewById(R.id.spinnerPromo)
        gridSeats = findViewById(R.id.gridSeats)
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etPhone = findViewById(R.id.etPhone)
        tvTicketType = findViewById(R.id.tvTicketType)
        tvSeat = findViewById(R.id.tvSeat)
        tvPromo = findViewById(R.id.tvPromo)
        tvTotal = findViewById(R.id.tvTotal)
        btnOrder = findViewById(R.id.btnOrder)
    }

    private fun loadEvent(eventId: Int) {
        val cursor = dbHelper.getEventById(eventId)
        if (cursor.moveToFirst()) {
            event = Event(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EVENT_ID)),
                name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EVENT_NAME)),
                description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EVENT_DESC)),
                date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EVENT_DATE)),
                time = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EVENT_TIME)),
                location = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EVENT_LOCATION)),
                artist = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EVENT_ARTIST))
            )

            tvEventName.text = event.name
            tvEventDetails.text = "${event.date} · ${event.time}"
            tvEventLocation.text = event.location
        }
        cursor.close()
    }

    private fun loadTickets(eventId: Int) {
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

        // Setup ticket type selection (you can use RadioGroup or custom view)
        if (tickets.isNotEmpty()) {
            selectedTicket = tickets[0]
            tvTicketType.text = selectedTicket?.type
            setupSeats()
        }
    }

    private fun loadPromos() {
        promos.clear()
        promos.add(Promo(0, "Pilih Promo", 0, "", "", ""))

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

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, promos.map { it.name })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerPromo.adapter = adapter
    }

    private fun setupSeats() {
        gridSeats.removeAllViews()

        // Create seat grid (e.g., 4 rows x 4 columns = 16 seats)
        for (i in 1..16) {
            val seatButton = Button(this)
            val seatNumber = String.format("%02d", i)
            seatButton.text = seatNumber
            seatButton.layoutParams = GridLayout.LayoutParams().apply {
                width = 0
                height = GridLayout.LayoutParams.WRAP_CONTENT
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                setMargins(8, 8, 8, 8)
            }

            seatButton.setOnClickListener {
                selectedSeat = seatNumber
                tvSeat.text = seatNumber
                updateTotal()
            }

            gridSeats.addView(seatButton)
        }
    }

    private fun setupListeners() {
        spinnerPromo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedPromo = if (position > 0) promos[position] else null
                tvPromo.text = if (selectedPromo != null) "Diskon ${selectedPromo!!.discount}%" else "Tidak Ada"
                updateTotal()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        btnOrder.setOnClickListener {
            placeOrder()
        }
    }

    private fun updateTotal() {
        if (selectedTicket == null) return

        var total = selectedTicket!!.price
        if (selectedPromo != null) {
            total -= (total * selectedPromo!!.discount / 100)
        }

        tvTotal.text = "IDR ${String.format("%,.0f", total)}"
    }

    private fun placeOrder() {
        val name = etName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val phone = etPhone.text.toString().trim()

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Mohon lengkapi semua data", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedSeat.isEmpty()) {
            Toast.makeText(this, "Mohon pilih kursi", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedTicket == null) {
            Toast.makeText(this, "Tiket tidak ditemukan", Toast.LENGTH_SHORT).show()
            return
        }

        var total = selectedTicket!!.price
        if (selectedPromo != null) {
            total -= (total * selectedPromo!!.discount / 100)
        }

        // Generate QR Code (simplified - in real app use QR library)
        val qrCode = UUID.randomUUID().toString()

        val orderId = dbHelper.insertOrder(
            event.id,
            selectedTicket!!.type,
            selectedSeat,
            name,
            email,
            phone,
            selectedPromo?.id,
            total,
            qrCode
        )

        if (orderId > 0) {
            // Update ticket availability
            val newAvailable = selectedTicket!!.available - 1
            dbHelper.updateTicketAvailability(selectedTicket!!.id, newAvailable)

            Toast.makeText(this, "Pemesanan berhasil!", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Pemesanan gagal", Toast.LENGTH_SHORT).show()
        }
    }
}