package bgu.spl.net.impl.tftp;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;
import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.srv.BlockingConnectionHandler;

public class WRQ implements Command<byte[]> 
{
    @Override
    public void execute(byte[] arg, BlockingConnectionHandler <byte []> handler, TftpConnections connectionsObject) 
    {
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
            byte [] ackMsg=new ACK(new byte[]{0,0}).getAck();
            connectionsObject.send(handler.getId(), ackMsg);
        }catch(IOException e){connectionsObject.send(handler.getId(), new ERROR(5).getError());}      
    }
}
    
    
