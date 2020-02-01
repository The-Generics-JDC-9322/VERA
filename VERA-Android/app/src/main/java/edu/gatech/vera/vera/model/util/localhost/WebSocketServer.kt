package edu.gatech.vera.vera.model.util.localhost

import android.util.Log
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.http.cio.websocket.close
import io.ktor.http.cio.websocket.CloseReason
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readBytes
import io.ktor.http.cio.websocket.readText
import io.ktor.routing.routing
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.applicationEngineEnvironment
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.websocket.WebSockets
import io.ktor.websocket.webSocket
import kotlinx.coroutines.channels.SendChannel
import org.slf4j.impl.StaticLoggerBinder
import java.lang.Integer.parseInt


/**
 * The WebSocketServer class is a singleton for handling the connecting to the
 * Fitbit Companion API JRE. This class implements an HTTP Server using the
 * ktor.io library and an embedded Netty Server. With an open endpoint on the
 * private loopback address we accept webSocket connections and ktor handles
 * all the WebSocket Protocol frames.
 * <p>
 * For more information about ktor see [Server Introduction](https://ktor.io/se
 * rvers/index.html)
 * <p>
 * Messages from the Fitbit Companion that contain important data will be put
 * into the data variable for consumption by the FitbitLocalhostDevice
 *
 * @author navaem@gatech.edu
 * @see edu.gatech.vera.vera.model.device.devices.FitbitLocalhostDevice
 */
object WebSocketServer {

    /** Reference to the ApplicationEngine that runs the Server*/
    var appEngine: ApplicationEngine? = null

    /** The current request from the FitbitLocalhostDevice*/
    var request : WebSocketRequest = WebSocketRequest.ConnectRequest

    /** data for sending to or reading from the Companion*/
    var data: Int? = null

    /**
     *
     */
    fun connect() {
        //define an application environment for the server
        val env = applicationEngineEnvironment {
            //not sure what this module does
            module {
                manageConnection()
            }

            //define host and port
            connector {
                host = "localhost"
                port = 4500
            }

            //logging config
            log = StaticLoggerBinder
                .getSingleton()
                .loggerFactory
                .getLogger("Application")

        }

        //start the netty server
        appEngine = embeddedServer(Netty, env).start(true)
    }

    /***
     * T
     */
    private fun Application.manageConnection () {
        //install WebSockets on our server
        install(WebSockets) {
            maxFrameSize = Long.MAX_VALUE // Disabled (max value). The connection will be closed if surpassed this length.
            masking = false
        }

        //define WebSocket Server
        routing {
            webSocket("/") {
                for (frame in incoming) {
                    when (frame) {
                        is Frame.Text -> {
                            val text = frame.readText()
                            Log.d("WebSocketServer", "Client SAID: $text")
                            val receivedRequest = WebSocketRequest.getType(text)
                            handleReceiveMsg(receivedRequest, text, outgoing, ::close)
                        }
                        is Frame.Binary -> {
                            val bytes = frame.readBytes()
                            Log.d("WebSocketServer", "Client SAID: $bytes")
                            val receivedRequest = WebSocketRequest.getType(bytes.toString())
                            handleReceiveMsg(receivedRequest, bytes.toString(), outgoing, ::close)

                        }
                    }
                }
            }
        }
    }

    /**
     *
     */
    private suspend fun handleReceiveMsg(
        receivedRequest: WebSocketRequest,
        raw: String,
        outgoingContent: SendChannel<Frame>,
        close: suspend (CloseReason) -> Unit
    ) {
        when (receivedRequest) {
            WebSocketRequest.EndConnection -> {
                close(CloseReason(CloseReason.Codes.NORMAL, "Client said Bye"))
            }
            WebSocketRequest.NullRequest -> {}
            WebSocketRequest.GetHealthData -> {
                val splitArray = raw.split(",")
                data = parseInt(splitArray[1])
            }
            WebSocketRequest.ConnectRequest -> {}
        }
    }

    /**
     * Gracefully terminates the Server and sets appEngine to null. To restart
     * the Server, connect() must be called again.
     */
    fun terminate() {
        appEngine?.stop(1000, 5000)
        appEngine = null
    }

}