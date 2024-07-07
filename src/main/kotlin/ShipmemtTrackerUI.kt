import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun ShipmentTrackerUI(viewModel: TrackerViewHelper) {
    Column() {
        var trackingNumber by remember { mutableStateOf("") }

        TextField(
            value = trackingNumber,
            onValueChange = { trackingNumber = it },
            label = { Text("Enter Tracking Number") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { viewModel.trackShipment(trackingNumber) }) {
            Text("Track")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { viewModel.stopTracking() }) {
            Text("Stop Tracking")
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (viewModel.shipmentId.isNotEmpty()) {
            Text("Shipment ID: ${viewModel.shipmentId}")
            Text("Status: ${viewModel.shipmentStatus}")
            Text("Expected Delivery: ${viewModel.expectedShipmentDeliveryDate}")
            Text("Notes:")
            viewModel.shipmentNotes.forEach {
                Text(it)
            }
            Text("Update History:")
            viewModel.shipmentUpdateHistory.forEach {
                Text(it)
            }
        } else {
            Text("No Shipment Being Tracked")
        }
    }
}

@Composable
@Preview
fun PreviewShipmentTrackerUI() {
    val viewModel = TrackerViewHelper()
    ShipmentTrackerUI(viewModel = viewModel)
}
