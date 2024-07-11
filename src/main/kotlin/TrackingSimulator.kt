import com.example.shipmenttracking.*
import kotlinx.coroutines.delay
import java.io.File

class TrackingSimulator {
    private val shipments = mutableListOf<Shipment>()

    fun findShipment(id: String): Shipment? {
        println("Searching for shipment with ID: $id")
        println("Current shipments: ${shipments.map { it.id }}")
        return shipments.find { it.id == id }.also {
            println("Found shipment: $it")
        }
    }

    private fun addShipment(shipment: Shipment) {
        if (findShipment(shipment.id) == null) {
            shipments.add(shipment)
            println("Added new shipment: ${shipment.id}")
        }
    }

    private fun processUpdate(line: String) {
        val parts = line.split(',')
        if (parts.size < 3) return

        val updateType = parts[0]
        val shipmentId = parts[1]
        val otherInfo = parts.getOrNull(2)

        var shipment = findShipment(shipmentId)
        if (shipment == null) {
            shipment = Shipment(shipmentId, "", mutableListOf(), mutableListOf(), 0, "", null)
            addShipment(shipment)
        }

        val strategy = when (updateType) {
            "created" -> CreatedStrategy()
            "shipped" -> ShippedStrategy()
            "location" -> LocationStrategy()
            "delivered" -> DeliveredStrategy()
            "noteadded" -> NoteAddedStrategy()
            else -> return
        }

        shipment.strategy = strategy
        shipment.applyStrategy(otherInfo)
    }

    suspend fun updateShipments(fileContent: List<String>) {
        println("Starting to update shipments...")
        fileContent.forEach { line ->
            println("Processing line: $line")
            processUpdate(line)
            delay(1000)  // Process one update per second
        }
        println("Finished updating shipments. Total shipments: ${shipments.size}")
    }

    companion object {
        suspend fun runSimulation(): TrackingSimulator {
            val filename = "src/main/kotlin/test.txt"  // Replace with your actual file path
            println("Starting simulation with file: $filename")

            val simulator = TrackingSimulator()
            val fileContent = File(filename).readLines()
            println("File content: $fileContent")

            simulator.updateShipments(fileContent)
            return simulator
        }
    }
}
