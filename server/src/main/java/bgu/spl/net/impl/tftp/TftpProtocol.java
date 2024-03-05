package bgu.spl.net.impl.tftp;

import bgu.spl.net.api.BidiMessagingProtocol;
import bgu.spl.net.impl.rci.Command;
import bgu.spl.net.srv.ConnectionHandler;
import bgu.spl.net.srv.Connections;

public class TftpProtocol implements BidiMessagingProtocol<byte[]>  {

    boolean shouldTerminate = false;
    ConnectionHandler<byte[]> connectionHandler;

    
    @Override
    public void start(int connectionId, Connections<byte[]> connections, ConnectionHandler<byte[]> connectionHandler) {
        // TODO implement this
        System.out.println("start");
        connections.connect(connectionId, connectionHandler);
        this.connectionHandler = connectionHandler;
        //throw new UnsupportedOperationException("Unimplemented method 'start'");

    }

    @Override
    public byte[] process(byte[] message) {
        // TODO implement this
        if(connectionHandler == null) {
            System.out.println("no connection handler");
            return null;
        }
        System.out.println("process");
        if(message.length > 1){
            Command cmd = getCommand(message);
            byte[] response = cmd.execute(message);
            //connectionHandler.send(response);
            return response;
        }

        return null;
        
        //throw new UnsupportedOperationException("Unimplemented method 'process'");
    }

    private Command<byte[]> getCommand(byte[] message) {
        switch (message[1]) {
            case 1:
                return new RRQ();
            case 2:
                return new WRQ();
            case 3:
                return new DATA();
            case 4:
                return new ACK();
            case 5:
                return new ERROR();
            case 6:
                return new DIRQ();
            case 7:
                return new LOGRQ();
            case 8:
                return new DELRQ();
            case 9:
                return new BCAST();
            case 10:
                return new DISC();
        }
        return null;

    }
    @Override
    public boolean shouldTerminate() {
        // TODO implement this
       // System.out.println("should terminate: " + shouldTerminate);
        return shouldTerminate;
        //
        //throw new UnsupportedOperationException("Unimplemented method 'shouldTerminate'");
    }


}
