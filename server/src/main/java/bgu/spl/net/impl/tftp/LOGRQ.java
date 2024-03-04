package bgu.spl.net.impl.tftp;
import java.nio.charset.StandardCharsets;
import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.srv.BlockingConnectionHandler;
import bgu.spl.net.srv.Connections;

public class LOGRQ implements Command<byte[]> 
{

    @Override
    public byte[] execute(byte[] arg, BlockingConnectionHandler <byte[]> handler) 
    {
        // TODO Auto-generated method stub
        Connections connections=handler.getConnections();
        //extracting userName
        byte [] bytesUserName= new byte[arg.length-2];//acording to Ori we get args without the last byte 
        for(int i=2; i<arg.length-2; i++)
        {
            bytesUserName[i-2]=arg[i];
        }
        String userName = new String(bytesUserName, StandardCharsets.UTF_8);
        if(connections.isExist(userName))// if there is such userName it means someone with this name is already logged in
        {
            return new ERROR(7).getError();
        }
        handler.setName(userName);
        return new ACK(new byte[]{0,0}).getAck();
    }
    
}
