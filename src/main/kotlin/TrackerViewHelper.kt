package com.example.shipmenttracking

import Shipment
import ShipmentObserver
import TrackingSimulator
import androidx.compose.runtime.*

class TrackerViewHelper(private val simulator: TrackingSimulator) : ShipmentObserver {
    var shipmentIdInput by mutableStateOf("")
    var trackedShipments = mutableStateListOf<Shipment>()

    fun onShipmentIdChange(id: String) {
        shipmentIdInput = id
    }

    fun trackShipment() {
        println("Tracking shipment: $shipmentIdInput")
        val shipment = simulator.findShipment(shipmentIdInput)
        if (shipment != null) {
            println("Shipment found: $shipment")
            shipment.registerObserver(this)
            if (!trackedShipments.contains(shipment)) {
                trackedShipments.add(shipment)
            }
        } else {
            println("Shipment not found: $shipmentIdInput")
            // Handle shipment not found case
        }
        shipmentIdInput = ""
    }

    fun stopTracking(id: String) {
        val shipment = trackedShipments.find { it.id == id }
        if (shipment != null) {
            shipment.removeObserver(this)
            trackedShipments.remove(shipment)
        }
    }

    override fun update(shipment: Shipment) {
        // Force recomposition of the specific shipment
        val index = trackedShipments.indexOfFirst { it.id == shipment.id }
        if (index != -1) {
            trackedShipments[index] = shipment
        }
    }
}
