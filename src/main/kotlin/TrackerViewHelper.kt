package com.example.shipmenttracking

import androidx.compose.runtime.*

class TrackerViewHelper(private val simulator: TrackingSimulator) : Observer {
    var shipmentIdInput by mutableStateOf("")
    private var currentShipmentId by mutableStateOf("")
    var shipmentNotes by mutableStateOf(listOf<String>())
    var shipmentUpdateHistory by mutableStateOf(listOf<String>())
    var expectedShipmentDeliveryDate by mutableStateOf("")
    var shipmentStatus by mutableStateOf("")

    init {
        simulator.registerObserver(this)
    }

    fun onShipmentIdChange(id: String) {
        shipmentIdInput = id
    }

    fun trackShipment() {
        val shipment = simulator.findShipment(shipmentIdInput)
        if (shipment != null) {
            currentShipmentId = shipmentIdInput
            shipmentNotes = shipment.notes
            shipmentUpdateHistory = shipment.updateHistory.map {
                "Shipment went from ${it.previousStatus} to ${it.newStatus} on ${it.timestamp}"
            }
            expectedShipmentDeliveryDate = shipment.expectedDeliveryDateTimestamp.toString()
            shipmentStatus = shipment.status
        } else {
            currentShipmentId = ""
            shipmentStatus = "Shipment not found"
            shipmentNotes = emptyList()
            shipmentUpdateHistory = emptyList()
            expectedShipmentDeliveryDate = ""
        }
    }

    fun stopTracking() {
        currentShipmentId = ""
        shipmentIdInput = ""
        shipmentNotes = emptyList()
        shipmentUpdateHistory = emptyList()
        expectedShipmentDeliveryDate = ""
        shipmentStatus = ""
    }

    override fun update() {
        // Update the UI based on changes in the simulator
        if (currentShipmentId.isNotEmpty()) {
            trackShipment()
        }
    }
}
