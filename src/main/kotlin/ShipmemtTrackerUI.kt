package com.example.shipmenttracking

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ShipmentTrackerUI(trackerViewHelper: TrackerViewHelper) {
    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = trackerViewHelper.shipmentId,
            onValueChange = { trackerViewHelper.trackShipment(it) },
            label = { Text("Enter Shipment ID") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Shipment Status: ${trackerViewHelper.shipmentStatus}")
        Spacer(modifier = Modifier.height(16.dp))
        Text("Expected Delivery Date: ${trackerViewHelper.expectedShipmentDeliveryDate}")
        Spacer(modifier = Modifier.height(16.dp))
        Text("Shipment Notes:")
        trackerViewHelper.shipmentNotes.forEach { note ->
            Text("- $note")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Update History:")
        trackerViewHelper.shipmentUpdateHistory.forEach { update ->
            Text("- $update")
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
