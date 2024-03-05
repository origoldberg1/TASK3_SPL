package bgu.spl.net.impl.tftp;
import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.srv.BlockingConnectionHandler;
import java.io.FileOutputStream;
import java.io.IOException;

public class DATA implements Command<byte[]> {

    @Override
    public void execute(byte[] packet, BlockingConnectionHandler <byte[]> handler, TftpConnections connectionsObject) 
    {
        if(handler.getFileToWritePath()==null) //trying to write without sending WRQ request at first
        {
            connectionsObject.send(handler.getId() ,new ERROR(2).getError());
        }
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