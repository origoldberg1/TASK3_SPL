package bgu.spl.net.impl.tftp;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.srv.BlockingConnectionHandler;
import bgu.spl.net.srv.Connections;

public class LOGRQ implements Command<byte[]> 
{

    @Override
    public byte[] execute(byte[] arg, byte [] error, BlockingConnectionHandler <byte[]> handler) 
    {
        // TODO Auto-generated method stub
        Connections connections=handler.getConnections();
        byte[] ack={(byte)0x00, (byte)0x09, (byte)0x00, (byte)0x00};
        //extracting userName
        byte [] bytesUserName= new byte[arg.length-2];//acording ori we get args withoud the last byte 
        for(int i=2; i<arg.length-2; i++)
        {
            bytesUserName[i-2]=arg[i];
        }
        String userName = new String(bytesUserName, StandardCharsets.UTF_8);
        if(connections.isExist(userName))// if there such userName it means someone with this name is already logged in
        {
            handler.setName(userName);
            return ack;
        }
            return error;
    }
    
}
