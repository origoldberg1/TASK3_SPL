package bgu.spl.net.impl.rci;
import bgu.spl.net.impl.tftp.TftpConnections;
import bgu.spl.net.srv.BlockingConnectionHandler;
public interface Command<T>{

    void execute(T arg, BlockingConnectionHandler<byte[]> handler, TftpConnections connectionsObject);
}