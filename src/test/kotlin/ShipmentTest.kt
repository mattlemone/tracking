import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.shipmenttracking.ShippingUpdate
import junit.framework.TestCase.assertEquals
import java.time.LocalDateTime
import kotlin.test.Test

class StandardShipmentTest {
    @Test
    fun `should create standard shipment`() {
        val shipment = StandardShipment(
            id = "123",
            status = "In Transit",
            notes = SnapshotStateList(),
            updateHistory = SnapshotStateList(),
            expectedDeliveryDateTimestamp = LocalDateTime.now(),
            currentLocation = "Warehouse A"
        )
        assertEquals("123", shipment.id)
    }
}
class ExpressShipmentTest {
    @Test
    fun `should create express shipment`() {
        val shipment = ExpressShipment(
            id = "124",
            status = "In Transit",
            notes = SnapshotStateList(),
            updateHistory = SnapshotStateList(),
            expectedDeliveryDateTimestamp = LocalDateTime.now().plusDays(4),
            currentLocation = "Warehouse B"
        )
        assertEquals("124", shipment.id)
    }

    @Test
    fun `should mark express shipment as abnormal if delivery date is too late`() {
        val shipment = ExpressShipment(
            id = "125",
            status = "In Transit",
            notes = SnapshotStateList(),
            updateHistory = SnapshotStateList(),
            expectedDeliveryDateTimestamp = LocalDateTime.now().plusDays(5),
            currentLocation = "Warehouse C"
        )
        shipment.validateExpectedDeliveryDate()
        assertEquals("Abnormal", shipment.status)
        assertEquals("Expected delivery date is not the day after creation for an overnight shipment.", shipment.notes[0])
    }
}
class OvernightShipmentTest {
    @Test
    fun `should create overnight shipment`() {
        val shipment = OvernightShipment(
            id = "126",
            status = "In Transit",
            notes = SnapshotStateList(),
            updateHistory = SnapshotStateList(),
            expectedDeliveryDateTimestamp = LocalDateTime.now().plusDays(1),
            currentLocation = "Warehouse D"
        )
        assertEquals("126", shipment.id)
    }

    @Test
    fun `should mark overnight shipment as abnormal if delivery date is not the next day`() {
        val shipment = OvernightShipment(
            id = "127",
            status = "In Transit",
            notes = SnapshotStateList(),
            updateHistory = SnapshotStateList(),
            expectedDeliveryDateTimestamp = LocalDateTime.now().plusDays(2),
            currentLocation = "Warehouse E"
        )
        shipment.validateExpectedDeliveryDate()
        assertEquals("Abnormal", shipment.status)
        assertEquals("Expected delivery date is not the day after creation for an overnight shipment.", shipment.notes[0])
    }
}
class BulkShipmentTest {
    @Test
    fun `should create bulk shipment`() {
        val shipment = BulkShipment(
            id = "128",
            status = "In Transit",
            notes = SnapshotStateList(),
            updateHistory = SnapshotStateList(),
            expectedDeliveryDateTimestamp = LocalDateTime.now().plusDays(4),
            currentLocation = "Warehouse F"
        )
        assertEquals("128", shipment.id)
    }

    @Test
    fun `should mark bulk shipment as abnormal if delivery date is too soon`() {
        val shipment = BulkShipment(
            id = "129",
            status = "In Transit",
            notes = SnapshotStateList(),
            updateHistory = SnapshotStateList(),
            expectedDeliveryDateTimestamp = LocalDateTime.now().plusDays(2),
            currentLocation = "Warehouse G"
        )
        shipment.validateExpectedDeliveryDate()
        assertEquals("Abnormal", shipment.status)
        assertEquals("Expected delivery date is sooner than 3 days after creation for a bulk shipment.", shipment.notes[0])
    }
}
