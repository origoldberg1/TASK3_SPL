package bgu.spl.net.impl.tftp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import bgu.spl.net.api.MessageEncoderDecoder;

public class SendData {
    int n;
    MessageEncoderDecoder<byte[]> encdec;
    byte[] fileBytes;
    int defaultPacketSize = 512;

    public SendData(Path filePath, MessageEncoderDecoder<byte[]> encdec){
        this.n = 0;
        this.encdec = encdec;
        try {
            fileBytes = Files.readAllBytes(filePath);
        } catch (IOException e) {
            System.err.println("cannot convert file to byte[]");
        }
    }

    public byte[] makePacket(){
        int indent = n * defaultPacketSize;
        int i = 0;
        byte[] packet = new byte[defaultPacketSize];
        while(i < packet.length && (indent + i) < fileBytes.length){
            packet[i] = fileBytes[i + indent];
            i++;
        }
        if(i < packet.length){
            trim(packet, i);
        }
        n++;
        return encdec.encode(packet);
    }

    private byte[] trim(byte[] packet, int len){
        byte[] tmp = new byte[len];
        for (int i = 0; i < tmp.length; i++) {
            tmp[i] = packet[i];
        }
        return tmp;
    }
}
