import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class TrackerViewHelper {
    var shipmentId by mutableStateOf("")
    var shipmentNotes by mutableStateOf(emptyList<String>())
    var shipmentUpdateHistory by mutableStateOf(emptyList<String>())
    var expectedShipmentDeliveryDate by mutableStateOf("")
    var shipmentStatus by mutableStateOf("")

    fun trackShipment(id: String) {
        val shipment = TrackingSimulator.findShipment(id)
        if (shipment != null) {
            shipmentId = shipment.id
            shipmentNotes = shipment.notes
            shipmentUpdateHistory = shipment.updateHistory.map {
                "Shipment went from ${it.previousStatus} to ${it.newStatus} on ${it.timestamp}"
            }
            expectedShipmentDeliveryDate = shipment.expectedDeliveryDateTimestamp.toString()
            shipmentStatus = shipment.status
        } else {
            shipmentId = "Shipment not found"
        }
    }

    fun stopTracking() {
        shipmentId = ""
        shipmentNotes = emptyList()
        shipmentUpdateHistory = emptyList()
        expectedShipmentDeliveryDate = ""
        shipmentStatus = ""
    }
}
