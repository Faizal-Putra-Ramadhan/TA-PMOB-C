package com.faizalputraramadhan.adakonser.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "AdaKonser.db"
        private const val DATABASE_VERSION = 2

        // Table Users
        const val TABLE_USERS = "users"
        const val COLUMN_USER_ID = "user_id"
        const val COLUMN_USER_FULLNAME = "full_name"
        const val COLUMN_USER_EMAIL = "email"
        const val COLUMN_USER_PHONE = "phone"
        const val COLUMN_USER_USERNAME = "username"
        const val COLUMN_USER_PASSWORD = "password"

        // Table Events
        const val TABLE_EVENTS = "events"
        const val COLUMN_EVENT_ID = "event_id"
        const val COLUMN_EVENT_NAME = "event_name"
        const val COLUMN_EVENT_DESC = "event_description"
        const val COLUMN_EVENT_DATE = "event_date"
        const val COLUMN_EVENT_TIME = "event_time"
        const val COLUMN_EVENT_LOCATION = "event_location"
        const val COLUMN_EVENT_ARTIST = "event_artist"
        const val COLUMN_EVENT_POSTER = "event_poster"

        // Table Tickets
        const val TABLE_TICKETS = "tickets"
        const val COLUMN_TICKET_ID = "ticket_id"
        const val COLUMN_TICKET_EVENT_ID = "ticket_event_id"
        const val COLUMN_TICKET_TYPE = "ticket_type"
        const val COLUMN_TICKET_PRICE = "ticket_price"
        const val COLUMN_TICKET_STOCK = "ticket_stock"
        const val COLUMN_TICKET_AVAILABLE = "ticket_available"

        // Table Promos
        const val TABLE_PROMOS = "promos"
        const val COLUMN_PROMO_ID = "promo_id"
        const val COLUMN_PROMO_NAME = "promo_name"
        const val COLUMN_PROMO_DISCOUNT = "promo_discount"
        const val COLUMN_PROMO_START_DATE = "promo_start_date"
        const val COLUMN_PROMO_END_DATE = "promo_end_date"
        const val COLUMN_PROMO_TERMS = "promo_terms"

        // Table Orders
        const val TABLE_ORDERS = "orders"
        const val COLUMN_ORDER_ID = "order_id"
        const val COLUMN_ORDER_EVENT_ID = "order_event_id"
        const val COLUMN_ORDER_TICKET_TYPE = "order_ticket_type"
        const val COLUMN_ORDER_SEAT_NUMBER = "order_seat_number"
        const val COLUMN_ORDER_NAME = "order_name"
        const val COLUMN_ORDER_EMAIL = "order_email"
        const val COLUMN_ORDER_PHONE = "order_phone"
        const val COLUMN_ORDER_PROMO_ID = "order_promo_id"
        const val COLUMN_ORDER_TOTAL = "order_total"
        const val COLUMN_ORDER_DATE = "order_date"
        const val COLUMN_ORDER_QR_CODE = "order_qr_code"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Create Users table
        val createUsersTable = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_USER_FULLNAME TEXT NOT NULL,
                $COLUMN_USER_EMAIL TEXT NOT NULL UNIQUE,
                $COLUMN_USER_PHONE TEXT NOT NULL,
                $COLUMN_USER_USERNAME TEXT NOT NULL UNIQUE,
                $COLUMN_USER_PASSWORD TEXT NOT NULL
            )
        """.trimIndent()

        // Create Events table
        val createEventsTable = """
            CREATE TABLE $TABLE_EVENTS (
                $COLUMN_EVENT_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_EVENT_NAME TEXT NOT NULL,
                $COLUMN_EVENT_DESC TEXT,
                $COLUMN_EVENT_DATE TEXT NOT NULL,
                $COLUMN_EVENT_TIME TEXT NOT NULL,
                $COLUMN_EVENT_LOCATION TEXT NOT NULL,
                $COLUMN_EVENT_ARTIST TEXT,
                $COLUMN_EVENT_POSTER TEXT
            )
        """.trimIndent()

        // Create Tickets table
        val createTicketsTable = """
            CREATE TABLE $TABLE_TICKETS (
                $COLUMN_TICKET_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TICKET_EVENT_ID INTEGER NOT NULL,
                $COLUMN_TICKET_TYPE TEXT NOT NULL,
                $COLUMN_TICKET_PRICE REAL NOT NULL,
                $COLUMN_TICKET_STOCK INTEGER NOT NULL,
                $COLUMN_TICKET_AVAILABLE INTEGER NOT NULL,
                FOREIGN KEY ($COLUMN_TICKET_EVENT_ID) REFERENCES $TABLE_EVENTS($COLUMN_EVENT_ID)
            )
        """.trimIndent()

        // Create Promos table
        val createPromosTable = """
            CREATE TABLE $TABLE_PROMOS (
                $COLUMN_PROMO_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_PROMO_NAME TEXT NOT NULL,
                $COLUMN_PROMO_DISCOUNT INTEGER NOT NULL,
                $COLUMN_PROMO_START_DATE TEXT NOT NULL,
                $COLUMN_PROMO_END_DATE TEXT NOT NULL,
                $COLUMN_PROMO_TERMS TEXT
            )
        """.trimIndent()

        // Create Orders table
        val createOrdersTable = """
            CREATE TABLE $TABLE_ORDERS (
                $COLUMN_ORDER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_ORDER_EVENT_ID INTEGER NOT NULL,
                $COLUMN_ORDER_TICKET_TYPE TEXT NOT NULL,
                $COLUMN_ORDER_SEAT_NUMBER TEXT NOT NULL,
                $COLUMN_ORDER_NAME TEXT NOT NULL,
                $COLUMN_ORDER_EMAIL TEXT NOT NULL,
                $COLUMN_ORDER_PHONE TEXT NOT NULL,
                $COLUMN_ORDER_PROMO_ID INTEGER,
                $COLUMN_ORDER_TOTAL REAL NOT NULL,
                $COLUMN_ORDER_DATE TEXT NOT NULL,
                $COLUMN_ORDER_QR_CODE TEXT,
                FOREIGN KEY ($COLUMN_ORDER_EVENT_ID) REFERENCES $TABLE_EVENTS($COLUMN_EVENT_ID),
                FOREIGN KEY ($COLUMN_ORDER_PROMO_ID) REFERENCES $TABLE_PROMOS($COLUMN_PROMO_ID)
            )
        """.trimIndent()

        db?.execSQL(createUsersTable)
        db?.execSQL(createEventsTable)
        db?.execSQL(createTicketsTable)
        db?.execSQL(createPromosTable)
        db?.execSQL(createOrdersTable)

        // Insert sample data
        insertSampleData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_ORDERS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_TICKETS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_PROMOS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_EVENTS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        onCreate(db)
    }

    private fun insertSampleData(db: SQLiteDatabase?) {
        // Sample Events
        val events = listOf(
            ContentValues().apply {
                put(COLUMN_EVENT_NAME, "Malam Indie Nusantara")
                put(COLUMN_EVENT_DESC, "Bergabunglah dengan kami untuk malam musik live yang tak terlupakan yang menampilkan artis papan atas dan bakat baru.")
                put(COLUMN_EVENT_DATE, "20 Apr")
                put(COLUMN_EVENT_TIME, "19:00 WIB")
                put(COLUMN_EVENT_LOCATION, "Gedung Putih")
                put(COLUMN_EVENT_ARTIST, "Artis 1")
            },
            ContentValues().apply {
                put(COLUMN_EVENT_NAME, "Senandung Kopi & Senja")
                put(COLUMN_EVENT_DESC, "Nikmati sore hari yang santai dengan musik acoustic dan kopi nikmat")
                put(COLUMN_EVENT_DATE, "23 Apr")
                put(COLUMN_EVENT_TIME, "19:00 WIB")
                put(COLUMN_EVENT_LOCATION, "Gedung Putih")
                put(COLUMN_EVENT_ARTIST, "Artis 2")
            },
            ContentValues().apply {
                put(COLUMN_EVENT_NAME, "Ruang Rindu Live")
                put(COLUMN_EVENT_DESC, "Konser penuh emosi dengan lagu-lagu menyentuh hati")
                put(COLUMN_EVENT_DATE, "30 Apr")
                put(COLUMN_EVENT_TIME, "21:00 WIB")
                put(COLUMN_EVENT_LOCATION, "Gedung Putih")
                put(COLUMN_EVENT_ARTIST, "Artis 3")
            },
            ContentValues().apply {
                put(COLUMN_EVENT_NAME, "Panggung Indie Pagi")
                put(COLUMN_EVENT_DESC, "Morning vibes dengan musik indie terbaik")
                put(COLUMN_EVENT_DATE, "30 Apr")
                put(COLUMN_EVENT_TIME, "21:00 WIB")
                put(COLUMN_EVENT_LOCATION, "Gedung Putih")
                put(COLUMN_EVENT_ARTIST, "Artis 4")
            }
        )

        events.forEach { db?.insert(TABLE_EVENTS, null, it) }

        // Sample Tickets
        for (i in 1..4) {
            db?.insert(TABLE_TICKETS, null, ContentValues().apply {
                put(COLUMN_TICKET_EVENT_ID, i)
                put(COLUMN_TICKET_TYPE, "Reguler")
                put(COLUMN_TICKET_PRICE, 100000.0)
                put(COLUMN_TICKET_STOCK, 100)
                put(COLUMN_TICKET_AVAILABLE, 100)
            })
            db?.insert(TABLE_TICKETS, null, ContentValues().apply {
                put(COLUMN_TICKET_EVENT_ID, i)
                put(COLUMN_TICKET_TYPE, "VIP")
                put(COLUMN_TICKET_PRICE, 300000.0)
                put(COLUMN_TICKET_STOCK, 100)
                put(COLUMN_TICKET_AVAILABLE, 100)
            })
            db?.insert(TABLE_TICKETS, null, ContentValues().apply {
                put(COLUMN_TICKET_EVENT_ID, i)
                put(COLUMN_TICKET_TYPE, "Khusus")
                put(COLUMN_TICKET_PRICE, 150000.0)
                put(COLUMN_TICKET_STOCK, 100)
                put(COLUMN_TICKET_AVAILABLE, 100)
            })
        }

        // Sample Promos
        val promos = listOf(
            ContentValues().apply {
                put(COLUMN_PROMO_NAME, "Diskon 20%")
                put(COLUMN_PROMO_DISCOUNT, 20)
                put(COLUMN_PROMO_START_DATE, "2024-07-01")
                put(COLUMN_PROMO_END_DATE, "2024-07-31")
                put(COLUMN_PROMO_TERMS, "Valid until July 31, 2024")
            },
            ContentValues().apply {
                put(COLUMN_PROMO_NAME, "Diskon 30%")
                put(COLUMN_PROMO_DISCOUNT, 30)
                put(COLUMN_PROMO_START_DATE, "2024-07-01")
                put(COLUMN_PROMO_END_DATE, "2024-07-31")
                put(COLUMN_PROMO_TERMS, "Valid until July 31, 2024")
            },
            ContentValues().apply {
                put(COLUMN_PROMO_NAME, "Diskon 10%")
                put(COLUMN_PROMO_DISCOUNT, 10)
                put(COLUMN_PROMO_START_DATE, "2024-07-01")
                put(COLUMN_PROMO_END_DATE, "2024-07-31")
                put(COLUMN_PROMO_TERMS, "Valid until July 31, 2024")
            },
            ContentValues().apply {
                put(COLUMN_PROMO_NAME, "Diskon 60%")
                put(COLUMN_PROMO_DISCOUNT, 60)
                put(COLUMN_PROMO_START_DATE, "2024-07-01")
                put(COLUMN_PROMO_END_DATE, "2024-07-31")
                put(COLUMN_PROMO_TERMS, "Valid until July 31, 2024")
            },
            ContentValues().apply {
                put(COLUMN_PROMO_NAME, "Diskon 50%")
                put(COLUMN_PROMO_DISCOUNT, 50)
                put(COLUMN_PROMO_START_DATE, "2024-07-01")
                put(COLUMN_PROMO_END_DATE, "2024-07-31")
                put(COLUMN_PROMO_TERMS, "Valid until July 31, 2024")
            }
        )

        promos.forEach { db?.insert(TABLE_PROMOS, null, it) }
    }

    // Event CRUD operations
    fun insertEvent(name: String, desc: String, date: String, time: String, location: String, artist: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_EVENT_NAME, name)
            put(COLUMN_EVENT_DESC, desc)
            put(COLUMN_EVENT_DATE, date)
            put(COLUMN_EVENT_TIME, time)
            put(COLUMN_EVENT_LOCATION, location)
            put(COLUMN_EVENT_ARTIST, artist)
        }
        return db.insert(TABLE_EVENTS, null, values)
    }

    fun getAllEvents(): Cursor {
        val db = readableDatabase
        return db.query(TABLE_EVENTS, null, null, null, null, null, "$COLUMN_EVENT_ID DESC")
    }

    fun getEventById(eventId: Int): Cursor {
        val db = readableDatabase
        return db.query(TABLE_EVENTS, null, "$COLUMN_EVENT_ID = ?", arrayOf(eventId.toString()), null, null, null)
    }

    fun updateEvent(eventId: Int, name: String, desc: String, date: String, time: String, location: String, artist: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_EVENT_NAME, name)
            put(COLUMN_EVENT_DESC, desc)
            put(COLUMN_EVENT_DATE, date)
            put(COLUMN_EVENT_TIME, time)
            put(COLUMN_EVENT_LOCATION, location)
            put(COLUMN_EVENT_ARTIST, artist)
        }
        return db.update(TABLE_EVENTS, values, "$COLUMN_EVENT_ID = ?", arrayOf(eventId.toString()))
    }

    fun deleteEvent(eventId: Int): Int {
        val db = writableDatabase
        return db.delete(TABLE_EVENTS, "$COLUMN_EVENT_ID = ?", arrayOf(eventId.toString()))
    }

    // Ticket CRUD operations
    fun insertTicket(eventId: Int, type: String, price: Double, stock: Int): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TICKET_EVENT_ID, eventId)
            put(COLUMN_TICKET_TYPE, type)
            put(COLUMN_TICKET_PRICE, price)
            put(COLUMN_TICKET_STOCK, stock)
            put(COLUMN_TICKET_AVAILABLE, stock)
        }
        return db.insert(TABLE_TICKETS, null, values)
    }

    fun getTicketsByEventId(eventId: Int): Cursor {
        val db = readableDatabase
        return db.query(TABLE_TICKETS, null, "$COLUMN_TICKET_EVENT_ID = ?", arrayOf(eventId.toString()), null, null, null)
    }

    fun updateTicket(ticketId: Int, type: String, price: Double, stock: Int, available: Int): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TICKET_TYPE, type)
            put(COLUMN_TICKET_PRICE, price)
            put(COLUMN_TICKET_STOCK, stock)
            put(COLUMN_TICKET_AVAILABLE, available)
        }
        return db.update(TABLE_TICKETS, values, "$COLUMN_TICKET_ID = ?", arrayOf(ticketId.toString()))
    }

    fun updateTicketAvailability(ticketId: Int, available: Int): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TICKET_AVAILABLE, available)
        }
        return db.update(TABLE_TICKETS, values, "$COLUMN_TICKET_ID = ?", arrayOf(ticketId.toString()))
    }

    fun deleteTicket(ticketId: Int): Int {
        val db = writableDatabase
        return db.delete(TABLE_TICKETS, "$COLUMN_TICKET_ID = ?", arrayOf(ticketId.toString()))
    }

    // Promo CRUD operations
    fun insertPromo(name: String, discount: Int, startDate: String, endDate: String, terms: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_PROMO_NAME, name)
            put(COLUMN_PROMO_DISCOUNT, discount)
            put(COLUMN_PROMO_START_DATE, startDate)
            put(COLUMN_PROMO_END_DATE, endDate)
            put(COLUMN_PROMO_TERMS, terms)
        }
        return db.insert(TABLE_PROMOS, null, values)
    }

    fun getAllPromos(): Cursor {
        val db = readableDatabase
        return db.query(TABLE_PROMOS, null, null, null, null, null, "$COLUMN_PROMO_ID DESC")
    }

    fun getPromoById(promoId: Int): Cursor {
        val db = readableDatabase
        return db.query(TABLE_PROMOS, null, "$COLUMN_PROMO_ID = ?", arrayOf(promoId.toString()), null, null, null)
    }

    fun updatePromo(promoId: Int, name: String, discount: Int, startDate: String, endDate: String, terms: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_PROMO_NAME, name)
            put(COLUMN_PROMO_DISCOUNT, discount)
            put(COLUMN_PROMO_START_DATE, startDate)
            put(COLUMN_PROMO_END_DATE, endDate)
            put(COLUMN_PROMO_TERMS, terms)
        }
        return db.update(TABLE_PROMOS, values, "$COLUMN_PROMO_ID = ?", arrayOf(promoId.toString()))
    }

    fun deletePromo(promoId: Int): Int {
        val db = writableDatabase
        return db.delete(TABLE_PROMOS, "$COLUMN_PROMO_ID = ?", arrayOf(promoId.toString()))
    }

    // Order operations
    fun insertOrder(eventId: Int, ticketType: String, seatNumber: String, name: String, email: String, phone: String, promoId: Int?, total: Double, qrCode: String): Long {
        val db = writableDatabase
        val currentDate = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(java.util.Date())
        val values = ContentValues().apply {
            put(COLUMN_ORDER_EVENT_ID, eventId)
            put(COLUMN_ORDER_TICKET_TYPE, ticketType)
            put(COLUMN_ORDER_SEAT_NUMBER, seatNumber)
            put(COLUMN_ORDER_NAME, name)
            put(COLUMN_ORDER_EMAIL, email)
            put(COLUMN_ORDER_PHONE, phone)
            put(COLUMN_ORDER_PROMO_ID, promoId)
            put(COLUMN_ORDER_TOTAL, total)
            put(COLUMN_ORDER_DATE, currentDate)
            put(COLUMN_ORDER_QR_CODE, qrCode)
        }
        return db.insert(TABLE_ORDERS, null, values)
    }

    fun getAllOrders(): Cursor {
        val db = readableDatabase
        return db.rawQuery("""
            SELECT o.*, e.$COLUMN_EVENT_NAME, e.$COLUMN_EVENT_DATE, e.$COLUMN_EVENT_TIME, e.$COLUMN_EVENT_LOCATION
            FROM $TABLE_ORDERS o
            INNER JOIN $TABLE_EVENTS e ON o.$COLUMN_ORDER_EVENT_ID = e.$COLUMN_EVENT_ID
            ORDER BY o.$COLUMN_ORDER_ID DESC
        """, null)
    }

    fun getOrderById(orderId: Int): Cursor {
        val db = readableDatabase
        return db.rawQuery("""
            SELECT o.*, e.$COLUMN_EVENT_NAME, e.$COLUMN_EVENT_DATE, e.$COLUMN_EVENT_TIME, e.$COLUMN_EVENT_LOCATION, e.$COLUMN_EVENT_ARTIST
            FROM $TABLE_ORDERS o
            INNER JOIN $TABLE_EVENTS e ON o.$COLUMN_ORDER_EVENT_ID = e.$COLUMN_EVENT_ID
            WHERE o.$COLUMN_ORDER_ID = ?
        """, arrayOf(orderId.toString()))
    }

    // Statistics for Admin Dashboard
    fun getTotalEvents(): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM $TABLE_EVENTS", null)
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()
        return count
    }

    fun getTotalTicketsSold(): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM $TABLE_ORDERS", null)
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()
        return count
    }

    fun getTotalRevenue(): Double {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT SUM($COLUMN_ORDER_TOTAL) FROM $TABLE_ORDERS", null)
        cursor.moveToFirst()
        val revenue = if (!cursor.isNull(0)) cursor.getDouble(0) else 0.0
        cursor.close()
        return revenue
    }

    // User Management Functions
    fun registerUser(fullName: String, email: String, phone: String, username: String, password: String): Long {
        val db = writableDatabase

        // Check if username already exists
        val cursor = db.query(
            TABLE_USERS,
            arrayOf(COLUMN_USER_ID),
            "$COLUMN_USER_USERNAME = ?",
            arrayOf(username),
            null, null, null
        )

        if (cursor.count > 0) {
            cursor.close()
            return -1 // Username already exists
        }
        cursor.close()

        val values = ContentValues().apply {
            put(COLUMN_USER_FULLNAME, fullName)
            put(COLUMN_USER_EMAIL, email)
            put(COLUMN_USER_PHONE, phone)
            put(COLUMN_USER_USERNAME, username)
            put(COLUMN_USER_PASSWORD, password)
        }

        return db.insert(TABLE_USERS, null, values)
    }

    fun loginUser(username: String, password: String): User? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_USERS,
            null,
            "$COLUMN_USER_USERNAME = ? AND $COLUMN_USER_PASSWORD = ?",
            arrayOf(username, password),
            null, null, null
        )

        var user: User? = null
        if (cursor.moveToFirst()) {
            user = User(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)),
                fullName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_FULLNAME)),
                email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_EMAIL)),
                phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_PHONE)),
                username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_USERNAME))
            )
        }
        cursor.close()
        return user
    }

    fun getUserByUsername(username: String): User? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_USERS,
            null,
            "$COLUMN_USER_USERNAME = ?",
            arrayOf(username),
            null, null, null
        )

        var user: User? = null
        if (cursor.moveToFirst()) {
            user = User(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)),
                fullName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_FULLNAME)),
                email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_EMAIL)),
                phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_PHONE)),
                username = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_USERNAME))
            )
        }
        cursor.close()
        return user
    }
}

// User data class
data class User(
    val id: Int,
    val fullName: String,
    val email: String,
    val phone: String,
    val username: String
)