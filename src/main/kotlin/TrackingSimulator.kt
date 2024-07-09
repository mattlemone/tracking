import com.example.shipmenttracking.*
import kotlinx.coroutines.delay
import java.io.File

class TrackingSimulator : Subject {
    private val observers = mutableListOf<Observer>()
    private val shipments = mutableListOf<Shipment>()

    override fun registerObserver(observer: Observer) {
        observers.add(observer)
    }

    override fun removeObserver(observer: Observer) {
        observers.remove(observer)
    }

    override fun notifyObservers() {
        for (observer in observers) {
            observer.update()
        }
    }

    fun findShipment(id: String): Shipment? {
        return shipments.find { it.id == id }
    }

    fun addShipment(shipment: Shipment) {
        if (findShipment(shipment.id) == null) {
            shipments.add(shipment)
            println("Adding $shipment")
        }
    }

    suspend fun updateShipments(fileContent: List<String>) {
        fileContent.forEach { line ->
            processUpdate(line)
            notifyObservers()
            delay(1000)  // Process one update per second
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

    companion object {
        suspend fun runSimulation() {
            val filename = "src/main/kotlin/test.txt"  // Replace with your actual file path

            val simulator = TrackingSimulator()
            val fileContent = File(filename).readLines()

            simulator.updateShipments(fileContent)
        }
    }
}
