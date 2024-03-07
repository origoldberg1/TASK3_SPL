package bgu.spl.net.impl.tftp;
import java.math.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import bgu.spl.net.api.MessageEncoderDecoder;

public class SendData {
    byte[] fileBytes;
    int defaultPacketSize = 512;

    public SendData(Path filePath){
        try {
            fileBytes = Files.readAllBytes(filePath);
            int numOfPackets = (int) Math.ceil((double) fileBytes.length / defaultPacketSize);
            System.out.println("num of packets= " + numOfPackets);
        } catch (IOException e) {
            System.err.println("cannot convert file to byte[]");
        }
    }

    public byte[] makePacket(int n){
        if(n * defaultPacketSize > fileBytes.length) {
            return null;
        }
        int indent = n * defaultPacketSize;
        int i = 0;
        byte[] packet = new byte[defaultPacketSize];
        while(i < packet.length && (indent + i) < fileBytes.length){
            packet[i] = fileBytes[i + indent];
            i++;
        }
        if(i < defaultPacketSize){
            return(trim(packet, i));
        }
        return packet;
    }

    private byte[] trim(byte[] packet, int len){
        byte[] tmp = new byte[len];
        for (int i = 0; i < tmp.length; i++) {
            tmp[i] = packet[i];
        }
        return tmp;
    }
}
