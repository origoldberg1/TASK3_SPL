package bgu.spl.net.srv;

import java.io.IOException;

public interface Connections<T> {

    boolean connect(int connectionId, BlockingConnectionHandler<T> handler);

    boolean send(int connectionId, T msg);

    boolean disconnect(int connectionId);

    boolean isExist(String userName); //we add this method
}
