package edu.gatech.vera.vera.model.util.localhost

import android.util.Log
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.ServerSocket
import java.net.Socket
import java.security.MessageDigest
import java.util.*
import java.util.regex.Pattern


class WebSocketServer {

    val serverSocket : ServerSocket = ServerSocket(4500)
    var client : Socket = Socket()
    var request : WebSocketRequest = WebSocketRequest.ConnectRequest

        //todo this implementation is probably very buggy
    fun connect(): Int {
        serverSocket.use { serverSocket ->
            client = serverSocket.accept()
            val inStream = BufferedReader(InputStreamReader(client.getInputStream()))
            val out = client.getOutputStream()
            val s = Scanner(client.getInputStream(), "UTF-8")

            s.use { s ->
                val data = s.useDelimiter("\\r\\n\\r\\n").next()
                val get = Pattern.compile("^GET").matcher(data)
                if (get.find()) {
                    val match = Pattern.compile("Sec-WebSocketServer-Key: (.*)").matcher(data);
                    match.find();
                    val response = ("HTTP/1.1 101 Switching Protocols\r\n"
                            + "Connection: Upgrade\r\n"
                            + "Upgrade: websocket\r\n"
                            //                            + "Sec-WebSocketServer-Accept: "
                            //                            + Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-1").digest((match.group(1) + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes("UTF-8")))
                            + "\r\n\r\n").toByteArray()
                    out.write(response, 0, response.size);
                    manageConnection(inStream, out)

                }
            }
        }
        return 0
    }

    private fun manageConnection(inStream: BufferedReader, out: OutputStream) {
        val inputLine = inStream.readLine()
        while (inputLine != null) {
            Log.d("WebSocketServer","Got input from socket " + inputLine)

            out.write(request.requestStr.toByteArray())
            Log.d("WebSocketServer", "Sending" + request.requestStr.toByteArray())

            if (request == WebSocketRequest.EndConnection) {
                break
            }
        }
    }


}