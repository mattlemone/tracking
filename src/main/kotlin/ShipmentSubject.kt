package com.example.shipmenttracking

interface ShipmentSubject {
    fun registerObserver(shipmentObserver: ShipmentObserver)
    fun removeObserver(shipmentObserver: ShipmentObserver)
    fun notifyObservers()
}
