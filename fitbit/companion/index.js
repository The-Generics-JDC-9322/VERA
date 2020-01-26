//import { inbox } from "file-transfer";
//import { sendfile, processAllFilesFromDevice } from "./file-utils";
import { addWebsocketHandlers, sendHealthDataMessage, sendConnectedMessage } from "./localhost";
import { peerSocket }  from "messaging";
import { onOpen, onError, onClose } from "./message-utils";


console.log("Max message size=" + peerSocket.MAX_MESSAGE_SIZE);
// Process new files as they are received
//inbox.addEventListener("newfile", processAllFilesFromDevice(sendHealthDataFile));

// Also process any files that arrived when the companion wasn’t running
//processAllFilesFromDevice(sendHealthDataFile);

// setTimeout(sendFile, 2000);

function onPeerMessage(event) {
  var message = event.data
  console.log(`MESSAGE: ${message}`);
  if (message[0] == "hb") {
    sendHealthDataMessage(message);
  } else if (message[0] == "c") {
    sendConnectedMessage();
  }
}

// add peerSocket handlers
peerSocket.addEventListener("message", onPeerMessage);
peerSocket.addEventListener("open", onOpen);
peerSocket.addEventListener("error", onError);
peerSocket.addEventListener("close", onClose);

// add webSocket Handlers
addWebsocketHandlers();



