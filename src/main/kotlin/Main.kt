import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.shipmenttracking.ShipmentTrackerUI
import com.example.shipmenttracking.TrackerViewHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MyApp(simulator: TrackingSimulator) {
    val viewModel = remember { TrackerViewHelper(simulator) }
    MaterialTheme {
        ShipmentTrackerUI(viewModel)
    }
}

fun main() {
    val simulator = TrackingSimulator()
    val server = TrackingServer()

    CoroutineScope(Dispatchers.Default).launch {
        server.startServer(simulator)
    }

    application {
        Window(onCloseRequest = ::exitApplication) {
            MyApp(simulator)
        }
    }
}