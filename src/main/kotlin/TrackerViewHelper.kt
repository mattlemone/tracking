package com.example.shipmenttracking

import androidx.compose.runtime.*

class TrackerViewHelper(private val simulator: TrackingSimulator) : Observer {
    var shipmentId by mutableStateOf("")
    var shipmentNotes by mutableStateOf(listOf<String>())
    var shipmentUpdateHistory by mutableStateOf(listOf<String>())
    var expectedShipmentDeliveryDate by mutableStateOf("")
    var shipmentStatus by mutableStateOf("")

    init {
        simulator.registerObserver(this)
    }

    fun onShipmentIdChange(id: String) {
        shipmentId = id
    }

    fun trackShipment() {
        val shipment = simulator.findShipment(shipmentId)
        if (shipment != null) {
            shipmentNotes = shipment.notes
            shipmentUpdateHistory = shipment.updateHistory.map {
                "Shipment went from ${it.previousStatus} to ${it.newStatus} on ${it.timestamp}"
            }
            expectedShipmentDeliveryDate = shipment.expectedDeliveryDateTimestamp.toString()
            shipmentStatus = shipment.status
        } else {
            shipmentStatus = "Shipment not found"
            shipmentNotes = emptyList()
            shipmentUpdateHistory = emptyList()
            expectedShipmentDeliveryDate = ""
        }
    }

    fun stopTracking() {
        shipmentId = ""
        shipmentNotes = emptyList()
        shipmentUpdateHistory = emptyList()
        expectedShipmentDeliveryDate = ""
        shipmentStatus = ""
    }

    override fun update() {
        // Update the UI based on changes in the simulator
        if (shipmentId.isNotEmpty()) {
            trackShipment()
        }
    }
}
