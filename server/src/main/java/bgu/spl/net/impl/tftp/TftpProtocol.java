package bgu.spl.net.impl.tftp;
import java.net.Socket;
import bgu.spl.net.api.BidiMessagingProtocol;
import bgu.spl.net.srv.BlockingConnectionHandler;
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
        System.out.println("process");
        if(message.length > 1){
            switch (message[1]) {
                case (byte)1:
                    return new RRQ().execute(message, handler);
                case (byte)2:
                    return new WRQ().execute(message, handler);
                case (byte)6:
                    return new DIRQ().execute(message, handler);
                case (byte)7:
                    return new LOGRQ().execute(message, handler);
                case (byte)8:
                    return new DELRQ().execute(message, handler);
                case (byte)10:
                    return new DISC().execute(message, handler);
                default:
                    return new ERROR (4).getError();
            }
        }
        return new ERROR (4).getError();   
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
