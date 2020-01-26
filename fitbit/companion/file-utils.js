import { inbox, outbox } from "file-transfer";

function sendFile() {
  console.log("Sending file...");
  let data = new Uint8Array(26);
  for (let counter = 0; counter < data.length; counter++) {
    data[counter] = "a".charCodeAt(0) + counter;
  }
  var file = new File(data, "alphabits.txt");
  outbox.enqueue("alphabits.txt", data);
}

// Process the inbox queue for files, and read their contents as text
export async function processAllFilesFromDevice(callback) {
   let file;
   while ((file = await inbox.pop())) {
     const payload = await file.text();
     //send file
     callback(file);
     console.log(`file contents: $‌{payload}`);
   }
}
