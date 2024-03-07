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
    public boolean errorFound(byte[] arg, BlockingConnectionHandler <byte[]> handler, TftpConnections connectionsObject)
    {
        //error 1- File not found
        byte [] bytesFileName= new byte[arg.length-2];//Acording to Ori we get args without the last byte 
        final int INDENT = 2;
        for(int i = 0; i < bytesFileName.length; i++)
        {
            bytesFileName[i]=arg[i+INDENT];
        }
        String fileName = new String(bytesFileName, StandardCharsets.UTF_8);
        Path filePath = Paths.get("server/Files/"+fileName);
        if(!Files.exists(filePath))
        {
            connectionsObject.send(handler.getId() ,new ERROR(1).getError());
            return true;
        }
        //error 2- access violation 
        if(!Files.isWritable(filePath))
        {
            connectionsObject.send(handler.getId(),new ERROR (2).getError());
            return true;
        }
        //error 4- illegal TFTP operation- unknown opcode
        if((arg[1]>7 || arg[1]<1 ||arg.length <= 1)){ 
            connectionsObject.send(handler.getId(),new ERROR (4).getError());
            return true;
        }
        // error 6- user not logged in
        if(arg[1] != 7 && handler.getName()==null){ 
            connectionsObject.send(handler.getId(), new ERROR(6).getError());
            return true;
            }
        return false;   
    }

    @Override
    public void execute(byte[] arg, BlockingConnectionHandler <byte[]> handler, TftpConnections connectionsObject) 
    {
        
        if(!errorFound(arg,handler,connectionsObject))
        {
            byte [] bytesFileName= new byte[arg.length-2];//Acording to Ori we get args without the last byte 
            final int INDENT = 2;
            for(int i = 0; i < bytesFileName.length; i++)
            {
                bytesFileName[i]=arg[i+INDENT];
            }
            String fileName = new String(bytesFileName, StandardCharsets.UTF_8);
            try{
                //delete the file
                Path filePath = Paths.get("server/Files/"+fileName);
                Files.delete(filePath); 
                //starting broadcast
                byte [] bcastMsg= new BCAST(bytesFileName, (byte)0x00).getBcast();
                ConcurrentHashMap <Integer, BlockingConnectionHandler<byte[]>> connectionsHash =connectionsObject.getConnectionsHash();
                ConcurrentHashMap<Integer, BlockingConnectionHandler<byte[]>> copyConnectionsHash = new ConcurrentHashMap<>(connectionsHash);
                for(int i=0; i<connectionsHash.size(); i++)
                {
                    BlockingConnectionHandler <byte []> ch=copyConnectionsHash.get(i);
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
}