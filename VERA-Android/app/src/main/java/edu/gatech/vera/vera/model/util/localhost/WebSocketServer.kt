package edu.gatech.vera.vera.model.util.localhost

import android.util.Log
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.http.cio.websocket.close
import io.ktor.http.cio.websocket.CloseReason
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readBytes
import io.ktor.http.cio.websocket.readText
import io.ktor.http.cio.websocket.WebSocketSession
import io.ktor.routing.routing
import io.ktor.server.engine.ApplicationEngine
import io.ktor.server.engine.applicationEngineEnvironment
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.websocket.WebSockets
import io.ktor.websocket.webSocket
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
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

    var session: WebSocketSession? = null

    /** The current outgoingRequest from the FitbitLocalhostDevice*/
    var outgoingRequest : WebSocketRequest = WebSocketRequest.NullRequest

    private var incomingRequest: WebSocketRequest = WebSocketRequest.NullRequest


    /** data for sending to or reading from the Companion*/
    var data: Int? = null

    /**
     * Defines the ApplicationEnvironment and starts the embedded server.
     * The server opens an HTTP endpoint at the specified port.
     * <p>
     * connect() should not be called if there is a server running on the
     * specified port.
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
                host = "127.0.0.1"
                port = 4500
            }

            //logging config
            log = StaticLoggerBinder
                .getSingleton()
                .loggerFactory
                .getLogger("Application")

        }

        //start the netty server
        appEngine = embeddedServer(Netty, env).start(false)
    }

    /***
     * This function defines the WebSocket server running on the HTTP server.
     * The WebSocket Server accepts both Text Frames and Binary Frames.
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
                session = this
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
                            val bytesAsString = String(bytes)
                            Log.d("WebSocketServer", "Client SAID: $bytesAsString")

                            val receivedRequest = WebSocketRequest.getType(bytesAsString)
                            handleReceiveMsg(receivedRequest, bytesAsString, outgoing, ::close)

                        }
                    }
                }
            }
        }
    }

    /**
     * This function parses received messages from the WebSocket connection.
     * Based on the receivedRequest, the server will perform different actions.
     * <p>
     * A received GetHealthData will tell the server to store the HealthData in
     * the WebSocketServer Class data variable.
     * <p>
     * A received EndConnection will tell the server to close the connection
     * with the client.
     * <p>
     * NullRequests and ConnectRequests are treated as NOPs.
     *
     * @param receivedRequest WebSocketRequest from the client
     * @param raw the raw data input
     * @param outgoingContent SendChannel for sending Frames to the client
     * @param close close function for closing the connection
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

                val trimmed = raw.trim('[',']')
                val splitArray = trimmed.split(",")
                val intStr = splitArray[1].trim()
                data = parseInt(intStr)
                this.incomingRequest = WebSocketRequest.GetHealthData
            }
            WebSocketRequest.ConnectRequest -> {}
        }
    }

    /**
     * This is a suspending function which means it must be called from a
     * Kotlin CoroutineScope. It will launch an asynchronous request for
     * from the Companion API. It is possible that the request will
     * timeout or that no client is connected. In cases where the
     * FitbitLocalhostDevice cannot retrieve the HealthData from the Companion
     * API, it will return HealthData with a value of -1.
     *
     * @param request the Request to fulfill from the client
     * @return an integer for the requested data
     */
    suspend fun request(request: WebSocketRequest) = runBlocking {
        var requestData = -1

        //wait for a session for at least 4.3 seconds
        val sess = withTimeoutOrNull(4300) {
            while (session == null) {
                delay(100)
            }
            session
        }

        //wait for outgoingRequest to be NullRequest
        val outReq = withTimeoutOrNull(4300) {
            while (outgoingRequest != WebSocketRequest.NullRequest) {
                delay(100)
            }
            outgoingRequest
        }

        //if our waits have succeeded
        if (outReq == WebSocketRequest.NullRequest && sess != null) {

            //set outgoingRequest to the request we are about to perform
            outgoingRequest = request
            sess.outgoing?.send(Frame.Text(request.requestStr))

            //wait until this.incomingRequest is GetHealthData
            requestData = withTimeoutOrNull(4300L) {
                while (incomingRequest != request) {
                    delay(100)
                }
                data
            } ?: -1

            //reset outgoingRequest and data
            outgoingRequest = WebSocketRequest.NullRequest
            data = null
        }
        return@runBlocking requestData
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