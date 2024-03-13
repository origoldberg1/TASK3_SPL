package bgu.spl.net.impl.tftp;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import bgu.spl.net.api.BidiMessagingProtocol;
import bgu.spl.net.srv.BlockingConnectionHandler;
import bgu.spl.net.srv.ConnectionHandler;
import bgu.spl.net.srv.Connections;

public class TftpConnections implements Connections<byte[]>{
    ConcurrentHashMap<Integer, BlockingConnectionHandler<byte[]>> connections = new ConcurrentHashMap<>(); //we think integer is the id and the other one is the connectionHandler

    @Override
    public synchronized boolean connect(int connectionId, BlockingConnectionHandler<byte[]> handler) {
        connections.put(connectionId, handler);
        return true;
    }

    @Override
    public synchronized boolean send(int connectionId, byte[] msg)  {
        ConnectionHandler<byte[]> handler = connections.get(connectionId);
        if(handler == null){
        //    System.err.println("no handler for this id");
            return false;
        }
        handler.send(msg);
        return true;      
    }

    @Override
    public synchronized void disconnect(int connectionId){ 
        BlockingConnectionHandler<byte[]> handlerToDisc=connections.get(connectionId);
        connections.remove(connectionId); //"remove" client from logged-in list
        ((TftpProtocol)handlerToDisc.getProtocol()).setShouldTerminate(); //in order to finish procees gracefully
    }

    public synchronized boolean isExistByUserName(String userName){ //we add this method in order to check if this userName is already connected
        for(Map.Entry<Integer, BlockingConnectionHandler<byte[]>> ch: connections.entrySet()){
            if(ch.getValue().getName() != null &&ch.getValue().getName().equals(userName))
            {
                  return true;                
            }
        }
        return false;
    }

    public synchronized boolean isExistById(int id){ //we add this method in order to check if this userName is already connected
        return connections.containsKey(id);
    }

    public synchronized ConcurrentHashMap<Integer, BlockingConnectionHandler<byte[]>> getConnectionsHash(){
        return connections;
    }

    public synchronized ConcurrentHashMap<Integer, BlockingConnectionHandler<byte[]>> getCopyHashMap()
    {
        return new ConcurrentHashMap<>(connections);
    }

    // public void bcast(byte [] fileNameinBytes, String fileNameString, byte deletedOrAdded) //deleteOrAdded hold (byte)0x00 if the file was deleted, otherwise (byte)0x01
    // {
    //     byte [] bcastMsg= new BCAST(fileNameinBytes,deletedOrAdded).getBcast();
    //     for(int i=0; i<connections.size(); i++)
    //     {
    //         if(connections.get(i).getName()!=null) //means this CH is logged in
    //         {
    //             connections.get(i).send(bcastMsg);
    //         }
    //         if(deletedOrAdded==(byte)0x00 && connections.get(i).getFileToWritePath()!=null && connections.get(i).getFileToWritePath()==fileNameString)
    //         {
    //             connections.get(i).setFileToWritePath(null);
    //         }
    //     }
    // }

}