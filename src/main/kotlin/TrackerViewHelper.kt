package com.example.shipmenttracking

import Shipment
import ShipmentObserver
import TrackingSimulator
import androidx.compose.runtime.*

class TrackerViewHelper(private val simulator: TrackingSimulator) : ShipmentObserver {
    var trackedShipments = mutableStateListOf<Shipment>()

    fun trackShipment(id: String) {
        println("Attempting to track shipment: $id")
        if (trackedShipments.any { it.id == id }) {
            println("Shipment $id is already being tracked")
            return
        }

        val shipment = simulator.findShipment(id)
        if (shipment != null) {
            println("Shipment found: $shipment")
            shipment.registerObserver(this)
            trackedShipments.add(shipment)
            println("Started tracking shipment: $id")
        } else {
            println("Shipment not found: $id")
        }
    }

    fun stopTracking(id: String) {
        val shipment = trackedShipments.find { it.id == id }
        if (shipment != null) {
            shipment.removeObserver(this)
            trackedShipments.remove(shipment)
            println("Stopped tracking shipment: $id")
        }
    }

    override fun update(shipment: Shipment) {
        println("Received update for shipment: ${shipment.id}")
        val index = trackedShipments.indexOfFirst { it.id == shipment.id }
        if (index != -1) {
            trackedShipments[index] = shipment
            println("Updated tracked shipment: ${shipment.id}")
        }
    }
}