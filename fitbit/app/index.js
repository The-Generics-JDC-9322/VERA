import document from "document";
import { inbox } from "file-transfer";
import fs from "fs";

let statusText = document.getElementById("status");
statusText.text = "Waiting...";

// Event occurs when new file(s) are received
inbox.onnewfile = () => {
  console.log("New file!");
  let fileName;
  do {
    // If there is a file, move it from staging into the application folder
    fileName = inbox.nextFile();
    if (fileName) {
      console.log(`Received File: <${fileName}>`);
      let data = fs.readFileSync(fileName, "ascii");
      statusText.text = `Received: ${data}`;
    }
  } while (fileName);
};
