import androidx.compose.runtime.mutableStateListOf
import com.example.shipmenttracking.*
import kotlinx.coroutines.delay
import java.io.File
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class TrackingSimulator {
    private val shipments = mutableListOf<Shipment>()

    fun findShipment(id: String): Shipment? {
        println("Current shipments: ${shipments.map { it.id }}")
        return shipments.find { it.id == id }
    }

    private fun addShipment(shipment: Shipment) {
        if (findShipment(shipment.id) == null) {
            shipments.add(shipment)
        }
    }

    private fun processUpdate(line: String) {
        val parts = line.split(',')
        if (parts.size < 3) return

        val updateType = parts[0]
        val shipmentId = parts[1]
        val timestamp = parts.getOrNull(2)?.toLongOrNull() ?: 0

        var shipment = findShipment(shipmentId)
        if (shipment == null) {
            shipment = Shipment(shipmentId, "", mutableStateListOf(), mutableStateListOf(), 0, "", null)
            addShipment(shipment)
        }

        val strategy = when (updateType) {
            "created" -> {
                CreatedStrategy()
            }
            "shipped" -> {
                ShippedStrategy()
            }
            "location" -> {
                LocationStrategy()
            }
            "delivered" -> {
                DeliveredStrategy()
            }
            "noteadded" -> {
                NoteAddedStrategy()
            }
            else -> return
        }

        shipment.strategy = strategy

        // Apply timestamp if it's valid
        if (timestamp > 0) {
            val dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault())
            shipment.expectedDeliveryDateTimestamp = timestamp
        }

        shipment.applyStrategy(null) // Assuming otherInfo is not used in this case
    }

    suspend fun updateShipments(fileContent: List<String>) {
        fileContent.forEach { line ->
            processUpdate(line)
            delay(1000)  // Process one update per second
        }
    }

    suspend fun runSimulation() {
        val filename = "src/main/kotlin/test.txt"  // Replace with your actual file path

        val fileContent = File(filename).readLines()

        updateShipments(fileContent)
    }
}
