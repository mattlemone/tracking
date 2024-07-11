import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.shipmenttracking.ShipmentSubject
import com.example.shipmenttracking.ShippingUpdate

data class Shipment(
    val id: String,
    var status: String,
    var notes: SnapshotStateList<String>,
    var updateHistory: SnapshotStateList<ShippingUpdate>,
    var expectedDeliveryDateTimestamp: Long,
    var currentLocation: String,
    var strategy: UpdateStrategy? = null
) : ShipmentSubject {
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

    fun applyStrategy(otherInfo: List <String>) {
        strategy?.processUpdate(this, otherInfo)
        notifyObservers()
    }
}