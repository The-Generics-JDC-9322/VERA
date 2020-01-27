package edu.gatech.vera.vera.model.util.localhost

enum class WebSocketRequest(val requestStr: String) {
    NullRequest(""),

    GetHealthData("[hb]"),

    EndConnection("[x]"),

    ConnectRequest("[c]")

}