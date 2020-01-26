import { peerSocket } from "messaging";
// import {sendHealthDataMessage} from "./localhost";

export function onOpen(evt) {
  console.log("PeerSocket Opened");
}

export function onError(evt) {
  console.log(`PeerSocket Error ${evt.data}`);
}

export function onClose(evt) {
  console.log("PeerSocketClosed");
}
