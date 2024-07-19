import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

class TrackingServer {
    fun startServer(simulator: TrackingSimulator) {
        println("Server started")
        embeddedServer(Netty, 8080) {
            routing {
                get("/") {
                    call.respondText(File("index.html").readText(), ContentType.Text.Html)
                }

                post("/data") {
                    val data = call.receiveText()
                    val dataList = data.split(",")

                    // Validate data
                    val validationError = validateData(dataList)
                    if (validationError != null) {
                        call.respond(HttpStatusCode.BadRequest, validationError)
                        return@post
                    }

                    println("nice")
                    simulator.updateShipments(dataList)
                    call.respond(HttpStatusCode.OK, "Data processed successfully")
                }
            }
        }.start(wait = true)
    }

    private fun validateData(dataList: List<String>): String? {
        if (dataList.size < 4) {
            return "Invalid request: Insufficient data. Expected at least 4 elements."
        }

        val updateType = dataList[0]
        if (updateType !in listOf("created", "shipped", "location", "delivered", "noteadded", "lost", "canceled")) {
            return "Invalid request: Unsupported update type."
        }

        val shipmentType = dataList[2].toUpperCase()
        if (shipmentType !in listOf("STANDARD", "EXPRESS", "OVERNIGHT", "BULK")) {
            return "Invalid request: Unsupported shipment type."
        }

        // Additional validation logic can be added here

        return null // No validation errors
    }
}
