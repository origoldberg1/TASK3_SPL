package bgu.spl.net.impl.tftp;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.srv.BlockingConnectionHandler;
import bgu.spl.net.srv.Connections;

public class WRQ implements Command<byte[]> 
{
    @Override
    public byte[] execute(byte[] arg, BlockingConnectionHandler <byte []> handler) 
    {
        if(handler.getName()==null)
        {
            return new ERROR(6).getError();
        }
        byte [] bytesFileName= new byte[arg.length-2];//According to ori we get arg without the last byte 
        for(int i=2; i<arg.length-2; i++)
        {
            bytesFileName[i-2]=arg[i];
        }
        String fileName = new String(bytesFileName, StandardCharsets.UTF_8);             
        Path filePath = Paths.get("server/Files/"+fileName); 
        if(Files.exists(filePath))
        {
            return new ERROR(5).getError();  
        }         
        Connections connections= handler.getConnections();
        connections.bcast(bytesFileName, fileName, (byte)0x01);
        return new ACK(new byte[]{0,0}).getAck();
    }
}
    
    