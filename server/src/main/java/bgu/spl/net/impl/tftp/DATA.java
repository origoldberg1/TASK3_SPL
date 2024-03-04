package bgu.spl.net.impl.tftp;
import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.srv.BlockingConnectionHandler;
import java.io.FileOutputStream;
import java.io.IOException;

public class DATA implements Command<byte[]> {

    @Override
    public byte[] execute(byte[] arg, BlockingConnectionHandler <byte[]> handler) 
    {
        if(handler.getName()==null) //isn't logged in
        {
            return new ERROR(6).getError();
        }
        if(handler.getFileToWritePath()==null) //trying to write without sending WRQ request at first
        {
            return new ERROR(2).getError();
        }
        try (FileOutputStream fos = new FileOutputStream(handler.getFileToWritePath())) 
        {
            byte [] dataBytes= new byte[arg.length-6];
            for (int i=0; i<dataBytes.length; i++)
            {
                dataBytes[i]=arg[i+6]; //data information starts at sixth cell in Data Opcode
            }
            fos.write(dataBytes); //writing the data packet to file
            if(dataBytes.length<512) //means this is the last packet to write
            {
                handler.setFileToWritePath(null);
            }
            return new ACK(arg[5]).getAck();
        } 
        catch (IOException e) {return new ERROR(2).getError();} //someone deleted the file so we can't write into anymore 
    }
}

