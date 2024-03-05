package bgu.spl.net.impl.tftp;
import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.srv.BlockingConnectionHandler;
public class DISC implements Command<byte[]> {

    @Override
    public void execute(byte[] arg, BlockingConnectionHandler <byte[]> handler, TftpConnections connectionsObject) 
    {
        // connectionsObject.disconnect(handler.getId());
        // if(handler.getName()==null) //isn't logged in
        // {
            
        // }
        // else //is logged in
        // {
        //     connectionsObject.send(handler.getId(), new ACK(new byte[]{0,0}).getAck());
        // }
        // else
        // {
        //     connectionsObject.send(handler.getId(), new ACK(new byte[]{0,0}).getAck());
        // }

    }
    
}