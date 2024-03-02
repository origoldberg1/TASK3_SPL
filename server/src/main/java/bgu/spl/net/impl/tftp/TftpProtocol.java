package bgu.spl.net.impl.tftp;

import java.net.Socket;

import bgu.spl.net.api.BidiMessagingProtocol;
import bgu.spl.net.srv.BlockingConnectionHandler;
import bgu.spl.net.srv.ConnectionHandler;
import bgu.spl.net.srv.Connections;

public class TftpProtocol implements BidiMessagingProtocol<byte[]>  {

    boolean shouldTerminate = false;
    

    
    @Override
    public void start(int connectionId, Connections<byte[]> connections, ConnectionHandler<byte[]> connectionHandler) {
        // TODO implement this
        System.out.println("start");
        connections.connect(connectionId, connectionHandler);
        //throw new UnsupportedOperationException("Unimplemented method 'start'");

    }

    @Override
    public byte[] process(byte[] message) {
        // TODO implement this
        System.out.println("process");
        if(message.length > 1){
            switch (message[1]) {
                case 1:
                    return new RRQ().execute(message);
                case 2:
                    return new WRQ().execute(message);
                case 3:
                    return new DATA().execute(message);
                case 4:
                    return new ACK().execute(message);
                case 5:
                    return new ERROR().execute(message);
                case 6:
                    return new DIRQ().execute(message);
                case 7:
                    return new LOGRQ().execute(message);
                case 8:
                    return new DELRQ().execute(message);
                case 9:
                    return new BCAST().execute(message);
                case 10:
                    return new DISC().execute(message);
                default:
                    break;
            }
        }

        return null;
        
        //throw new UnsupportedOperationException("Unimplemented method 'process'");
    }

    @Override
    public boolean shouldTerminate() {
        // TODO implement this
        System.out.println("should terminate: " + shouldTerminate);
        return shouldTerminate;
        //
        //throw new UnsupportedOperationException("Unimplemented method 'shouldTerminate'");
    }


}
