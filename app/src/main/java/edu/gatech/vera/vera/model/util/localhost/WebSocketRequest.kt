package edu.gatech.vera.vera.model.util.localhost

import java.util.regex.Pattern

/**
 * This class defines the WebSocket Requests that the WebSocketServer and
 * Companion API use to communicate.
 *
 * @author navaem@gatech.edu
 * @see WebSocketServer
 */
enum class WebSocketRequest(val requestStr: String, val regexStr: String) {

    /** default request is no request. Mainly used as a placeholder. */
    NullRequest("", ""),

    /** request heath data from the client connection */
    GetHealthData("[hb]\r\n\r\n", "[\\[]hb.*"),

    /** request an end to the connection to the client. */
    EndConnection("[x]\r\n\r\n", "[\\[]x.*"),

    /** request a connection be established */
    ConnectRequest("[c]\r\n\r\n", "[\\[]c.*");

    companion object {

        /**
         * This function turns a string message into a WebSocketRequest. If the
         * message is unrecognized or not parsable, a NullRequest will be
         * returned.
         *
         * @param msg the message received from the Companion
         * @return a WebSocketRequest representing the message type
         */
        fun getType(msg: String): WebSocketRequest {
            var request = NullRequest
            var trimmed = msg.trim()

            for (enumRequest in values()) {
                if (Pattern.matches(enumRequest.regexStr, trimmed)) {
                    request = enumRequest
                }
            }

            return request
        }
    }
}