import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

@Composable
fun MyApp() {
    val viewModel = remember { TrackerViewHelper() }
    MaterialTheme {
        ShipmentTrackerUI(viewModel = viewModel)
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
