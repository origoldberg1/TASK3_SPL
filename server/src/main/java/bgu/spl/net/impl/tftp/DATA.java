package bgu.spl.net.impl.tftp;
import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.srv.BlockingConnectionHandler;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

//this class implements all about writing to files- after WRQ opcode
public class DATA implements Command<byte[]> {

    public boolean errorFound(byte[] arg, BlockingConnectionHandler <byte[]> handler, TftpConnections connectionsObject)
    {
        Path filePath = Paths.get(handler.getFileToWritePath());
        //error 1- File not found 
        if(!Files.exists(filePath))
        {
              connectionsObject.send(handler.getId(),new ERROR (1).getError());
              return true;
        }
        //error 2- access violation 
        if(!Files.isWritable(filePath))
        {
            connectionsObject.send(handler.getId(),new ERROR (2).getError());
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
    public void execute(byte[] packet, BlockingConnectionHandler <byte[]> handler, TftpConnections connectionsObject) 
    {

        if(!errorFound(packet, handler, connectionsObject))
        {
              
            try (FileOutputStream fos = new FileOutputStream(handler.getFileToWritePath())) 
            {
                byte [] dataBytes= new byte[packet.length-6];
                for (int i=0; i<dataBytes.length; i++)
                {
                    dataBytes[i]=packet[i+6]; //data information starts at sixth cell in Data Opcode
                }
                fos.write(dataBytes); //writing the data packet to file
                if(dataBytes.length<512) //means this is the last packet to write
                {
                    handler.setFileToWritePath(null);
                }
                byte [] ackMsg=new ACK(new byte[]{packet[4],packet[5]}).getAck();
                connectionsObject.send(handler.getId(),ackMsg);
            } 
            catch (IOException e) {connectionsObject.send(handler.getId(), new ERROR(2).getError());} //someone deleted the file so we can't write into anymore 
        }
        
    }
}