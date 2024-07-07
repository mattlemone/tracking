data class Shipment(
    val id: String,
    var status: String,
    var notes: MutableList<String>,
    var updateHistory: MutableList<ShippingUpdate>,
    var expectedDeliveryDateTimestamp: Long,
    var currentLocation: String
) {
    fun addNote(note: String) {
        notes.add(note)
    }

    fun addUpdate(update: ShippingUpdate) {
        updateHistory.add(update)
    }
}