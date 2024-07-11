package com.example.shipmenttracking

import TrackingSimulator
import androidx.compose.runtime.*

data class TrackedShipment(
    val id: String,
    val notes: MutableList<String>,
    val updateHistory: MutableList<String>,
    val expectedDeliveryDate: String,
    val status: String
)

class TrackerViewHelper(private val simulator: TrackingSimulator) : ShipmentObserver {
    var shipmentIdInput by mutableStateOf("")
    var trackedShipments = mutableStateListOf<TrackedShipment>()

    init {
        simulator.registerObserver(this)
    }

    fun onShipmentIdChange(id: String) {
        shipmentIdInput = id
    }

    fun trackShipment() {
        val shipment = simulator.findShipment(shipmentIdInput)
        val newShipment = if (shipment != null) {
            TrackedShipment(
                id = shipmentIdInput,
                notes = shipment.notes.toMutableList(),
                updateHistory = shipment.updateHistory.map {
                    "Shipment went from ${it.previousStatus} to ${it.newStatus} on ${it.timestamp}"
                }.toMutableList(),
                expectedDeliveryDate = shipment.expectedDeliveryDateTimestamp.toString(),
                status = shipment.status
            )
        } else {
            TrackedShipment(
                id = shipmentIdInput,
                notes = mutableListOf(),
                updateHistory = mutableListOf(),
                expectedDeliveryDate = "",
                status = "Shipment not found"
            )
        }
        trackedShipments.add(newShipment)
        shipmentIdInput = ""
    }

    fun stopTracking(id: String) {
        val index = trackedShipments.indexOfFirst { it.id == id }
        if (index != -1) {
            trackedShipments.removeAt(index)
        }
    }

    override fun update() {
        for (i in trackedShipments.indices) {
            val shipment = trackedShipments[i]
            val updatedShipment = simulator.findShipment(shipment.id)
            if (updatedShipment != null) {
                trackedShipments[i] = shipment.copy(
                    notes = updatedShipment.notes.toMutableList(),
                    updateHistory = updatedShipment.updateHistory.map {
                        "Shipment went from ${it.previousStatus} to ${it.newStatus} on ${it.timestamp}"
                    }.toMutableList(),
                    expectedDeliveryDate = updatedShipment.expectedDeliveryDateTimestamp.toString(),
                    status = updatedShipment.status
                )
            } else {
                trackedShipments[i] = shipment.copy(status = "Shipment not found")
            }
        }
    }
}
