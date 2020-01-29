package edu.gatech.vera.vera.model.util.localhost

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.lang.StringBuilder
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException
import java.security.MessageDigest
import java.util.*
import java.util.regex.Pattern



object WebSocketServer {

    var serverSocket : ServerSocket = ServerSocket(4500)
    var request : WebSocketRequest = WebSocketRequest.ConnectRequest

    var data = null

    @UseExperimental(ExperimentalStdlibApi::class)
    fun connect() {
        if (serverSocket.isClosed) {
            serverSocket = ServerSocket(4500)
        }
        try {
            Log.d("WebSocketServer", "Waiting for client connection")
            val client = serverSocket.accept()
            try {
                Log.d("WebSocketServer", "Got client connection")
                val inStream = BufferedReader(InputStreamReader(client.getInputStream()))
                val out = client.getOutputStream()
                //val data = client.getInputStream().readBytes().decodeToString()//dataB = StringBuilder()
//                data =
//                while ()//inStream.readLine()
//                var data = dataB.toString()
                val s = Scanner(client.getInputStream(), "UTF-8")
                val data = s.useDelimiter("\\r\\n\\r\\n").next()
                Log.d("WebSocketServer", data)
                val get = Pattern.compile("^GET").matcher(data)
                if (get.find()) {
                    val match = Pattern.compile("Sec-WebSocket-Key: (.*)").matcher(data)
                    match.find()
                    val response = ("HTTP/1.1 101 Switching Protocols\r\n"
                            + "Connection: Upgrade\r\n"
                            + "Upgrade: websocket\r\n"
                            + "Sec-WebSocketServer-Accept: "
                            + android.util.Base64.encodeToString(MessageDigest.getInstance("SHA-1").digest((match.group(1) + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").toByteArray()),android.util.Base64.DEFAULT)
                            + "\r\n\r\n").toByteArray()
                    out.write(response, 0, response.size)
                    Log.d("WebSocketServer", "Wrote upgrade Request to Client")
                    manageConnection(client, s, out)

                } else {
                    Log.d("WebSocketServer", "Unknown message received")
                }
            } catch (sE: Throwable) {
                Log.e("WebSocketServer", "${sE.printStackTrace()} ${sE.message}")
            } finally {
                client.close()
            }
        } catch (sSE: Throwable) {
            Log.e("WebSocketServer", sSE.message)
        } finally {
            serverSocket.close()
        }
        Log.d("WebSocketServer", "returned from connection")
    }

    private fun manageConnection(client: Socket, inScanner: Scanner, out: OutputStream) {
        var inputLine = inScanner.next()
        while (client.isConnected) {
            Log.d("WebSocketServer","Got input from socket " + inputLine)

            out.write(request.requestStr.toByteArray())
            Log.d("WebSocketServer", "Sending " + request.requestStr)

            if (request == WebSocketRequest.EndConnection) {
                break
            }
            inputLine = inScanner.next()
            Log.d("WebSocketServer", "Found " + inputLine)
        }
    }

    /**
     * This function retrieves the client webSocket key an computes the server
     * Sec-WebSocketServer-Accept response.
     *
     * Eg.
     *
     *     val data = "GET / HTTP/1.1\n" +
     *         "    Upgrade: websocket\n" +
     *         "    Connection: Upgrade\n" +
     *         "    Sec-WebSocket-Key: jqWEAOhTGw0TZUdqZUjVfg==\n" +
     *         "    Sec-WebSocket-Version: 13\n" +
     *         "    Host: localhost:4500\n" +
     *         "    Accept-Encoding: gzip\n" +
     *         "    User-Agent: okhttp/4.3.1"
     *     val s = WebSocketServer.retrieveAcceptKey(data)
     *     System.out.println(s)
     *
     *     user@host:~$ 3GiMmUWBe7vXgH2VYv6pSoRjG78=
     *
     * @param packet the incoming upgrade to websocket connection packet
     * @return String containing the Sec-WebSocketServer-Accept response
     */
    fun retrieveAcceptKey(packet: String) : String {
        Log.d("WebSocketServer.retrieveAcceptKey", packet)
        val match = Pattern.compile("Sec-WebSocket-Key: (.*)").matcher(packet)
        match.find()
        val byteArrayToDigest = (match.group(1) + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").toByteArray()
        val stringToEncode = MessageDigest.getInstance("SHA-1").digest(byteArrayToDigest)
        val s = android.util.Base64.encodeToString(stringToEncode, android.util.Base64.DEFAULT)
        return s
    }


}