import com.example.shipmenttracking.ShippingUpdate
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

interface UpdateStrategy {
    fun processUpdate(shipment: Shipment, otherInfo: List<String>)
}

class CreatedStrategy : UpdateStrategy {
    override fun processUpdate(shipment: Shipment, otherInfo: List<String>) {
        shipment.status = "created"
        val date = timestampToDate(otherInfo[0])
        shipment.addUpdate(ShippingUpdate("", shipment.status, date))
//        println("created $shipment")
    }
}

class ShippedStrategy : UpdateStrategy {
    override fun processUpdate(shipment: Shipment, otherInfo: List<String>) {
        var old = shipment.status
        shipment.status = "shipped"
        var shippedDate = timestampToDate(otherInfo[1])
        shipment.expectedDeliveryDateTimestamp = shippedDate

        val date = timestampToDate(otherInfo[0])
        shipment.addUpdate(ShippingUpdate(old, shipment.status, date))
//        println("shipped $shipment")
    }
}

class LocationStrategy : UpdateStrategy {
    override fun processUpdate(shipment: Shipment, otherInfo: List<String>) {
        var old = shipment.status
        shipment.currentLocation = otherInfo[1]
        val date = timestampToDate(otherInfo[0])
        shipment.addUpdate(ShippingUpdate(old, shipment.currentLocation, date))
//        println("location $shipment")
    }
}

class DeliveredStrategy : UpdateStrategy {
    override fun processUpdate(shipment: Shipment, otherInfo: List<String>) {
        shipment.status = "delivered"
        val date = timestampToDate(otherInfo[0])
        shipment.addUpdate(ShippingUpdate(shipment.currentLocation, shipment.status, date))
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
        val date = timestampToDate(otherInfo[0])
        shipment.addUpdate(ShippingUpdate(old, shipment.status, date))
    }
}

class CanceledStrategy : UpdateStrategy {
    override fun processUpdate(shipment: Shipment, otherInfo: List<String>) {
        var old = shipment.status
        shipment.status = "Canceled"
        val date = timestampToDate(otherInfo[0])
        shipment.addUpdate(ShippingUpdate(old, shipment.status, date))
    }
}

fun timestampToDate(timestampStr: String): LocalDateTime {
    val timestamp = timestampStr.toLong()
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault())
}
