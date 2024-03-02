package bgu.spl.net.impl.tftp;

import java.net.Socket;

import bgu.spl.net.api.BidiMessagingProtocol;
import bgu.spl.net.srv.BlockingConnectionHandler;
import bgu.spl.net.srv.ConnectionHandler;
import bgu.spl.net.srv.Connections;

public class TftpProtocol implements BidiMessagingProtocol<byte[]>  {

    boolean shouldTerminate = false;
    private BlockingConnectionHandler <byte[]> handler;
    private TftpConnections connecntions;
    private int connectionId;
    

    
    @Override
    public void start(int connectionId, Connections<byte[]> connections, BlockingConnectionHandler <byte[]>  connectionHandler) {
        // TODO implement this
        this.handler=connectionHandler;
        this.connecntions=connecntions;
        this.connectionId=handler.getId();
        System.out.println("start");
        //throw new UnsupportedOperationException("Unimplemented method 'start'");

    }

    @Override
    public byte[] process(byte[] message) {
        // TODO implement this
        byte[] error = {
            (byte) 0x00, (byte) 0x05, (byte) 0x00, (byte) 0x07,
            (byte) 0x37, (byte) 0x49, (byte) 0x41, (byte) 0x48,
            (byte) 0x20, (byte) 0x61, (byte) 0x6c, (byte) 0x72,
            (byte) 0x65, (byte) 0x61, (byte) 0x64, (byte) 0x79,
            (byte) 0x20, (byte) 0x6c, (byte) 0x6f, (byte) 0x67,
            (byte) 0x00, (byte) 0x67, (byte) 0x65, (byte) 0x64,
            (byte) 0x20, (byte) 0x69, (byte) 0x6e, (byte) 0x20,
            (byte) 0xe2, (byte) 0x80, (byte) 0x93, (byte) 0x20,
            (byte) 0x4c, (byte) 0x6f, (byte) 0x67, (byte) 0x69,
            (byte) 0x6e, (byte) 0x20, (byte) 0x75, (byte) 0x73,
            (byte) 0x65, (byte) 0x72, (byte) 0x6e, (byte) 0x61,
            (byte) 0x6d, (byte) 0x65, (byte) 0x20, (byte) 0x61,
            (byte) 0x6c, (byte) 0x72, (byte) 0x65, (byte) 0x61,
            (byte) 0x64, (byte) 0x79, (byte) 0x20, (byte) 0x63,
            (byte) 0x6f, (byte) 0x6e, (byte) 0x6e, (byte) 0x65,
            (byte) 0x63, (byte) 0x74, (byte) 0x65, (byte) 0x64,
            (byte) 0x2e
        };
        System.out.println("process");
        if(message.length > 1){
            switch (message[1]) {
                case 1:
                    return new RRQ().execute(message, error, handler);
                case 2:
                    return new WRQ().execute(message, error, handler);
                case 6:
                    return new DIRQ().execute(message, error, handler);
                case 7:
                    return new LOGRQ().execute(message, error, handler);
                case 8:
                    return new DELRQ().execute(message, error, handler);
                case 10:
                    return new DISC().execute(message, error, handler);
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
