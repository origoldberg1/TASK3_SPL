package bgu.spl.net.impl.rci;

import java.io.Serializable;
import java.sql.Connection;

import bgu.spl.net.srv.BlockingConnectionHandler;
import bgu.spl.net.srv.Connections;

public interface Command<T>{

    byte[] execute(T arg, T error, BlockingConnectionHandler<byte[]> handler);
}
