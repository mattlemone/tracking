import com.example.shipmenttracking.ShippingUpdate

interface UpdateStrategy {
    fun processUpdate(shipment: Shipment, otherInfo: String?)
}

class CreatedStrategy : UpdateStrategy {
    override fun processUpdate(shipment: Shipment, otherInfo: String?) {
        shipment.status = "created"
        shipment.addUpdate(ShippingUpdate("", shipment.status, System.currentTimeMillis()))
//        println("created $shipment")
    }
}

class ShippedStrategy : UpdateStrategy {
    override fun processUpdate(shipment: Shipment, otherInfo: String?) {
        var old = shipment.status
        shipment.status = "shipped"
        shipment.expectedDeliveryDateTimestamp = otherInfo?.toLongOrNull() ?: 0L
        shipment.addUpdate(ShippingUpdate(old, shipment.status, System.currentTimeMillis()))
//        println("shipped $shipment")
    }
}

class LocationStrategy : UpdateStrategy {
    override fun processUpdate(shipment: Shipment, otherInfo: String?) {
        var old = shipment.status
        shipment.currentLocation = otherInfo ?: "Unknown location"
        shipment.addUpdate(ShippingUpdate(old, shipment.currentLocation, System.currentTimeMillis()))
//        println("location $shipment")
    }
}

class DeliveredStrategy : UpdateStrategy {
    override fun processUpdate(shipment: Shipment, otherInfo: String?) {
        shipment.status = "delivered"
        shipment.addUpdate(ShippingUpdate(shipment.currentLocation, shipment.status, System.currentTimeMillis()))
        shipment.notes.add("Shipment delivered.")
//        println("delivered $shipment")
    }
}

class NoteAddedStrategy : UpdateStrategy {
    override fun processUpdate(shipment: Shipment, otherInfo: String?) {
        shipment.addNote(otherInfo ?: "")
        shipment.addUpdate(ShippingUpdate("noteadded", "noteadded", System.currentTimeMillis()))
        shipment.notes.add("$otherInfo")
//        println("note added $shipment")
    }
}
