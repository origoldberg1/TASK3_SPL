package bgu.spl.net.impl.tftp;
import bgu.spl.net.srv.BlockingConnectionHandler;
public class DISC implements Command<byte[]> {

    private boolean errorFound(byte[] arg, BlockingConnectionHandler <byte[]> handler, TftpConnections connectionsObject)
    {
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
        if(!errorFound(arg, handler, connectionsObject)){
            connectionsObject.send(handler.getId(), new ACK(new byte[]{0,0}).getAck());
        }
        connectionsObject.disconnect(handler.getId());
    
        
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