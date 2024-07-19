import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.shipmenttracking.ShippingUpdate
import java.time.LocalDateTime

object ShipmentFactory {
    fun createShipment(
        type: ShipmentType,
        id: String,
        status: String,
        notes: SnapshotStateList<String>,
        updateHistory: SnapshotStateList<ShippingUpdate>,
        expectedDeliveryDateTimestamp: LocalDateTime,
        currentLocation: String,
        strategy: UpdateStrategy? = null
    ): Shipment {
        return when (type) {
            ShipmentType.STANDARD -> StandardShipment(id, status, notes, updateHistory, expectedDeliveryDateTimestamp, currentLocation, strategy)
            ShipmentType.EXPRESS -> ExpressShipment(id, status, notes, updateHistory, expectedDeliveryDateTimestamp, currentLocation, strategy)
            ShipmentType.OVERNIGHT -> OvernightShipment(id, status, notes, updateHistory, expectedDeliveryDateTimestamp, currentLocation, strategy)
            ShipmentType.BULK -> BulkShipment(id, status, notes, updateHistory, expectedDeliveryDateTimestamp, currentLocation, strategy)
        }.apply {
            validateExpectedDeliveryDate()
        }
    }
}

enum class ShipmentType {
    STANDARD, EXPRESS, OVERNIGHT, BULK
}
