package com.example.shipmenttracking

data class ShippingUpdate(
    val previousStatus: String,
    val newStatus: String,
    val timestamp: Long
)
