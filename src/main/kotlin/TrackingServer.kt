import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

class TrackingServer {
    fun startServer(simulator: TrackingSimulator){
        println("Server started")
        embeddedServer(Netty, 8080) {
            routing {
                get("/") {
                    call.respondText(File("index.html").readText(), ContentType.Text.Html)
                }

                post("/data"){
                    val data = call.receiveText()
                    val dataList = data.split(",")
                    println("nice")
                    simulator.updateShipments(dataList)
                }
            }
        }.start(wait = true)
    }
}