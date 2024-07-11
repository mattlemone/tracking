import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.shipmenttracking.ShipmentSubject
import com.example.shipmenttracking.ShippingUpdate

data class Shipment(
    val id: String,
    var status: String,
    var notes: SnapshotStateList<String> = mutableStateListOf(),
    var updateHistory: SnapshotStateList<ShippingUpdate> = mutableStateListOf(),
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

    fun applyStrategy(otherInfo: String?) {
        strategy?.processUpdate(this, otherInfo)
        notifyObservers()
    }
}
