package bgu.spl.net.impl.tftp;
import java.nio.charset.StandardCharsets;
import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.srv.BlockingConnectionHandler;
public class LOGRQ implements Command<byte[]> 
{

    @Override
    public void execute(byte[] arg, BlockingConnectionHandler <byte[]> handler, TftpConnections connectionsObject) 
    {
        // TODO Auto-generated method stub
        //extracting userName
        byte [] bytesUserName= new byte[arg.length-2];//acording to Ori we get args without the last byte 
        for(int i=2; i<arg.length-2; i++)
        {
            bytesUserName[i-2]=arg[i];
        }
        String userName = new String(bytesUserName, StandardCharsets.UTF_8);
        if(connectionsObject.isExistByUserName(userName))// if there is such userName it means someone with this name is already logged in
        {
            connectionsObject.send(handler.getId(), new ERROR(7).getError());
        }
        handler.setName(userName);
        connectionsObject.send(handler.getId(), new ACK(new byte[]{0,0}).getAck());
    }
    
}
