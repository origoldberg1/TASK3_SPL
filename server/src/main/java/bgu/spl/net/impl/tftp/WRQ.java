package bgu.spl.net.impl.tftp;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

import bgu.spl.net.impl.rci.Command;

public class WRQ implements Command<byte[]> {

    @Override
    public byte[] execute(byte[] arg) {
        // TODO Auto-generated method stub
        byte[] ack={(byte)0x00, (byte)0x04, (byte)0x00, (byte)0x00};
        byte [] bytesFileName= new byte[arg.length-2];//acording ori we get args withoud the last byte 
        for(int i=2; i<arg.length; i++){
            bytesFileName[i-2]=arg[i];
        }
        String fileName = new String(bytesFileName, StandardCharsets.UTF_8);
        System.out.println("WRQ file name = " + fileName);             
        return ack;
    }
    
}