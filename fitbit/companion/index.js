import { outbox } from "file-transfer";

function sendFile() {
  console.log("Sending file...");
  let data = new Uint8Array(26);
  for (let counter = 0; counter < data.length; counter++) {
    data[counter] = "a".charCodeAt(0) + counter;
  }
  outbox.enqueue("alphabits.txt", data);
}

setTimeout(sendFile, 2000);
