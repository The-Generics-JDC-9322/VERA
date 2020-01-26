import document from "document";
import { inbox, outbox } from "file-transfer";
import writeHeartData, { getLastDataPoint, timeSinceWrite } from "./author";
import { peerSocket } from "messaging";
import { memory } from "system";

console.log("JS memory: " + memory.js.used + "/" + memory.js.total);

let hrLabel = document.getElementById("hrm");
let updatedLabel = document.getElementById("updated");
let lastValueTimestamp = Date.now();
let statusText = document.getElementById("status");

// Initialize the UI with some values
hrLabel.text = "--";
updatedLabel.text = "...";
statusText.text = "Waiting For Connection...";

function updateDisplay() {
  hrLabel.text = getLastDataPoint();
  updatedLabel.text = timeSinceWrite();
}

function onOpen(evt) {
  console.log("PeerSocket Opened");
}

function onError(evt) {
  console.log(`PeerSocket Error ${evt.data}`);
}

function onMessage(evt) {
  console.log(`PeerSocket Recv ${evt.data}`);
  //if the message says read the file then send the file data
  var message = evt.data
  console.log(`MESSAGE: ${message}`);
  if (message[0] == "hb") {
    let last = getLastDataPoint();
    peerSocket.send(["hb", last]);
  } else if (message[0] == "c") {
    statusText.text = "Connected to VERA";
    peerSocket.send(["c"]);
  }
}

function onClose(evt) {
  console.log("PeerSocketClosed");
}

peerSocket.addEventListener("open", onOpen);
peerSocket.addEventListener("error", onError);
peerSocket.addEventListener("message", onMessage);
peerSocket.addEventListener("close", onClose);

writeHeartData();

setInterval(updateDisplay, 500);


