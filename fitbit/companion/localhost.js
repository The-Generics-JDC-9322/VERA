import {peerSocket} from "messaging";

const wsUri = "wss://localhost:4500/";
export const websocket = new WebSocket(wsUri);

export function addWebsocketHandlers() {
  websocket.addEventListener("open", onOpen);
  websocket.addEventListener("close", onClose);
  websocket.addEventListener("message", onAppMessage);
  websocket.addEventListener("error", onError);
  console.log("Websocket is currently: " + websocket.readyState)
}

function send(msg) {
  if (websocket.readyState === WebSocket.OPEN) {
    websocket.send(msg);
  } else {
    console.log("Websocket not open. Cannot send message.");
  }
}

function onOpen(evt) {
   console.log("CONNECTED");
   websocket.send(message);
}

function onClose(evt) {
   console.log("DISCONNECTED");
}

function onAppMessage(evt) {
  console.log(`MESSAGE: ${evt.data}`);
  var message = evt.data
  if (message[0] == "hb") {
    peerSocket.send(['hb']);
  } else if (message[0] == "c") {
    peerSocket.send(['c']);
  }
}

function onError(evt) {
  evt.preventDefault();
  console.error(`ERROR: ${evt.type}`);
}

export function sendHealthDataFile(file) {
  send(file);
}

export function sendHealthDataMessage(msg) {
  console.log(`Sending message over websocket ${msg}`);
  send(msg);
}

export function sendConnectedMessage() {
  send(["c"]);
}