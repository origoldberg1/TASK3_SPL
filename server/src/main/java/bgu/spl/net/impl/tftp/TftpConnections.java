package bgu.spl.net.impl.tftp;

import java.util.concurrent.ConcurrentHashMap;

import bgu.spl.net.srv.ConnectionHandler;
import bgu.spl.net.srv.Connections;

public class TftpConnections implements Connections<byte[]>{

    ConcurrentHashMap<Integer, ConnectionHandler<byte[]>> connections = new ConcurrentHashMap<>();

    @Override
    public void connect(int connectionId, ConnectionHandler<byte[]> handler) {
        // TODO Auto-generated method stub
        System.out.println(connectionId);
        connections.put(connectionId, handler);
    }

    @Override
    public boolean send(int connectionId, byte[] msg)  {
        // TODO Auto-generated method stub
        ConnectionHandler<byte[]> handler = connections.get(connectionId);
        handler.send(msg);
        return true;

        
    }

    @Override
    public void disconnect(int connectionId) {
        // TODO Auto-generated method stub
        connections.remove(connectionId);
    }

    @Override
    public boolean isExist(String userName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isExist'");
    }

    @Override
    public void bcast(byte[] fileNameInBytes, String fileNameString, byte b) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'bcast'");
    }

}