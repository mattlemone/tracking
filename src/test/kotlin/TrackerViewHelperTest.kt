import androidx.compose.runtime.mutableStateListOf
import com.example.shipmenttracking.*
import java.time.LocalDateTime
import kotlin.test.*

class TrackerViewHelperTests {

    private lateinit var simulator: TrackingSimulator
    private lateinit var viewHelper: TrackerViewHelper

    @BeforeTest
    fun setup() {
        simulator = TrackingSimulator()
        viewHelper = TrackerViewHelper(simulator)
    }

    @Test
    fun `trackShipment should add new shipment to tracked list`() {
        val shipment = StandardShipment(
            id = "123",
            status = "In Transit",
            notes = mutableStateListOf(),
            updateHistory = mutableStateListOf(),
            expectedDeliveryDateTimestamp = LocalDateTime.now().plusDays(1),
            currentLocation = "Warehouse A"
        )
        simulator.setShipmentToReturn(shipment)

        viewHelper.trackShipment("123")

        assertEquals(1, viewHelper.trackedShipments.size)
        assertEquals("123", viewHelper.trackedShipments[0].id)
    }

    @Test
    fun `trackShipment should not add duplicate shipment`() {
        val shipment = StandardShipment(
            id = "123",
            status = "In Transit",
            notes = mutableStateListOf(),
            updateHistory = mutableStateListOf(),
            expectedDeliveryDateTimestamp = LocalDateTime.now().plusDays(1),
            currentLocation = "Warehouse A"
        )
        simulator.setShipmentToReturn(shipment)

        viewHelper.trackShipment("123")
        viewHelper.trackShipment("123")

        assertEquals(1, viewHelper.trackedShipments.size)
    }

    @Test
    fun `trackShipment should not add non-existent shipment`() {
        simulator.setShipmentToReturn(null)

        viewHelper.trackShipment("456")

        assertTrue(viewHelper.trackedShipments.isEmpty())
    }

    @Test
    fun `stopTracking should remove shipment from tracked list`() {
        val shipment = StandardShipment(
            id = "123",
            status = "In Transit",
            notes = mutableStateListOf(),
            updateHistory = mutableStateListOf(),
            expectedDeliveryDateTimestamp = LocalDateTime.now().plusDays(1),
            currentLocation = "Warehouse A"
        )
        simulator.setShipmentToReturn(shipment)

        viewHelper.trackShipment("123")
        viewHelper.stopTracking("123")

        assertTrue(viewHelper.trackedShipments.isEmpty())
    }

    @Test
    fun `update should update existing shipment in tracked list`() {
        val initialShipment = StandardShipment(
            id = "123",
            status = "In Transit",
            notes = mutableStateListOf(),
            updateHistory = mutableStateListOf(),
            expectedDeliveryDateTimestamp = LocalDateTime.now().plusDays(1),
            currentLocation = "Warehouse A"
        )
        simulator.setShipmentToReturn(initialShipment)
        viewHelper.trackShipment("123")

        // Manually create a new shipment with updated status
        val updatedShipment = StandardShipment(
            id = "123",
            status = "Delivered",
            notes = initialShipment.notes,
            updateHistory = initialShipment.updateHistory,
            expectedDeliveryDateTimestamp = initialShipment.expectedDeliveryDateTimestamp,
            currentLocation = initialShipment.currentLocation
        )
        viewHelper.update(updatedShipment)

        assertEquals("Delivered", viewHelper.trackedShipments[0].status)
    }
}

// Test implementation of TrackingSimulator
class TrackingSimulator {
    private var shipmentToReturn: Shipment? = null

    fun setShipmentToReturn(shipment: Shipment?) {
        shipmentToReturn = shipment
    }

    fun findShipment(id: String): Shipment? {
        return shipmentToReturn
    }
}
