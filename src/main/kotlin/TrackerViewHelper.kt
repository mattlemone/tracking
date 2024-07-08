package com.example.shipmenttracking

import androidx.compose.runtime.*

data class TrackedShipment(
    val id: String,
    val notes: List<String>,
    val updateHistory: List<String>,
    val expectedDeliveryDate: String,
    val status: String
)

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
                notes = shipment.notes,
                updateHistory = shipment.updateHistory.map {
                    "Shipment went from ${it.previousStatus} to ${it.newStatus} on ${it.timestamp}"
                },
                expectedDeliveryDate = shipment.expectedDeliveryDateTimestamp.toString(),
                status = shipment.status
            )
            trackedShipments = trackedShipments + newShipment
        } else {
            trackedShipments = trackedShipments + TrackedShipment(
                id = shipmentIdInput,
                notes = emptyList(),
                updateHistory = emptyList(),
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
        // Update the UI based on changes in the simulator
        trackedShipments = trackedShipments.map { shipment ->
            val updatedShipment = simulator.findShipment(shipment.id)
            if (updatedShipment != null) {
                shipment.copy(
                    notes = updatedShipment.notes,
                    updateHistory = updatedShipment.updateHistory.map {
                        "Shipment went from ${it.previousStatus} to ${it.newStatus} on ${it.timestamp}"
                    },
                    expectedDeliveryDate = updatedShipment.expectedDeliveryDateTimestamp.toString(),
                    status = updatedShipment.status
                )
            } else {
                shipment.copy(status = "Shipment not found")
            }
        }
    }
}
