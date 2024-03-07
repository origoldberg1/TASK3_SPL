package bgu.spl.net.impl.tftp;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.srv.BlockingConnectionHandler;

public class DIRQ implements Command<byte[]> {
     private boolean errorFound(byte[] arg, BlockingConnectionHandler <byte[]> handler, TftpConnections connectionsObject)
    {
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
        if(!errorFound(arg, handler, connectionsObject))
        {
            File folder = new File("server/Flies");
            File[] files = folder.listFiles();
            //creating list with file names in byte (0 byte between each of them)
            List<Byte> bytesNameFiles = new ArrayList<>();
            for (File file: files) //passing each file
            {
                String fileName=file.getName();
                for(byte b: fileName.getBytes()) //passing each byte in fileName
                {
                    bytesNameFiles.add(b);   
                }
                bytesNameFiles.add((byte)0x00);
            }
            //converting bytesNameFiles list to array
            int i=0;
            byte[] data = new byte[bytesNameFiles.size()];
            for (byte b: bytesNameFiles) 
            {
                data[i] = b;
                i++;
            }
            //TODO creating the data to send
            connectionsObject.send(handler.getId(), data);
        }

    }
    
}