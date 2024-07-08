package com.example.shipmenttracking

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ShipmentTrackerUI(trackerViewHelper: TrackerViewHelper) {
    Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
        TextField(
            value = trackerViewHelper.shipmentIdInput,
            onValueChange = { trackerViewHelper.onShipmentIdChange(it) },
            label = { Text("Enter Shipment ID") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { trackerViewHelper.trackShipment() },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Track")
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f)) {
            items(trackerViewHelper.trackedShipments) { shipment ->
                TrackedShipmentView(shipment, onStopTracking = {
                    trackerViewHelper.stopTracking(shipment.id)
                })
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun TrackedShipmentView(shipment: TrackedShipment, onStopTracking: () -> Unit) {
    Column {
        Text("Tracking Shipment ID: ${shipment.id}")
        Spacer(modifier = Modifier.height(8.dp))
        Text("Shipment Status: ${shipment.status}")
        Spacer(modifier = Modifier.height(8.dp))
        Text("Expected Delivery Date: ${shipment.expectedDeliveryDate}")
        Spacer(modifier = Modifier.height(8.dp))
        Text("Shipment Notes:")
        shipment.notes.forEach { note ->
            Text("- $note")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text("Update History:")
        shipment.updateHistory.forEach { update ->
            Text("- $update")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onStopTracking, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("Stop Tracking")
        }
    }
}

@Preview
@Composable
fun PreviewShipmentTrackerUI() {
    val simulator = TrackingSimulator()
    val trackerViewHelper = TrackerViewHelper(simulator)
    ShipmentTrackerUI(trackerViewHelper)
}
