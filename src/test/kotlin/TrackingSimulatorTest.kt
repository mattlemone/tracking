//import junit.framework.TestCase.*
//import kotlinx.coroutines.test.runBlockingTest
//import org.junit.Before
//import org.junit.Test
//
//class TrackingSimulatorTest {
//
//    private lateinit var simulator: TrackingSimulator
//
//    @Before
//    fun setUp() {
//        simulator = TrackingSimulator()
//    }
//
//    @Test
//    fun testFindShipment() = runBlockingTest {
//        // Indirectly add a shipment via updateShipments
//        simulator.updateShipments(listOf("created,1,info"))
//        val foundShipment = simulator.findShipment("1")
//        assertNotNull(foundShipment)
//        assertEquals("1", foundShipment?.id)
//    }
//
//    @Test
//    fun testUpdateShipmentsWithCreatedStrategy() = runBlockingTest {
//        simulator.updateShipments(listOf("created,2,info"))
//        val shipment = simulator.findShipment("2")
//        assertNotNull(shipment)
//        assertTrue(shipment?.strategy is CreatedStrategy)
//    }
//
//    @Test
//    fun testUpdateShipmentsWithShippedStrategy() = runBlockingTest {
//        // First create the shipment
//        simulator.updateShipments(listOf("created,3,info"))
//        // Now ship it
//        simulator.updateShipments(listOf("shipped,3,info"))
//        val shipment = simulator.findShipment("3")
//        assertNotNull(shipment)
//        assertTrue(shipment?.strategy is ShippedStrategy)
//    }
//
//    @Test
//    fun testUpdateMultipleShipments() = runBlockingTest {
//        val fileContent = listOf("created,4,info", "shipped,4,info")
//        simulator.updateShipments(fileContent)
//        val shipment = simulator.findShipment("4")
//        assertNotNull(shipment)
//        assertTrue(shipment?.strategy is ShippedStrategy)
//    }
//
//    @Test
//    fun testRunSimulation() = runBlockingTest {
//        // Mock the file read operation by replacing `runSimulation` to read from a mock file content
//        // Since we can't directly test private methods, we'll simulate a file read
//        val fileContent = listOf("created,5,info", "delivered,5,info")
//        simulator.updateShipments(fileContent)
//        val shipment = simulator.findShipment("5")
//        assertNotNull(shipment)
//        assertTrue(shipment?.strategy is DeliveredStrategy)
//    }
//}
