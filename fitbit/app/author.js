import { HeartRateSensor } from "heart-rate";
import { peerSocket } from "messaging";
import fs from "fs";
import cbor from "cbor";

export const hrLogName = "hrLog.dat";
const cleanIntervalMS = 200000;
var lastWriteTime = Date.now();
const hrm = new HeartRateSensor();    

export default function writeHeartData() {
  if (HeartRateSensor) {
    console.log("This device has a HeartRateSensor!");
    
    hrm.addEventListener("reading", () => {
      console.log(`Current heart rate: ${hrm.heartRate}`);
            
      let buf = new Uint16Array(1);
      buf[0] = hrm.heartRate
      let fd = fs.openSync(hrLogName, "a");
      fs.writeSync(fd, buf);
      fs.closeSync(fd);
      lastWriteTime = Date.now();
      
    });
    hrm.start();
  } else {
    console.log("This device does NOT have a HeartRateSensor!");
  }
}

function cleanLogFile() {
  console.log("Cleaning Data Log")
  let stats = fs.statSync(hrLogName);
  if (stats) {
    console.log("File size: " + stats.size + " bytes");
    console.log("Last modified: " + stats.mtime);
  }
  
  let buf = new Uint16Array(1);
  buf[0] = getLastDataPoint();
  fs.writeFileSync(hrLogName, buf);
  
}

export function getLastDataPoint() {
  let stats = fs.statSync(hrLogName);
  let fd = fs.openSync(hrLogName, "r");
  var buf = new ArrayBuffer(stats.size);
  fs.readSync(fd, buf);
  var uint16Arr = new Uint16Array(buf);  
  var last = uint16Arr[uint16Arr.length - 1];
  fs.closeSync(fd);
  
  return last;
}

export function timeSinceWrite() {
  var milis = Date.now() - lastWriteTime;
  if (milis < 1000 * 60) {
    return `${Math.round(milis / 1000)} s ago`
  } else {
    return `${Math.round(milis / 1000 / 60)} m ago`
  }
}

setInterval(cleanLogFile, cleanIntervalMS);

