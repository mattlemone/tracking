package com.example.shipmenttracking

import UpdateStrategy

data class Shipment(
    val id: String,
    var status: String,
    var notes: MutableList<String>,
    var updateHistory: MutableList<ShippingUpdate>,
    var expectedDeliveryDateTimestamp: Long,
    var currentLocation: String,
    var strategy: UpdateStrategy? = null
) {
    fun addNote(note: String) {
        notes.add(note)
    }

    fun addUpdate(update: ShippingUpdate) {
        updateHistory.add(update)
    }

    fun applyStrategy(otherInfo: String?) {
        strategy?.processUpdate(this, otherInfo)
    }
}
