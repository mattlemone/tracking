package com.example.shipmenttracking

import androidx.compose.runtime.*

data class TrackedShipment(
    val id: String,
    val notes: MutableList<String>,
    val updateHistory: MutableList<String>,
    val expectedDeliveryDate: String,
    val status: String
) {
    fun addNote(note: String) {
        notes.add(note)
    }

    fun addUpdate(update: ShippingUpdate) {
        updateHistory.add(update.toString())
    }
}

class TrackerViewHelper(private val simulator: TrackingSimulator) : Observer {
    var shipmentIdInput by mutableStateOf("")
    var trackedShipments by mutableStateOf(listOf<TrackedShipment>())

    init {
        simulator.registerObserver(this)
    }

    fun onShipmentIdChange(id: String) {
        shipmentIdInput = id
    }

    fun trackShipment() {
        val shipment = simulator.findShipment(shipmentIdInput)
        if (shipment != null) {
            val newShipment = TrackedShipment(
                id = shipmentIdInput,
                notes = shipment.notes.toMutableList(),  // Convert to MutableList
                updateHistory = shipment.updateHistory.map {
                    "Shipment went from ${it.previousStatus} to ${it.newStatus} on ${it.timestamp}"
                }.toMutableList(),  // Convert to MutableList
                expectedDeliveryDate = shipment.expectedDeliveryDateTimestamp.toString(),
                status = shipment.status
            )
            trackedShipments = trackedShipments + newShipment
        } else {
            trackedShipments = trackedShipments + TrackedShipment(
                id = shipmentIdInput,
                notes = mutableListOf(),  // Use MutableList
                updateHistory = mutableListOf(),  // Use MutableList
                expectedDeliveryDate = "",
                status = "Shipment not found"
            )
        }
        shipmentIdInput = ""
    }

    fun stopTracking(id: String) {
        trackedShipments = trackedShipments.filter { it.id != id }
    }

    override fun update() {
        trackedShipments = trackedShipments.map { shipment ->
            val updatedShipment = simulator.findShipment(shipment.id)
            if (updatedShipment != null) {
                shipment.copy(
                    notes = updatedShipment.notes.toMutableList(),  // Convert to MutableList
                    updateHistory = updatedShipment.updateHistory.map {
                        "Shipment went from ${it.previousStatus} to ${it.newStatus} on ${it.timestamp}"
                    }.toMutableList(),  // Convert to MutableList
                    expectedDeliveryDate = updatedShipment.expectedDeliveryDateTimestamp.toString(),
                    status = updatedShipment.status
                )
            } else {
                shipment.copy(status = "Shipment not found")
            }
        }
    }
}
