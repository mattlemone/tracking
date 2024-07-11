package com.example.shipmenttracking

import ShipmentObserver

interface ShipmentSubject {
    fun registerObserver(shipmentObserver: ShipmentObserver)
    fun removeObserver(shipmentObserver: ShipmentObserver)
    fun notifyObservers()
}
