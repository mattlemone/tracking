import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.delay
import java.io.File
import java.time.LocalDateTime

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
        val otherInfo = parts.drop(2)

        var shipment = findShipment(shipmentId)
        if (shipment == null) {
            shipment = Shipment(shipmentId, "", mutableStateListOf(), mutableStateListOf(), LocalDateTime.now(), "", null)
            addShipment(shipment)
        }

        val strategy = when (updateType) {
            "created" -> CreatedStrategy()
            "shipped" -> ShippedStrategy()
            "location" -> LocationStrategy()
            "delivered" -> DeliveredStrategy()
            "noteadded" -> NoteAddedStrategy()
            "lost" -> LostStrategy()
            "canceled" -> CanceledStrategy()
            else -> return
        }

        shipment.strategy = strategy

        // Apply strategy with otherInfo as a list of additional arguments
        strategy.processUpdate(shipment, otherInfo)
    }

    internal suspend fun updateShipments(fileContent: List<String>) {
        fileContent.forEach { line ->
            processUpdate(line)
            delay(1000)  // Process one update per second
        }
    }

    suspend fun runSimulation(filePath: String) {
        val fileContent = File(filePath).readLines()
        updateShipments(fileContent)
    }
}
