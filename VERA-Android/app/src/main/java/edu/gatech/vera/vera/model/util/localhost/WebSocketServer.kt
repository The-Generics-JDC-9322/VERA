package edu.gatech.vera.vera.model.util.localhost

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
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
                val data = inStream.readLine()
                Log.d("WebSocketServer", data)
                val get = Pattern.compile("^GET").matcher(data)
                if (get.find()) {
                    val match = Pattern.compile("Sec-WebSocketServer-Key: (.*)").matcher(data)
                    match.find()
                    val response = ("HTTP/1.1 101 Switching Protocols\r\n"
                            + "Connection: Upgrade\r\n"
                            + "Upgrade: websocket\r\n"
                            //                            + "Sec-WebSocketServer-Accept: "
                            //                            + Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-1").digest((match.group(1) + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes("UTF-8")))
                            + "\r\n\r\n").toByteArray()

                    out.write(response, 0, response.size)
                    Log.d("WebSocketServer", "Wrote upgrade Request to Client")
                    manageConnection(client, inStream, out)

                } else {
                    Log.d("WebSocketServer", "Unknown message received")
                }
            } catch (sE: Throwable) {
                Log.e("WebSocketServer", sE.message)
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

    private fun manageConnection(client: Socket, inStream: BufferedReader, out: OutputStream) {
        var inputLine = inStream.readLine()
        while (client.isConnected) {
            Log.d("WebSocketServer","Got input from socket " + inputLine)

            out.write(request.requestStr.toByteArray())
            Log.d("WebSocketServer", "Sending " + request.requestStr)

            if (request == WebSocketRequest.EndConnection) {
                break
            }
            inputLine = inStream.readLine()
            Log.d("WebSocketServer", "Found " + inputLine)
        }
    }


}