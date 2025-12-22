package com.faizalputraramadhan.adakonser

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.faizalputraramadhan.adakonser.database.DatabaseHelper
import com.google.android.material.button.MaterialButton

class AdminAddEventActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private var eventId: Int = -1

    private lateinit var etEventName: EditText
    private lateinit var etEventDesc: EditText
    private lateinit var etEventDate: EditText
    private lateinit var etEventTime: EditText
    private lateinit var etEventLocation: EditText
    private lateinit var etEventArtist: EditText
    private lateinit var btnSave: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_add_event)

        dbHelper = DatabaseHelper(this)
        eventId = intent.getIntExtra("EVENT_ID", -1)

        initViews()

        if (eventId != -1) {
            loadEventData()
        }

        setupListeners()
    }

    private fun initViews() {
        etEventName = findViewById(R.id.etEventName)
        etEventDesc = findViewById(R.id.etEventDesc)
        etEventDate = findViewById(R.id.etEventDate)
        etEventTime = findViewById(R.id.etEventTime)
        etEventLocation = findViewById(R.id.etEventLocation)
        etEventArtist = findViewById(R.id.etEventArtist)
        btnSave = findViewById(R.id.btnSave)
    }

    private fun loadEventData() {
        val cursor = dbHelper.getEventById(eventId)

        if (cursor.moveToFirst()) {
            etEventName.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EVENT_NAME)))
            etEventDesc.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EVENT_DESC)))
            etEventDate.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EVENT_DATE)))
            etEventTime.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EVENT_TIME)))
            etEventLocation.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EVENT_LOCATION)))
            etEventArtist.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EVENT_ARTIST)))

            btnSave.text = "Update Event"
        }
        cursor.close()
    }

    private fun setupListeners() {
        btnSave.setOnClickListener {
            saveEvent()
        }
    }

    private fun saveEvent() {
        val name = etEventName.text.toString().trim()
        val desc = etEventDesc.text.toString().trim()
        val date = etEventDate.text.toString().trim()
        val time = etEventTime.text.toString().trim()
        val location = etEventLocation.text.toString().trim()
        val artist = etEventArtist.text.toString().trim()

        if (name.isEmpty() || date.isEmpty() || time.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "Mohon lengkapi data yang wajib diisi", Toast.LENGTH_SHORT).show()
            return
        }

        val result = if (eventId == -1) {
            // Add new event
            dbHelper.insertEvent(name, desc, date, time, location, artist)
        } else {
            // Update existing event
            dbHelper.updateEvent(eventId, name, desc, date, time, location, artist).toLong()
        }

        if (result > 0) {
            Toast.makeText(this, "Event berhasil disimpan", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Gagal menyimpan event", Toast.LENGTH_SHORT).show()
        }
    }
}