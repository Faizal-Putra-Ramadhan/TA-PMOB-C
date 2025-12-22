package com.faizalputraramadhan.adakonser

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.faizalputraramadhan.adakonser.database.DatabaseHelper
import com.google.android.material.button.MaterialButton
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

class TicketDetailActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper

    private lateinit var tvEventName: TextView
    private lateinit var tvArtist: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvLocation: TextView
    private lateinit var tvSeat: TextView
    private lateinit var tvHolder: TextView
    private lateinit var tvTicketType: TextView
    private lateinit var ivQrCode: ImageView
    private lateinit var btnShare: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ticket_detail)

        dbHelper = DatabaseHelper(this)

        val orderId = intent.getIntExtra("ORDER_ID", -1)
        if (orderId == -1) {
            finish()
            return
        }

        initViews()
        loadTicketDetail(orderId)
    }

    private fun initViews() {
        tvEventName = findViewById(R.id.tvEventName)
        tvArtist = findViewById(R.id.tvArtist)
        tvDate = findViewById(R.id.tvDate)
        tvLocation = findViewById(R.id.tvLocation)
        tvSeat = findViewById(R.id.tvSeat)
        tvHolder = findViewById(R.id.tvHolder)
        tvTicketType = findViewById(R.id.tvTicketType)
        ivQrCode = findViewById(R.id.ivQrCode)
        btnShare = findViewById(R.id.btnShare)
    }

    private fun loadTicketDetail(orderId: Int) {
        val cursor = dbHelper.getOrderById(orderId)

        if (cursor.moveToFirst()) {
            val eventName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EVENT_NAME))
            val artist = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EVENT_ARTIST))
            val date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EVENT_DATE))
            val location = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EVENT_LOCATION))
            val seat = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_SEAT_NUMBER))
            val holder = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_NAME))
            val ticketType = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_TICKET_TYPE))
            val qrCode = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ORDER_QR_CODE))

            tvEventName.text = eventName
            tvArtist.text = "Artis: $artist"
            tvDate.text = date
            tvLocation.text = location
            tvSeat.text = seat
            tvHolder.text = holder
            tvTicketType.text = ticketType

            // Generate QR Code
            generateQRCode(qrCode)
        }
        cursor.close()

        btnShare.setOnClickListener {
            // Implement share functionality
        }
    }

    private fun generateQRCode(text: String) {
        try {
            val writer = QRCodeWriter()
            val bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 512, 512)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
                }
            }

            ivQrCode.setImageBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}