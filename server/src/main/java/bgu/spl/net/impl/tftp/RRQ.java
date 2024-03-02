package bgu.spl.net.impl.tftp;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.srv.BlockingConnectionHandler;
import bgu.spl.net.srv.Connections;

public class RRQ implements Command<byte[]> {

    @Override
    public byte[] execute(byte[] arg, byte [] error, BlockingConnectionHandler <byte[]> handler) {
        byte [] bytesFileName= new byte[arg.length-2];//acording ori we get args withoud the last byte 
        for(int i=2; i<arg.length-2; i++)
        {
            bytesFileName[i-2]=arg[i];
        }
        String fileName = new String(bytesFileName, StandardCharsets.UTF_8);             
        try{
            Path filePath = Paths.get("server/Flies"+fileName+".txt"); 
            byte[] dataByte = Files.readAllBytes(filePath);
            return dataByte;

        }catch(IOException e){return error;}
    }
    
}
