package bgu.spl.net.impl.tftp;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import bgu.spl.net.srv.BlockingConnectionHandler;
import bgu.spl.net.srv.ConnectionHandler;
import bgu.spl.net.srv.Connections;

public class TftpConnections implements Connections<byte[]>{
    ConcurrentHashMap<Integer, BlockingConnectionHandler<byte[]>> connections = new ConcurrentHashMap<>(); //we think integer is the id and the other one is the connectionHandler

    @Override
    public boolean connect(int connectionId, BlockingConnectionHandler<byte[]> handler) {
        // TODO Auto-generated method stub
        System.out.println(connectionId);
        connections.put(connectionId, handler);
        return true;
    }

    @Override
    public boolean send(int connectionId, byte[] msg)  {
        // TODO Auto-generated method stub
        ConnectionHandler<byte[]> handler = connections.get(connectionId);
        handler.send(msg);
        return true;

        
    }

    @Override
    public boolean disconnect(int connectionId) 
    {
        // TODO Auto-generated method stub
        connections.remove(connectionId);
        return true;
    }

    public boolean isExist(String userName)
    {
        for(int i=1; i<=connections.size(); i++)
        {
            if(connections.get(i).getName()!=null && (connections.get(i).getName()==userName))
            {
                return false;
            }
        }
        return true;
    }
}