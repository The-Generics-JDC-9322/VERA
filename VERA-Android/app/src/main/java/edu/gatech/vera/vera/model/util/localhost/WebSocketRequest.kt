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

    NullRequest("", ""),

    GetHealthData("[hb]\r\n\r\n", "[\\[]hb.*"),

    EndConnection("[x]\r\n\r\n", "[\\[]x.*"),

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