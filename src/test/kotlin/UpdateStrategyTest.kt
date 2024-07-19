import androidx.compose.runtime.mutableStateListOf
import junit.framework.TestCase.assertEquals
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.test.Test

class UpdateStrategyTest {

    @Test
    fun `CreatedStrategy should update shipment status to created`() {
        val shipment = StandardShipment(
            id = "123",
            status = "",
            notes = mutableStateListOf(),
            updateHistory = mutableStateListOf(),
            expectedDeliveryDateTimestamp = LocalDateTime.now().plusDays(1),
            currentLocation = ""
        )
        val strategy = CreatedStrategy()

        strategy.processUpdate(shipment, listOf("1625074800000"))

        assertEquals("created", shipment.status)
    }

    @Test
    fun `ShippedStrategy should update shipment status to shipped`() {
        val shipment = StandardShipment(
            id = "123",
            status = "Created",
            notes = mutableStateListOf(),
            updateHistory = mutableStateListOf(),
            expectedDeliveryDateTimestamp = LocalDateTime.now().plusDays(1),
            currentLocation = ""
        )
        val strategy = ShippedStrategy()

        val currentTime = System.currentTimeMillis()
        val estimatedDeliveryTime = currentTime + 86400000 // Add 24 hours in milliseconds

        strategy.processUpdate(shipment, listOf(currentTime.toString(), estimatedDeliveryTime.toString()))

        assertEquals("shipped", shipment.status)

        val expectedDeliveryDateTime = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(estimatedDeliveryTime),
            ZoneId.systemDefault()
        )
        assertEquals(expectedDeliveryDateTime, shipment.expectedDeliveryDateTimestamp)
    }

    @Test
    fun `NoteAddedStrategy should add note to shipment`() {
        val shipment = StandardShipment(
            id = "123",
            status = "",
            notes = mutableStateListOf(),
            updateHistory = mutableStateListOf(),
            expectedDeliveryDateTimestamp = LocalDateTime.now().plusDays(1),
            currentLocation = ""
        )
        val strategy = NoteAddedStrategy()

        strategy.processUpdate(shipment, listOf("", "New note"))

        assertEquals(1, shipment.notes.size)
        assertEquals("New note", shipment.notes[0])
    }

    @Test
    fun `LostStrategy should update shipment status to Lost`() {
        val shipment = StandardShipment(
            id = "123",
            status = "",
            notes = mutableStateListOf(),
            updateHistory = mutableStateListOf(),
            expectedDeliveryDateTimestamp = LocalDateTime.now().plusDays(1),
            currentLocation = ""
        )
        val strategy = LostStrategy()

        strategy.processUpdate(shipment, listOf("1625074800000"))

        assertEquals("Lost", shipment.status)
    }

    @Test
    fun `CanceledStrategy should update shipment status to Canceled`() {
        val shipment = StandardShipment(
            id = "123",
            status = "",
            notes = mutableStateListOf(),
            updateHistory = mutableStateListOf(),
            expectedDeliveryDateTimestamp = LocalDateTime.now().plusDays(1),
            currentLocation = ""
        )
        val strategy = CanceledStrategy()

        strategy.processUpdate(shipment, listOf("1625074800000"))

        assertEquals("Canceled", shipment.status)
    }

    @Test
    fun `LocationStrategy should update current location and add shipping update`() {
        val shipment = StandardShipment(
            id = "123",
            status = "In Transit",
            notes = mutableStateListOf(),
            updateHistory = mutableStateListOf(),
            expectedDeliveryDateTimestamp = LocalDateTime.now().plusDays(1),
            currentLocation = "Old Location"
        )
        val strategy = LocationStrategy()
        val timestamp = System.currentTimeMillis().toString()
        val newLocation = "New Location"

        strategy.processUpdate(shipment, listOf(timestamp, newLocation))

        assertEquals(newLocation, shipment.currentLocation)
        assertEquals(1, shipment.updateHistory.size)
        with(shipment.updateHistory[0]) {
            assertEquals("In Transit", this.previousStatus)
            assertEquals(newLocation, this.newStatus)
            assertEquals(
                LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp.toLong()), ZoneId.systemDefault()),
                this.timestamp
            )
        }
    }
}

class DeliveredStrategyTest {
    @Test
    fun `DeliveredStrategy should update status to delivered and add shipping update`() {
        val shipment = StandardShipment(
            id = "123",
            status = "In Transit",
            notes = mutableStateListOf(),
            updateHistory = mutableStateListOf(),
            expectedDeliveryDateTimestamp = LocalDateTime.now().plusDays(1),
            currentLocation = "Current Location"
        )
        val strategy = DeliveredStrategy()
        val timestamp = System.currentTimeMillis().toString()

        strategy.processUpdate(shipment, listOf(timestamp))

        assertEquals("delivered", shipment.status)
        assertEquals(1, shipment.updateHistory.size)
        with(shipment.updateHistory[0]) {
            assertEquals("Current Location", this.previousStatus)
            assertEquals("delivered", this.newStatus)
            assertEquals(
                LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp.toLong()), ZoneId.systemDefault()),
                this.timestamp
            )
        }
    }
}
