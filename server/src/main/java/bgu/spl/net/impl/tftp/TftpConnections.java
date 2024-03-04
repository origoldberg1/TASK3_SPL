package bgu.spl.net.impl.tftp;
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
    public boolean disconnect(int connectionId) //we didn't use this method
    {
        // TODO Auto-generated method stub
        connections.remove(connectionId);
        return true;
    }

    public boolean isExist(String userName) //we add this method in order to check if this userName is already connected
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

    public  ConcurrentHashMap<Integer, BlockingConnectionHandler<byte[]>> getConnectionsHash()
    {
        return connections;
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