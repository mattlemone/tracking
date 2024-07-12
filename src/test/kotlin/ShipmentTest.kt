import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.shipmenttracking.ShippingUpdate
import junit.framework.TestCase.assertEquals
import java.time.LocalDateTime
import kotlin.test.Test

class MinimalShipmentTest {
    @Test
    fun `should create shipment`() {
        val shipment = Shipment(
            id = "123",
            status = "In Transit",
            notes = SnapshotStateList(),
            updateHistory = SnapshotStateList(),
            expectedDeliveryDateTimestamp = LocalDateTime.now(),
            currentLocation = "Warehouse A"
        )
        assertEquals("123", shipment.id)
    }

    @Test
    fun `should add note`() {
        val shipment = Shipment(
            id = "123",
            status = "In Transit",
            notes = SnapshotStateList(),
            updateHistory = SnapshotStateList(),
            expectedDeliveryDateTimestamp = LocalDateTime.now(),
            currentLocation = "Warehouse A"
        )
        shipment.addNote("Test note")
        assertEquals("Test note", shipment.notes[0])
    }

    @Test
    fun `should add update`() {
        val shipment = Shipment(
            id = "123",
            status = "In Transit",
            notes = SnapshotStateList(),
            updateHistory = SnapshotStateList(),
            expectedDeliveryDateTimestamp = LocalDateTime.now(),
            currentLocation = "Warehouse A"
        )
        val update = ShippingUpdate("Old Status", "New Status", LocalDateTime.now())
        shipment.addUpdate(update)
        assertEquals(update, shipment.updateHistory[0])
    }
}