import androidx.compose.runtime.mutableStateListOf
import com.example.shipmenttracking.TrackerViewHelper
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
        simulator.setShipmentToReturn(Shipment("123", "In Transit", mutableStateListOf(), mutableStateListOf(), LocalDateTime.now(), ""))

        viewHelper.trackShipment("123")

        assertEquals(1, viewHelper.trackedShipments.size)
        assertEquals("123", viewHelper.trackedShipments[0].id)
    }

    @Test
    fun `trackShipment should not add duplicate shipment`() {
        simulator.setShipmentToReturn(Shipment("123", "In Transit", mutableStateListOf(), mutableStateListOf(), LocalDateTime.now(), ""))

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
        simulator.setShipmentToReturn(Shipment("123", "In Transit", mutableStateListOf(), mutableStateListOf(), LocalDateTime.now(), ""))

        viewHelper.trackShipment("123")
        viewHelper.stopTracking("123")

        assertTrue(viewHelper.trackedShipments.isEmpty())
    }

    @Test
    fun `update should update existing shipment in tracked list`() {
        val initialShipment = Shipment("123", "In Transit", mutableStateListOf(), mutableStateListOf(), LocalDateTime.now(), "")
        simulator.setShipmentToReturn(initialShipment)
        viewHelper.trackShipment("123")

        val updatedShipment = initialShipment.copy(status = "Delivered")
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