package bgu.spl.net.impl.tftp;
import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.srv.BlockingConnectionHandler;

public class DISC implements Command<byte[]> {

    @Override
    public byte[] execute(byte[] arg, BlockingConnectionHandler <byte[]> handler) {
       // TODO Auto-generated method stub
       //according to instructions DISC remove user from Logged-in list, we chose implement loged-in mode by holding a userName
        if(handler.getName()==null) //isn't logged-in
        {
           return new ERROR(6).getError();
        }
        handler.setName(null);
        return new ACK(new byte[]{0,0}).getAck();
    }
    
}
