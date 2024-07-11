package com.example.shipmenttracking

import java.time.LocalDateTime

data class ShippingUpdate(
    val previousStatus: String,
    val newStatus: String,
    val timestamp: LocalDateTime
)
