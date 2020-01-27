package edu.gatech.vera.vera.model.util.localhost

import org.junit.Test
import kotlinx.coroutines.*


import org.junit.Assert.*
import java.net.Socket
import java.net.SocketAddress

class WebSocketServerTest {

    @Test
    fun connect() = runBlocking {
        var server = WebSocketServer()
        val job = launch {
            server.connect()
        }
        var clientSocket = Socket("localhost", 4500)
        assertTrue(clientSocket.isConnected)

        val out = clientSocket.getOutputStream()
        out.write("Hello".toByteArray())

        clientSocket.close()

        job.join()
    }
}