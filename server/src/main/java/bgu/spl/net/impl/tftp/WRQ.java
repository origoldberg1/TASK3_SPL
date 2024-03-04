package bgu.spl.net.impl.tftp;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;

import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.srv.BlockingConnectionHandler;
import bgu.spl.net.srv.Connections;

public class WRQ implements Command<byte[]> 
{
    @Override
    public byte[] execute(byte[] arg, BlockingConnectionHandler <byte []> handler) 
    {
        if(handler.getName()==null) //if client isn't logged-in
        {
            return new ERROR(6).getError();
        }
        //extracting fileName
        byte [] bytesFileName= new byte[arg.length-2];//According to ori we get arg without the last byte 
        for(int i=2; i<arg.length; i++)
        {
            bytesFileName[i-2]=arg[i];
        }
        String fileName = new String(bytesFileName, StandardCharsets.UTF_8);              
        //trying to create th file
        File file = new File("server/Files/"+fileName);
        try {
            file.createNewFile();
            //starting broadcast
            byte [] bcastMsg= new BCAST(bytesFileName, (byte)0x01).getBcast();
            Connections connectionsObject=handler.getConnectionsObject();
            ConcurrentHashMap <Integer, BlockingConnectionHandler<byte[]>> connectionsHash =connectionsObject.getConnectionsHash();
            for(int i=0; i<connectionsHash.size(); i++)
            {
                BlockingConnectionHandler <byte []> ch=connectionsHash.get(i);
                if(ch.getName()!=null) //means this CH is logged in
                {
                    int id=ch.getId();
                    connectionsObject.send(id,bcastMsg);
                }
            }
            //finishing broadcast
            return new ACK(new byte[]{0,0}).getAck();
        }catch(IOException e){return new ERROR(5).getError();}      
    }
}
    
    