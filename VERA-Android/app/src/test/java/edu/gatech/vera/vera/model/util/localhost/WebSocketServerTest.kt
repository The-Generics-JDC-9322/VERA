package edu.gatech.vera.vera.model.util.localhost

import com.tinder.scarlet.*
import org.junit.Test
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
        //val server = WebSocketServer
        var thread: Thread? = null

        //Launch server connection thread
        Thread {
            thread = Thread.currentThread()
            WebSocketServer.connect()
        }.start()

        //connect a client to the server
        val clientSocket = Socket("localhost", 4500)
        assertTrue(clientSocket.isConnected)

        val out = clientSocket.getOutputStream()

        /* this is an HTTP get request that is the beginning of every websocket
         protocol */
        val connectMsg = ("GET /chat HTTP/1.1\\r\n" +
                "Host: example.com:8000\r\n" +
                "Upgrade: websocket\r\n" +
                "Connection: Upgrade\r\n\r\n").toByteArray()

        out.write(connectMsg, 0, connectMsg.size)
        System.out.println("Wrote message")

        assertTrue(!WebSocketServer.serverSocket.isClosed)
        System.out.println("ServerSocket still open")


        val inStream = BufferedReader(
            InputStreamReader(clientSocket.getInputStream())
        )
        assertNotNull(inStream)

        //Check that the returned header switches protocols to websocket
        val expected = arrayOf("HTTP/1.1 101 Switching Protocols",
            "Connection: Upgrade",
            "Upgrade: websocket")

        //get the actual response from the server
        val str1 = inStream.readLine()
        val str2 = inStream.readLine()
        val str3 = inStream.readLine()
        val actual = arrayOf(str1, str2, str3)


        val bool = expected.contentEquals(actual)
        assertTrue(bool)

        //write our protocol to the server
        out.write("[hb,87]".toByteArray())

        clientSocket.close()
        thread?.join()
    }

    /**
     * This test assures that we can connect to our webSocket Server using a
     * Kotlin WebSocket Client library. This will mimic the javascript
     * WebSocket client that the Companion API uses.
     */
    @Test
    fun connectUsingScarlet() {
        var thread: Thread? = null

        //Launch server connection thread
        Thread {
            thread = Thread.currentThread()
            WebSocketServer.connect()
        }.start()


        val client = OkHttpClient.Builder().build()
        val scarletInstance = Scarlet.Builder()
            .webSocketFactory(client.newWebSocketFactory("ws://localhost:4500"))
            .build()

        scarletInstance.create<WebService>()

        assertTrue(!WebSocketServer.serverSocket.isClosed)

        thread?.join()
    }

    interface WebService {
        @Receive
        fun observeWebSocketEvent() : Stream<WebSocket.Event>

        @Send
        fun sendSubscribe(message: ByteArray)
    }


}