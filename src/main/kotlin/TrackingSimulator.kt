object TrackingSimulator {
    private val shipments = mutableListOf<Shipment>()

    fun findShipment(id: String): Shipment? {
        return shipments.find { it.id == id }
    }

    fun addShipment(shipment: Shipment) {
        shipments.add(shipment)
    }

    fun runSimulation(fileContent: List<String>) {
        // Simulate reading updates from the file and processing them
        fileContent.forEach { line ->
            processUpdate(line)
            Thread.sleep(1000)  // Process one update per second
        }
    }

    private fun processUpdate(line: String) {
        // Implement logic to process each line of the file
        // and update the corresponding shipment
    }
}
