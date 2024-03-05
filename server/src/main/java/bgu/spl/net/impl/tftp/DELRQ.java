package bgu.spl.net.impl.tftp;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;
import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.srv.BlockingConnectionHandler;

public class DELRQ implements Command<byte[]> 
{
    @Override
    public void execute(byte[] arg, BlockingConnectionHandler <byte[]> handler, TftpConnections connectionsObject) 
    {
        byte [] bytesFileName= new byte[arg.length-2];//Acording to Ori we get args without the last byte 
        for(int i=2; i<arg.length-2; i++)
        {
            bytesFileName[i-2]=arg[i];
        }
        String fileName = new String(bytesFileName, StandardCharsets.UTF_8);
        try{
            Path filePath = Paths.get("server/Files/"+fileName);
            Files.delete(filePath); 
            //starting broadcast
            byte [] bcastMsg= new BCAST(bytesFileName, (byte)0x00).getBcast();
            ConcurrentHashMap <Integer, BlockingConnectionHandler<byte[]>> connectionsHash =connectionsObject.getConnectionsHash();
            for(int i=0; i<connectionsHash.size(); i++)
            {
                BlockingConnectionHandler <byte []> ch=connectionsHash.get(i);
                if(ch.getName()!=null) //means this CH is logged in
                {
                    int id=ch.getId();
                    connectionsObject.send(id,bcastMsg);
                }
                if(ch.getFileToWritePath()!=null && ch.getFileToWritePath()=="server/Files/"+fileName) 
                {
                    ch.setFileToWritePath(null);
                }
            }
            //finishing broadCast
            connectionsObject.send(handler.getId(),new ACK(new byte[]{0,0}).getAck());
        } catch(IOException e){connectionsObject.send(handler.getId() ,new ERROR(1).getError());}
    }   
}