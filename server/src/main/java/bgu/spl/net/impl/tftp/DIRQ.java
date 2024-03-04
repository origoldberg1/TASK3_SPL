package bgu.spl.net.impl.tftp;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.srv.BlockingConnectionHandler;

public class DIRQ implements Command<byte[]> {

    @Override
    public byte[] execute(byte[] arge, BlockingConnectionHandler <byte[]> handler) 
    {
        if(handler.getName()==null)
        {
            return new ERROR(6).getError();
        }
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
        //returning date bytes arr
        return data;
    }
    
}
