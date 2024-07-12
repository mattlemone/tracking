import com.example.shipmenttracking.TrackerViewHelper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class TrackerViewHelperTest {

    private lateinit var trackerViewHelper: TrackerViewHelper
    private lateinit var simulator: TrackingSimulator
    private lateinit var shipment: Shipment

    @BeforeEach
    fun setUp() {
        simulator = mock(TrackingSimulator::class.java)
        trackerViewHelper = TrackerViewHelper(simulator)
        shipment = mock(Shipment::class.java)
        `when`(shipment.id).thenReturn("123")
    }

    @Test
    fun testTrackShipment_Success() {
        `when`(simulator.findShipment("123")).thenReturn(shipment)

        trackerViewHelper.trackShipment("123")

        verify(shipment).registerObserver(trackerViewHelper)
        assertTrue(trackerViewHelper.trackedShipments.contains(shipment))
    }

    @Test
    fun testTrackShipment_AlreadyTracked() {
        `when`(simulator.findShipment("123")).thenReturn(shipment)
        trackerViewHelper.trackedShipments.add(shipment)

        trackerViewHelper.trackShipment("123")

        verify(shipment, never()).registerObserver(any())
        assertEquals(1, trackerViewHelper.trackedShipments.size)
    }

    @Test
    fun testTrackShipment_NotFound() {
        `when`(simulator.findShipment("123")).thenReturn(null)

        trackerViewHelper.trackShipment("123")

        assertTrue(trackerViewHelper.trackedShipments.isEmpty())
    }

    @Test
    fun testStopTracking() {
        trackerViewHelper.trackedShipments.add(shipment)

        trackerViewHelper.stopTracking("123")

        verify(shipment).removeObserver(trackerViewHelper)
        assertTrue(trackerViewHelper.trackedShipments.isEmpty())
    }

    @Test
    fun testStopTracking_NotTracked() {
        trackerViewHelper.stopTracking("123")

        verify(shipment, never()).removeObserver(any())
        assertTrue(trackerViewHelper.trackedShipments.isEmpty())
    }

    @Test
    fun testUpdate() {
        val updatedShipment = mock(Shipment::class.java)
        `when`(updatedShipment.id).thenReturn("123")
        trackerViewHelper.trackedShipments.add(shipment)

        trackerViewHelper.update(updatedShipment)

        assertEquals(updatedShipment, trackerViewHelper.trackedShipments[0])
    }

    @Test
    fun testUpdate_ShipmentNotTracked() {
        val updatedShipment = mock(Shipment::class.java)
        `when`(updatedShipment.id).thenReturn("456")

        trackerViewHelper.update(updatedShipment)

        assertTrue(trackerViewHelper.trackedShipments.isEmpty())
    }
}