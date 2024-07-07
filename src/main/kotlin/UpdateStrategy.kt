package com.example.shipmenttracking

interface UpdateStrategy {
    fun processUpdate(shipment: Shipment, otherInfo: String?)
}

class CreatedStrategy : UpdateStrategy {
    override fun processUpdate(shipment: Shipment, otherInfo: String?) {
        shipment.status = "created"
    }
}

class ShippedStrategy : UpdateStrategy {
    override fun processUpdate(shipment: Shipment, otherInfo: String?) {
        shipment.status = "shipped"
        shipment.expectedDeliveryDateTimestamp = otherInfo?.toLongOrNull() ?: 0L
    }
}

class LocationStrategy : UpdateStrategy {
    override fun processUpdate(shipment: Shipment, otherInfo: String?) {
        shipment.currentLocation = otherInfo ?: "Unknown location"
    }
}

class DeliveredStrategy : UpdateStrategy {
    override fun processUpdate(shipment: Shipment, otherInfo: String?) {
        shipment.status = "delivered"
    }
}
