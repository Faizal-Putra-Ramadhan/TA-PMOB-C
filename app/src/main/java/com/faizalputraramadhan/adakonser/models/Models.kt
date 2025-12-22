package com.faizalputraramadhan.adakonser.models

data class Event(
    val id: Int,
    val name: String,
    val description: String,
    val date: String,
    val time: String,
    val location: String,
    val artist: String,
    val poster: String? = null
)

data class Ticket(
    val id: Int,
    val eventId: Int,
    val type: String,
    val price: Double,
    val stock: Int,
    val available: Int
)

data class Promo(
    val id: Int,
    val name: String,
    val discount: Int,
    val startDate: String,
    val endDate: String,
    val terms: String
)

data class Order(
    val id: Int,
    val eventId: Int,
    val eventName: String,
    val eventDate: String,
    val eventTime: String,
    val eventLocation: String,
    val ticketType: String,
    val seatNumber: String,
    val name: String,
    val email: String,
    val phone: String,
    val promoId: Int?,
    val total: Double,
    val orderDate: String,
    val qrCode: String
)
