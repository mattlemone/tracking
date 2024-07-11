import com.example.shipmenttracking.ShippingUpdate

interface UpdateStrategy {
    fun processUpdate(shipment: Shipment, otherInfo: List<String>)
}

class CreatedStrategy : UpdateStrategy {
    override fun processUpdate(shipment: Shipment, otherInfo: List<String>) {
        shipment.status = "created"
        shipment.addUpdate(ShippingUpdate("", shipment.status, System.currentTimeMillis()))
//        println("created $shipment")
    }
}

class ShippedStrategy : UpdateStrategy {
    override fun processUpdate(shipment: Shipment, otherInfo: List<String>) {
        var old = shipment.status
        shipment.status = "shipped"
        shipment.expectedDeliveryDateTimestamp = otherInfo[1].toLongOrNull() ?: 0L
        shipment.addUpdate(ShippingUpdate(old, shipment.status, otherInfo[0].toLongOrNull() ?: 0L))
//        println("shipped $shipment")
    }
}

class LocationStrategy : UpdateStrategy {
    override fun processUpdate(shipment: Shipment, otherInfo: List<String>) {
        var old = shipment.status
        shipment.currentLocation = otherInfo[1]
        shipment.addUpdate(ShippingUpdate(old, shipment.currentLocation, otherInfo[0].toLongOrNull() ?: 0L))
//        println("location $shipment")
    }
}

class DeliveredStrategy : UpdateStrategy {
    override fun processUpdate(shipment: Shipment, otherInfo: List<String>) {
        shipment.status = "delivered"
        shipment.addUpdate(ShippingUpdate(shipment.currentLocation, shipment.status, otherInfo[0].toLongOrNull() ?: 0L))
//        println("delivered $shipment")
    }
}

class NoteAddedStrategy : UpdateStrategy {
    override fun processUpdate(shipment: Shipment, otherInfo: List<String>) {
        shipment.addNote(otherInfo[1])
    }
}

class LostStrategy : UpdateStrategy {
    override fun processUpdate(shipment: Shipment, otherInfo: List<String>) {
        var old = shipment.status
        shipment.status = "Lost"
        shipment.addUpdate(ShippingUpdate(old, shipment.status, otherInfo[0].toLongOrNull() ?: 0L))
    }
}

class CanceledStrategy : UpdateStrategy {
    override fun processUpdate(shipment: Shipment, otherInfo: List<String>) {
        var old = shipment.status
        shipment.status = "Canceled"
        shipment.addUpdate(ShippingUpdate(old, shipment.status, otherInfo[0].toLongOrNull() ?: 0L))
    }
}
