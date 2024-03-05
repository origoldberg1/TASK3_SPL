package bgu.spl.net.api;
import bgu.spl.net.impl.tftp.TftpConnections;
import bgu.spl.net.srv.BlockingConnectionHandler;
public interface MessagingProtocol<T> {
 
    void start(int connectionId, TftpConnections connections, BlockingConnectionHandler<T> connectionHandler);

    /**
     * process the given message 
     * @param msg the received message
     * @return the response to send or null if no response is expected by the client
     */
    void process(T msg);
 
    /**
     * @return true if the connection should be terminated
     */
    boolean shouldTerminate();
 
}