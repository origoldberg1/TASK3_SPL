package bgu.spl.net.impl.tftp;
import java.math.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import bgu.spl.net.api.MessageEncoderDecoder;

public class SendData {
    byte[] fileBytes;
    final int DEFAULT_PACKET_SIZE = 512;
    int blockNumber = 0;
    OutputStream outputStream;

    public SendData(OutputStream outputStream, String fileFullPath){

        try {
            fileBytes = Util.readFile(fileFullPath);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
            //extracting file size (in bytes)
            //reads all data to buffer


        // try {
        //     fileBytes = Files.readAllBytes(filePath);
        //     int numOfPackets = (int) Math.ceil((double) fileBytes.length / defaultPacketSize);
        //     System.out.println("num of packets= " + numOfPackets);
        // } catch (IOException e) {
        //     System.err.println("cannot convert file to byte[]");
        // }
        this.outputStream = outputStream;
    }

    public byte[] makePacket(){
        if(blockNumber * DEFAULT_PACKET_SIZE > fileBytes.length) {
            return null;
        }
        int indent = blockNumber * DEFAULT_PACKET_SIZE;
        int i = 0;
        byte[] packet = new byte[DEFAULT_PACKET_SIZE];
        while(i < packet.length && (indent + i) < fileBytes.length){
            packet[i] = fileBytes[i + indent];
            i++;
        }
        if(i < DEFAULT_PACKET_SIZE){
            return(trim(packet, i));
        }
        return packet;
    }

    public boolean sendPacket(){
        byte[] packet = makePacket();
        if(packet == null) {return false;}
        try {
            outputStream.write(Util.padDataPacket(packet, blockNumber + 1));
            outputStream.flush();
        } catch (IOException e) {}
        blockNumber ++;
        return true;
    }


    private byte[] trim(byte[] packet, int len){
        byte[] tmp = new byte[len];
        for (int i = 0; i < tmp.length; i++) {
            tmp[i] = packet[i];
        }
        return tmp;
    }

}
