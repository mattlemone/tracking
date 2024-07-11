package com.example.shipmenttracking

import Shipment
import ShipmentObserver
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

    fun onShipmentIdChange(id: String) {
        shipmentIdInput = id
    }

    fun trackShipment() {
        println("Tracking shipment: $shipmentIdInput")
        val shipment = simulator.findShipment(shipmentIdInput)
        if (shipment != null) {
            println("Shipment found: $shipment")
            shipment.registerObserver(this)
            val newTrackedShipment = TrackedShipment(
                id = shipmentIdInput,
                notes = shipment.notes.toMutableList(),
                updateHistory = shipment.updateHistory.map {
                    "Shipment went from ${it.previousStatus} to ${it.newStatus} on ${it.timestamp}"
                }.toMutableList(),
                expectedDeliveryDate = shipment.expectedDeliveryDateTimestamp.toString(),
                status = shipment.status
            )
            trackedShipments.add(newTrackedShipment)
        } else {
            // Handle shipment not found case
        }
        shipmentIdInput = ""
    }

    fun stopTracking(id: String) {
        val index = trackedShipments.indexOfFirst { it.id == id }
        if (index != -1) {
            simulator.findShipment(id)?.removeObserver(this)
            trackedShipments.removeAt(index)
        }
    }

    override fun update(shipment: Shipment) {
        val index = trackedShipments.indexOfFirst { it.id == shipment.id }
        if (index != -1) {
            trackedShipments[index] = trackedShipments[index].copy(
                notes = shipment.notes.toMutableList(),
                updateHistory = shipment.updateHistory.map {
                    "Shipment went from ${it.previousStatus} to ${it.newStatus} on ${it.timestamp}"
                }.toMutableList(),
                expectedDeliveryDate = shipment.expectedDeliveryDateTimestamp.toString(),
                status = shipment.status
            )
        }
    }
}

