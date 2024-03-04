package bgu.spl.net.srv;

import java.io.IOException;

public interface Connections<T> {

    void connect(int connectionId, ConnectionHandler<T> handler);

    boolean send(int connectionId, T msg);

    void disconnect(int connectionId);
    
    boolean isExist(String userName); //we add this method
    
    void bcast(byte [] fileNameInBytes, String fileNameString, byte b); //we add this method
}
