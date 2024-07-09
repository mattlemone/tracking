import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.shipmenttracking.ShipmentTrackerUI
import com.example.shipmenttracking.TrackerViewHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MyApp() {
    val simulator = remember { TrackingSimulator() }
    val viewModel = remember { TrackerViewHelper(simulator) }
    MaterialTheme {
        ShipmentTrackerUI(viewModel)
    }
}

fun main() {
    application {
        val window = Window(onCloseRequest = ::exitApplication) {
            MyApp()
        }

        CoroutineScope(Dispatchers.Default).launch {
            TrackingSimulator.runSimulation()
        }
    }
}