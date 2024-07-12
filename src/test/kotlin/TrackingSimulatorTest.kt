import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import java.time.LocalDateTime

class TrackingSimulatorTest {

    private lateinit var simulator: TrackingSimulator

    @BeforeEach
    fun setUp() {
        simulator = TrackingSimulator()
    }

    @Test
    fun testFindShipment_ExistingShipment() {
        val shipment = Shipment("123", "", mutableStateListOf(), mutableStateListOf(), LocalDateTime.now(), "", null)
        simulator.javaClass.getDeclaredMethod("addShipment", Shipment::class.java).apply {
            isAccessible = true
            invoke(simulator, shipment)
        }

        val result = simulator.findShipment("123")

        assertNotNull(result)
        assertEquals("123", result.id)
    }

    @Test
    fun testFindShipment_NonExistingShipment() {
        val result = simulator.findShipment("456")

        assertNull(result)
    }

    @Test
    fun testProcessUpdate_NewShipment() {
        val method = simulator.javaClass.getDeclaredMethod("processUpdate", String::class.java)
        method.isAccessible = true

        method.invoke(simulator, "created,789,New York,2023-07-11")

        val shipment = simulator.findShipment("789")
        assertNotNull(shipment)
        assertEquals("789", shipment.id)
    }

    @Test
    fun testProcessUpdate_ExistingShipment() {
        val shipment = Shipment("123", "", mutableStateListOf(), mutableStateListOf(), LocalDateTime.now(), "", null)
        simulator.javaClass.getDeclaredMethod("addShipment", Shipment::class.java).apply {
            isAccessible = true
            invoke(simulator, shipment)
        }

        val method = simulator.javaClass.getDeclaredMethod("processUpdate", String::class.java)
        method.isAccessible = true

        method.invoke(simulator, "location,123,Los Angeles")

        val updatedShipment = simulator.findShipment("123")
        assertNotNull(updatedShipment)
        assertEquals("Los Angeles", updatedShipment.currentLocation)
    }

    @Test
    fun testUpdateShipments() = runBlocking {
        val fileContent = listOf(
            "created,123,New York,2023-07-11",
            "shipped,123,Los Angeles",
            "delivered,123,Customer's address"
        )

        simulator.updateShipments(fileContent)

        val shipment = simulator.findShipment("123")
        assertNotNull(shipment)
        assertEquals("Delivered", shipment.status)
        assertEquals("Customer's address", shipment.currentLocation)
    }
}