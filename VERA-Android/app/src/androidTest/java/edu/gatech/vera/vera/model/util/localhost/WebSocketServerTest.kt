package edu.gatech.vera.vera.model.util.localhost

import org.junit.Test
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send

import okhttp3.OkHttpClient
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory

import org.junit.Assert.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.Socket


class WebSocketServerTest {

    @Test
    fun connect() {
        var server = WebSocketServer
        System.out.println("Got server")

        //Launch server connection thread
        Thread {
            server.connect()
        }.start()

        //connect a client to the server
        var clientSocket = Socket("localhost", 4500)
        assertTrue(clientSocket.isConnected)

        val out = clientSocket.getOutputStream()
        val connectMsg = ("GET /chat HTTP/1.1\\r\n" +
                "Host: example.com:8000\r\n" +
                "Upgrade: websocket\r\n" +
                "Connection: Upgrade\r\n\r\n").toByteArray()

        out.write(connectMsg, 0, connectMsg.size)
        System.out.println("Wrote message")

        assertTrue(!server.serverSocket.isClosed)
        System.out.println("ServerSocket still open")


        val inStream = BufferedReader(
            InputStreamReader(clientSocket.getInputStream())
        )
        assertNotNull(inStream)

        //Check that the returned header switches protocols to websocket
        val expected = arrayOf("HTTP/1.1 101 Switching Protocols",
            "Connection: Upgrade",
            "Upgrade: websocket")

        val str1 = inStream.readLine()
        System.out.println(str1)

        val str2 = inStream.readLine()
        val str3 = inStream.readLine()

        val actual = arrayOf(str1, str2, str3)
        System.out.println(actual)


        val bool = expected.contentEquals(actual)
        assertTrue(bool)

        //write our protocol to the server
        out.write("[hb,87]".toByteArray())

        clientSocket.close()

    }

    /**
     * This test assures that we can connect to our webSocket Server using a
     * Kotlin WebSocket Client library. This will mimic the javascript
     * WebSocket client that the Companion API uses.
     */
    @Test
    fun connectUsingScarlet() {
        val client = OkHttpClient.Builder().build()
        val scarletInstance = Scarlet.Builder()
            .webSocketFactory(client.newWebSocketFactory("wss://localhost:4500"))
            .build()

        scarletInstance.create<WebService>()
    }

    interface WebService {
        @Receive
        fun observeWebSocketEvent()

        @Send
        fun sendSubscribe()
    }


}