package bgu.spl.net.impl.tftp;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.srv.BlockingConnectionHandler;
import bgu.spl.net.srv.Connections;

public class DIRQ implements Command<byte[]> {

    @Override
    public byte[] execute(byte[] arge, byte [] error, BlockingConnectionHandler <byte[]> handler) 
    {
        File folder = new File("server/Flies");
        File[] files = folder.listFiles();
        //creating list with file names in byte (0 byte between each of them)
        List<Byte> bytesNameFiles = new ArrayList<>();
        for (File file: files)
        {
            String fileName=file.getName();
            for(byte b: fileName.getBytes())
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
