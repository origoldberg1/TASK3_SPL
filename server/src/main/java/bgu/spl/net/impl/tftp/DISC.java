package bgu.spl.net.impl.tftp;
import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.srv.BlockingConnectionHandler;
public class DISC implements Command<byte[]> {

    private boolean errorFound(byte[] arg, BlockingConnectionHandler <byte[]> handler, TftpConnections connectionsObject)
    {
        //error 4- illegal TETP operation, unknown opcode
        if((arg[1]>7 || arg[1]<1 ||arg.length <= 1)){
            connectionsObject.send(handler.getId(),new ERROR (4).getError());
            return true;
        }
        return false;
    }

    @Override
    public void execute(byte[] arg, BlockingConnectionHandler <byte[]> handler, TftpConnections connectionsObject) 
    {
        //remaek- according to instructions, if the client isn't loggedin, he doesn't send a  DISC packet
        if(!errorFound(arg, handler, connectionsObject))
        {
            connectionsObject.disconnect(handler.getId());
        }
        
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