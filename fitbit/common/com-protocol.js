import * as messaging from "messaging";

export const packets = {
  HBP : 0,
  CP : 1,
}

class HeartBeatPacket {
  constructor(data) {
    this.value = data
  }
  type = 0;
}
                      
class ConnectPacket {
  type = 1;
} 
                      
               