import androidx.compose.runtime.mutableStateListOf
import com.example.shipmenttracking.TrackerViewHelper
import java.time.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TrackerViewHelperTests {

    @Test
    fun `trackShipment should add new shipment to tracked list`() {
        val simulator = TrackingSimulator()
        val trackerViewHelper = TrackerViewHelper(simulator)
        val shipment = Shipment("123", "In Transit", mutableStateListOf(), mutableStateListOf(), LocalDateTime.now(), "")
        simulator.addShipment(shipment)

        trackerViewHelper.trackShipment("123")

        assertTrue(trackerViewHelper.trackedShipments.contains(shipment))
    }

    @Test
    fun `trackShipment should not add duplicate shipment`() {
        val simulator = TrackingSimulator()
        val trackerViewHelper = TrackerViewHelper(simulator)
        val shipment = Shipment("123", "In Transit", mutableStateListOf(), mutableStateListOf(), LocalDateTime.now(), "")
        simulator.addShipment(shipment)

        trackerViewHelper.trackShipment("123")
        trackerViewHelper.trackShipment("123")

        assertEquals(1, trackerViewHelper.trackedShipments.size)
    }

    @Test
    fun `trackShipment should not add non-existent shipment`() {
        val simulator = TrackingSimulator()
        val trackerViewHelper = TrackerViewHelper(simulator)

        trackerViewHelper.trackShipment("456")

        assertTrue(trackerViewHelper.trackedShipments.isEmpty())
    }

    @Test
    fun `stopTracking should remove shipment from tracked list`() {
        val simulator = TrackingSimulator()
        val trackerViewHelper = TrackerViewHelper(simulator)
        val shipment = Shipment("123", "In Transit", mutableStateListOf(), mutableStateListOf(), LocalDateTime.now(), "")
        simulator.addShipment(shipment)

        trackerViewHelper.trackShipment("123")
        trackerViewHelper.stopTracking("123")

        assertFalse(trackerViewHelper.trackedShipments.contains(shipment))
    }

    @Test
    fun `update should update existing shipment in tracked list`() {
        val simulator = TrackingSimulator()
        val trackerViewHelper = TrackerViewHelper(simulator)
        val initialShipment = Shipment("123", "In Transit", mutableStateListOf(), mutableStateListOf(), LocalDateTime.now(), "")
        simulator.addShipment(initialShipment)

        trackerViewHelper.trackShipment("123")

        val updatedShipment = initialShipment.copy(status = "Delivered")
        trackerViewHelper.update(updatedShipment)

        assertEquals("Delivered", trackerViewHelper.trackedShipments[0].status)
    }
}

// A simple test implementation of TrackingSimulator
class TrackingSimulator : TrackingSimulator {
    private val shipments = mutableMapOf<String, Shipment>()

    fun addShipment(shipment: Shipment) {
        shipments[shipment.id] = shipment
    }

    override fun findShipment(id: String): Shipment? {
        return shipments[id]
    }
}