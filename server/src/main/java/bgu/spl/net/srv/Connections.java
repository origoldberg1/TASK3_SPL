package bgu.spl.net.srv;
import java.util.concurrent.ConcurrentHashMap;

public interface Connections<T> {

    boolean connect(int connectionId, BlockingConnectionHandler<T> handler);

    boolean send(int connectionId, T msg);

    void disconnect(int connectionId);

    boolean isExistByUserName(String userName); //we add this method

    ConcurrentHashMap<Integer, BlockingConnectionHandler<byte[]>> getConnectionsHash(); //we add this method

     boolean isExistById(int id);

}
