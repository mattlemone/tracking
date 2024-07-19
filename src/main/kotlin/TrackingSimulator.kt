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

    private fun processUpdate(line: List<String>) {
        if (line.size < 4) return // Ensure there are enough elements in the line list

        val updateType = line[0]
        val shipmentId = line[1]
        val shipmentTypeInput = line[2].uppercase() // Get the shipment type from the line list
        val otherInfo = line.drop(3) // Remaining elements

        // Map the shipment type input to ShipmentType enum
        val shipmentType = when (shipmentTypeInput) {
            "STANDARD" -> ShipmentType.STANDARD
            "EXPRESS" -> ShipmentType.EXPRESS
            "OVERNIGHT" -> ShipmentType.OVERNIGHT
            "BULK" -> ShipmentType.BULK
            else -> {
                println("Invalid shipment type. Defaulting to STANDARD.")
                ShipmentType.STANDARD
            }
        }

        var shipment = findShipment(shipmentId)
        if (shipment == null) {
            shipment = ShipmentFactory.createShipment(
                shipmentType,
                shipmentId,
                "",  // Initial status
                mutableStateListOf(),  // Initial notes
                mutableStateListOf(),  // Initial update history
                LocalDateTime.now(),  // Initial expected delivery date
                "",  // Initial current location
                null  // Initial strategy
            )
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
        processUpdate(fileContent)
        delay(1000)  // Process one update per second
    }

    suspend fun runSimulation(filePath: String) {
        val fileContent = File(filePath).readLines()
        updateShipments(fileContent)
    }
}
