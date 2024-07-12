import androidx.compose.runtime.snapshots.SnapshotStateList
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import com.example.shipmenttracking.ShippingUpdate

class ShipmentTest {

    private lateinit var shipment: Shipment

    @BeforeEach
    fun setUp() {
        shipment = Shipment(
            id = "123",
            status = "In Transit",
            notes = SnapshotStateList(),
            updateHistory = SnapshotStateList(),
            expectedDeliveryDateTimestamp = LocalDateTime.now().plusDays(3),
            currentLocation = "Warehouse A"
        )
    }

    @Test
    fun testAddNote() {
        shipment.addNote("Test note")
        assertEquals(1, shipment.notes.size)
        assertEquals("Test note", shipment.notes[0])
    }

    @Test
    fun testAddUpdate() {
        val update = ShippingUpdate("Update 1", "new status", LocalDateTime.now())
        shipment.addUpdate(update)
        assertEquals(1, shipment.updateHistory.size)
        assertEquals(update, shipment.updateHistory[0])
    }

    @Test
    fun testApplyStrategy() {
        val testStrategy = object : UpdateStrategy {
            override fun processUpdate(shipment: Shipment, otherInfo: List<String>) {
                shipment.status = "Updated"
            }
        }
        shipment.strategy = testStrategy
        shipment.applyStrategy(listOf("Info 1", "Info 2"))
        assertEquals("Updated", shipment.status)
    }

    @Test
    fun testApplyStrategyWithNullStrategy() {
        shipment.strategy = null
        shipment.applyStrategy(listOf("Info 1", "Info 2"))
        // This test just verifies that no exception is thrown
        assertTrue(true)
    }
}