import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
class MainTest {

    @Test
    fun testMainFunction() = runBlockingTest {
        val simulator = mock<TrackingSimulator>()

        main()

        // Verify that the simulator's runSimulation function was called
        verify(simulator).runSimulation()
    }
}
