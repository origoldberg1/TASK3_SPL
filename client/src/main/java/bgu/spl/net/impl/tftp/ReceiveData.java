package bgu.spl.net.impl.tftp;

public class ReceiveData {
    int n; 
    byte[] data;
    int defaultPacketSize = 512;
    
    
    public ReceiveData() {
        this.n = 0;
        this.data = new byte[0];
    }

    public byte[] processPacket(byte[] packet){
        int indent = data.length;
        resize(packet.length);
        for (int i = 0; i < packet.length; i++) {
            data[indent + i] = packet[i];
        }
        if(packet.length < defaultPacketSize) {return data;} //save file instead?
        n++;
        return null;
    }
    
    private void resize(int addSize){
        byte[] tmp = new byte[addSize + data.length];
        for (int i = 0; i < data.length; i++) {
            tmp[i] = data[i];
        }
        data = tmp;
    }
}
