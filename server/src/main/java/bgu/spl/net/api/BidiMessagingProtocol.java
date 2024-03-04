package bgu.spl.net.api;
import bgu.spl.net.impl.tftp.TftpConnections;
import bgu.spl.net.srv.BlockingConnectionHandler;


public interface BidiMessagingProtocol<T> extends MessagingProtocol<T>  {
	/**
	 * Used to initiate the current client protocol with it's personal connection ID and the connections implementation
	**/
    void start(int connectionId, TftpConnections connections, BlockingConnectionHandler<T> connectionHandler);
    
    void process(T message);
	
	/**
     * @return true if the connection should be terminated
     */
    boolean shouldTerminate();
}
