package com.example.shipmenttracking

import Shipment
import TrackingSimulator
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ShipmentTrackerUI(trackerViewHelper: TrackerViewHelper) {
    var forceRecompose by remember { mutableStateOf(0) }
    var shipmentIdInput by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
        TextField(
            value = shipmentIdInput,
            onValueChange = { shipmentIdInput = it },
            label = { Text("Enter Shipment ID") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                trackerViewHelper.trackShipment(shipmentIdInput)
                forceRecompose++ // Force recomposition
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Track")
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f)) {
            items(trackerViewHelper.trackedShipments) { shipment ->
                key(shipment.id) {
                    TrackedShipmentView(shipment, onStopTracking = {
                        trackerViewHelper.stopTracking(shipment.id)
                        forceRecompose++ // Force recomposition
                    })
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
    // This will force recomposition whenever forceRecompose changes
    LaunchedEffect(forceRecompose) {}
}

@Composable
fun TrackedShipmentView(shipment: Shipment, onStopTracking: () -> Unit) {
    Column {
        Text("Tracking Shipment ID: ${shipment.id}")
        Text("Shipment Status: ${shipment.status}")
        Text("Expected Delivery Date: ${shipment.expectedDeliveryDateTimestamp}")
        Text("Shipment Notes:")
        shipment.notes.forEach { note ->
            Text("- $note")
        }
        Text("Update History:")
        shipment.updateHistory.forEach { update ->
            Text("- Went from ${update.previousStatus} to ${update.newStatus} on ${update.timestamp}")
        }
        Button(onClick = onStopTracking) {
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
