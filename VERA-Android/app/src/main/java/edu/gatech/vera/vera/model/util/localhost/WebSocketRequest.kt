package edu.gatech.vera.vera.model.util.localhost

enum class WebSocketRequest(val requestStr: String) {
    NullRequest(""),

    GetHealthData("[hb]\r\n\r\n"),

    EndConnection("[x]\r\n\r\n"),

    ConnectRequest("[c]\r\n\r\n")

}