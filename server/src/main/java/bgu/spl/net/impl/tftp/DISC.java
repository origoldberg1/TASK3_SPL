package bgu.spl.net.impl.tftp;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.srv.BlockingConnectionHandler;
import bgu.spl.net.srv.Connections;

public class DISC implements Command<byte[]> {

    @Override
    public byte[] execute(byte[] arg, BlockingConnectionHandler <byte[]> handler) {
       // TODO Auto-generated method stub
        if(handler.getName()==null)
        {
           return new ERROR(6).getError();
        }
        int id=handler.getId();
        Connections connections= handler.getConnections();
        byte[] ack={(byte)0x00, (byte)0x09, (byte)0x00, (byte)0x00};
        if(connections.disconnect(id))
        {
            return new ACK("DISC").getAck();
        }
        return error;
    }
    
}
