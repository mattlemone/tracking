import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.shipmenttracking.ShipmentTrackerUI
import com.example.shipmenttracking.TrackerViewHelper
import com.example.shipmenttracking.TrackingSimulator

@Composable
fun MyApp() {
    val simulator = remember { TrackingSimulator() }
    val viewModel = remember { TrackerViewHelper(simulator) }
    MaterialTheme {
        ShipmentTrackerUI(viewModel)
    }
}

@ExperimentalComposeUiApi
@Composable
//@Preview(showBackground = true)
fun DefaultPreview() {
    MyApp()
}

fun main() {
//    TrackingSimulator.runSimulation(fileContent)
    application {
        Window(onCloseRequest = ::exitApplication) {
            MyApp()
        }
    }
}
