import com.example.shipmenttracking.Shipment
import com.example.shipmenttracking.ShippingUpdate

interface UpdateStrategy {
    fun processUpdate(shipment: Shipment, otherInfo: String?)
}

class CreatedStrategy : UpdateStrategy {
    override fun processUpdate(shipment: Shipment, otherInfo: String?) {
        shipment.status = "created"
        shipment.addUpdate(ShippingUpdate("created", "created", System.currentTimeMillis()))
        shipment.notes.add("Shipment created with ID: ${shipment.id}")
        println("created")
    }
}

class ShippedStrategy : UpdateStrategy {
    override fun processUpdate(shipment: Shipment, otherInfo: String?) {
        shipment.status = "shipped"
        shipment.expectedDeliveryDateTimestamp = otherInfo?.toLongOrNull() ?: 0L
        shipment.addUpdate(ShippingUpdate("shipped", "shipped", System.currentTimeMillis()))
        shipment.notes.add("Shipment shipped. Expected delivery timestamp: $otherInfo")
        println("shipped")
    }
}

class LocationStrategy : UpdateStrategy {
    override fun processUpdate(shipment: Shipment, otherInfo: String?) {
        shipment.currentLocation = otherInfo ?: "Unknown location"
        shipment.addUpdate(ShippingUpdate("location", "location", System.currentTimeMillis()))
        shipment.notes.add("Location updated to: $otherInfo")
        println("location")
    }
}

class DeliveredStrategy : UpdateStrategy {
    override fun processUpdate(shipment: Shipment, otherInfo: String?) {
        shipment.status = "delivered"
        shipment.addUpdate(ShippingUpdate("delivered", "delivered", System.currentTimeMillis()))
        shipment.notes.add("Shipment delivered.")
        println("delivered")
    }
}

class NoteAddedStrategy : UpdateStrategy {
    override fun processUpdate(shipment: Shipment, otherInfo: String?) {
        shipment.addNote(otherInfo ?: "")
        shipment.addUpdate(ShippingUpdate("noteadded", "noteadded", System.currentTimeMillis()))
        shipment.notes.add("Note added: $otherInfo")
        println("note added")
    }
}
