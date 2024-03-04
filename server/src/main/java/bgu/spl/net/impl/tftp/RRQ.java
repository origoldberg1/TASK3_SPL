package bgu.spl.net.impl.tftp;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.srv.BlockingConnectionHandler;

public class RRQ implements Command<byte[]> {

    @Override
    public byte[] execute(byte[] arg, BlockingConnectionHandler <byte[]> handler) {
        if(handler.getName()==null)
        {
            return new ERROR(6).getError();
        }
        byte [] bytesFileName= new byte[arg.length-2];//acording ori we get args withoud the last byte 
        for(int i=2; i<arg.length-2; i++)
        {
            bytesFileName[i-2]=arg[i];
        }
        String fileName = new String(bytesFileName, StandardCharsets.UTF_8);             
        try{
            Path filePath = Paths.get("server/Flies"+fileName); 
            byte[] dataByte = Files.readAllBytes(filePath);
            return dataByte;

        }catch(IOException e){return new ERROR(1).getError();}
    }
    
}
