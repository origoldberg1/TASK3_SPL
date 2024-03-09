package bgu.spl.net.impl.tftp;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.srv.BlockingConnectionHandler;
import java.util.ArrayList;

public class RRQ implements Command<byte[]> 
{
    private boolean errorFound(byte[] arg, BlockingConnectionHandler <byte[]> handler, TftpConnections connectionsObject)
    {
        //error 1- file not found
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
        //error 2- file cannot be read
        if(!Files.isReadable(filePath))
        {
            connectionsObject.send(handler.getId() ,new ERROR(2).getError());
            return true;
        }
        //error 4- illegal TETP operation, unknown opcode
        if((arg[1]>7 || arg[1]<1 ||arg.length <= 1)){
            connectionsObject.send(handler.getId(),new ERROR (4).getError());
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
    public void execute(byte[] arg, BlockingConnectionHandler <byte[]> handler, TftpConnections connectionsObject) 
    {       
        if(!errorFound(arg,handler,connectionsObject)) //checking errors
        {
            //extracting fileName
            final int INDENT=2;
            byte [] bytesFileName= new byte[arg.length-2];
            for(int i=0; i<bytesFileName.length; i++){
                bytesFileName[i]=arg[i+INDENT];
            }
            String fileName = new String(bytesFileName, StandardCharsets.UTF_8);
            //reading file 
            byte [] dataByte=new byte[0];  
            try(FileInputStream fis = new FileInputStream("server/Files/" + fileName)){
                //extracting file size (in bytes)
                long fileSize= new File("server/Files/" + fileName).length();
                //reads all data to buffer
                dataByte = new byte[(int) fileSize];
                fis.read(dataByte);
            }catch(IOException e){}
            //creating new HoldsdataToSend object
            HoldsDataToSend dataToSend=new HoldsDataToSend(connectionsObject, dataByte, handler);
            //updating protocol to hold dataToSend object
            ((TftpProtocol)handler.getProtocol()).setHoldsDataToSend(dataToSend);
            dataToSend.sendPacket(new byte[] {0,4,0,0});
            //sending ack
        //     byte[] blockNumber= new byte[] {0,0};
        //     connectionsObject.send(handler.getId(),new ACK(blockNumber).getAck()); //note- there is no meaning for the blockNumber field type in sendPacket method 
        }
    }
}
    
