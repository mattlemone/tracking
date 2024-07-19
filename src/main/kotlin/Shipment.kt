import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.shipmenttracking.ShipmentSubject
import com.example.shipmenttracking.ShippingUpdate
import java.time.LocalDateTime

abstract class Shipment(
    val id: String,
    var status: String,
    var notes: SnapshotStateList<String>,
    var updateHistory: SnapshotStateList<ShippingUpdate>,
    var expectedDeliveryDateTimestamp: LocalDateTime,
    var currentLocation: String,
    var strategy: UpdateStrategy? = null
) : ShipmentSubject, Observable {
    private val observers = mutableListOf<ShipmentObserver>()

    override fun registerObserver(observer: ShipmentObserver) {
        observers.add(observer)
    }

    override fun removeObserver(observer: ShipmentObserver) {
        observers.remove(observer)
    }

    override fun notifyObservers() {
        observers.forEach { it.update(this) }
    }

    fun addNote(note: String) {
        notes.add(note)
        notifyObservers()
    }

    fun addUpdate(update: ShippingUpdate) {
        updateHistory.add(update)
        notifyObservers()
    }

    abstract fun validateExpectedDeliveryDate()
}

class StandardShipment(
    id: String,
    status: String,
    notes: SnapshotStateList<String>,
    updateHistory: SnapshotStateList<ShippingUpdate>,
    expectedDeliveryDateTimestamp: LocalDateTime,
    currentLocation: String,
    strategy: UpdateStrategy? = null
) : Shipment(id, status, notes, updateHistory, expectedDeliveryDateTimestamp, currentLocation, strategy) {
    override fun validateExpectedDeliveryDate() {
        // No special conditions
    }
}

class ExpressShipment(
    id: String,
    status: String,
    notes: SnapshotStateList<String>,
    updateHistory: SnapshotStateList<ShippingUpdate>,
    expectedDeliveryDateTimestamp: LocalDateTime,
    currentLocation: String,
    strategy: UpdateStrategy? = null
) : Shipment(id, status, notes, updateHistory, expectedDeliveryDateTimestamp, currentLocation, strategy) {
    override fun validateExpectedDeliveryDate() {
        if (expectedDeliveryDateTimestamp.isAfter(LocalDateTime.now().plusDays(3))) {
            status = "Abnormal"
            addNote("Expected delivery date is not the day after creation for an overnight shipment.")
        }
    }
}

class OvernightShipment(
    id: String,
    status: String,
    notes: SnapshotStateList<String>,
    updateHistory: SnapshotStateList<ShippingUpdate>,
    expectedDeliveryDateTimestamp: LocalDateTime,
    currentLocation: String,
    strategy: UpdateStrategy? = null
) : Shipment(id, status, notes, updateHistory, expectedDeliveryDateTimestamp, currentLocation, strategy) {
    override fun validateExpectedDeliveryDate() {
        if (expectedDeliveryDateTimestamp.isAfter(LocalDateTime.now().plusDays(1))) {
            status = "Abnormal"
            addNote("Expected delivery date is not the day after creation for an overnight shipment.")
        }
    }
}

class BulkShipment(
    id: String,
    status: String,
    notes: SnapshotStateList<String>,
    updateHistory: SnapshotStateList<ShippingUpdate>,
    expectedDeliveryDateTimestamp: LocalDateTime,
    currentLocation: String,
    strategy: UpdateStrategy? = null
) : Shipment(id, status, notes, updateHistory, expectedDeliveryDateTimestamp, currentLocation, strategy) {
    override fun validateExpectedDeliveryDate() {
        if (expectedDeliveryDateTimestamp.isBefore(LocalDateTime.now().plusDays(3))) {
            status = "Abnormal"
            addNote("Expected delivery date is sooner than 3 days after creation for a bulk shipment.")
        }
    }
}
