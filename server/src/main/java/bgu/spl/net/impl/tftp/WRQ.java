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

public class WRQ implements Command<byte[]> 
{
    private static final String TftpProtocol = null;
    private boolean errorFound(byte[] arg, BlockingConnectionHandler <byte[]> handler, TftpConnections connectionsObject)
    {
        byte [] bytesFileName= new byte[arg.length-2];//Acording to Ori we get args without the last byte 
        final int INDENT = 2;
        for(int i = 0; i < bytesFileName.length; i++)
        {
            bytesFileName[i]=arg[i+INDENT];
        }
        String fileName = new String(bytesFileName, StandardCharsets.UTF_8);
        Path filePath = Paths.get("server/Files/"+fileName);
        //error 4- illegal TETP operation, unknown opcode
        if((arg[1]>7 || arg[1]<1 ||arg.length <= 1)){
            connectionsObject.send(handler.getId(),new ERROR (4).getError());
            return true;
        }
        //error 5- file already exists
        if(Files.exists(filePath))
        {
            connectionsObject.send(handler.getId() ,new ERROR(5).getError());
            return true;
        }
        //error 6- user not logged in
        if(arg[1] != 7 && handler.getName()==null){
            connectionsObject.send(handler.getId(), new ERROR(6).getError());
            return true;
        }
        return false;

    }
    @Override
    public void execute(byte[] arg, BlockingConnectionHandler <byte []> handler, TftpConnections connectionsObject) 
    {
        if(!errorFound(arg, handler, connectionsObject))
        {
            //extracting fileName
            byte [] bytesFileName= new byte[arg.length-2];//According to ori we get arg without the last byte 
            for(int i=2; i<arg.length; i++)
            {
                bytesFileName[i-2]=arg[i];
            }
            String fileName = new String(bytesFileName, StandardCharsets.UTF_8);              
            //trying to create the file
            File file = new File("server/Files/"+fileName);
            try {
                file.createNewFile();
                //updating protocol there is a path to write
                HoldsDataToWrite holdsDataTowrite= new HoldsDataToWrite("server/Files/"+fileName);
                ((TftpProtocol)(handler.getProtocol())).setDataToWrite(holdsDataTowrite);
                //starting broadcast
                BCAST bcast = new BCAST(bytesFileName, (byte)0x01);
                byte [] bcastMsg= bcast.getBcastMsg();
                ConcurrentHashMap <Integer, BlockingConnectionHandler<byte[]>> connectionsHash =connectionsObject.getCopyHashMap();                
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
}
    
    
