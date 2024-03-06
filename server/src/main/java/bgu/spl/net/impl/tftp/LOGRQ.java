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
        int indent = 2;
        for(int i = 0; i < arg.length; i++)
        {
            bytesUserName[i]=arg[i+2];
        }
        String userName = new String(bytesUserName, StandardCharsets.UTF_8);
        if(connectionsObject.isExistByUserName(userName))// if there is such userName it means someone with this name is already logged in
        {
            connectionsObject.send(handler.getId(), new ERROR(7).getError());
        }
        handler.setName(userName);
        //ask Yaniv:
        connectionsObject.connect(connectionsObject.connections.size(), handler);

        connectionsObject.send(handler.getId(), new ACK(new byte[]{0,0}).getAck());
    }
    
}
